@file:Suppress("DEPRECATION")

package app.regate

//import com.google.firebase.example.messaging.R

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import androidx.core.app.TaskStackBuilder
import androidx.core.graphics.drawable.IconCompat
import androidx.core.net.toUri
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import app.regate.common.resources.R
import app.regate.data.AppRoomDatabase
import app.regate.data.dto.notifications.MessageGroupPayload
import app.regate.data.dto.notifications.MessagePayload
import app.regate.data.dto.notifications.SalaPayload
import app.regate.data.dto.notifications.TypeNotification
import app.regate.extensions.unsafeLazy
import app.regate.home.MainActivity
import app.regate.inject.ApplicationComponent
import app.regate.inject.DbComponent
import app.regate.models.Grupo
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.transform.CircleCropTransformation
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json



class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    private val handler = HandleNotifications()
//    val component:DbComponent = DbComponent::class.create(this)

    //    @RequiresApi(Build.VERSION_CODES.P)
    @SuppressLint("SuspiciousIndentation")
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: ${remoteMessage.from}")

//        if (isAppRunning(applicationContext)) return
        Log.d(TAG, "App no running send notifications")
        val data = remoteMessage.data
        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            if(TypeNotification.NOTIFICATION_GROUP.ordinal == data["type"]?.toInt()){
            val db = AppRoomDatabase.getInstance(applicationContext)
            try{
            val messages = Json.decodeFromString<List<MessageGroupPayload>>(data["payload"].toString())
            val grupo = db.grupoDao().getGrupo(messages[0].grupo_id)
                Log.d(TAG,"Success $messages")
                scope.launch {
                sendNotificationGroupMessage(messages,grupo,applicationContext)
                }
            AppRoomDatabase.destroyInstance()
            }catch(e:Exception){
                Log.d(TAG,e.localizedMessage?:"")
            }
            }
            if(TypeNotification.NOTIFICATION_SALA_CREATION.ordinal == data["type"]?.toInt()){
                try{

                val newSala = Json.decodeFromString<SalaPayload>(data["payload"].toString())
                    scope.launch {
                        handler.sendNotificationSalaCreation(applicationContext,newSala)
                    }
                }catch (e:Exception){
                    Log.d(TAG,e.localizedMessage?:"")
                }
            }
            if(TypeNotification.NOTIFICATION_SALA_RESERVATION_CONFLICT.ordinal == data["type"]?.toInt()){
                try{
                    val payload = Json.decodeFromString<MessagePayload>(data["payload"].toString())
                    scope.launch {
                    handler.sendNotificationSalaConflict(applicationContext,payload)
                    }
                }catch (e:Exception){
                    Log.d(TAG,e.localizedMessage?:"")
                }
            }
            if(TypeNotification.NOTIFICATION_SALA_HAS_BEEN_RESERVED.ordinal == data["type"]?.toInt()) {
                try {
                    val payload = Json.decodeFromString<MessagePayload>(data["payload"].toString())
                    scope.launch {
                        handler.sendNotificationSalaHasBeenReserved(applicationContext, payload)
                    }
                } catch (e: Exception) {
                    Log.d(TAG, e.localizedMessage ?: "")
                }
            }
            if(TypeNotification.NOTIFICATION_BILLING.ordinal == data["type"]?.toInt()){
                try{
                    val payload = Json.decodeFromString<MessagePayload>(data["payload"].toString())
                    handler.sendNotificationBilling(applicationContext,payload)
                }catch (e:Exception){
                    Log.d(TAG,e.localizedMessage?:"")
                }
            }
            // Check if data needs to be processed by long running job
            if (needsToBeScheduled()) {
                scheduleJob()
            } else {
                // Handle message within 10 seconds
                handleNow()
            }
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
        }
    }


    @SuppressLint("RestrictedApi")
    private suspend fun sendNotificationGroupMessage(
        messages:List<MessageGroupPayload>,grupo:Grupo,context:Context) {
        try {
//            val dasd = messages.elementAt(2)
            val m1 = messages[2]
            val m2 = messages[1]
            val m3 = messages[0]
            val taskDetailIntent = Intent(
                Intent.ACTION_VIEW,
                "https://example.com/grupo_id=${grupo.id}".toUri(),
                this,
                MainActivity::class.java
            )
            val taskBuilder = TaskStackBuilder.create(this)
            taskBuilder.addNextIntentWithParentStack(taskDetailIntent)
            val pendingIntent = taskBuilder.getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)

            val persons = mutableListOf<Person>()
            messages.map {
                val person = Person.Builder()
                    .setIcon(IconCompat.createWithBitmap(getBitmap(it.profile_photo?:"")))
                    .setName("${it.profile_name} ${it.profile_apellido?:""}" )
                    .build()
                persons.add(person)
            }
//            Log.d(TAG,bit.toString())
            val CHANNEL_ID = context.getString(R.string.chat_group_channel_id)
            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setStyle(
                    NotificationCompat.MessagingStyle("Me")
                        .setConversationTitle(grupo.name)
                        .setGroupConversation(true)
                        .addMessage(m1.content, m1.created_at.epochSeconds, persons[2])
                        .addMessage(m2.content, m2.created_at.epochSeconds, persons[1])
                        .addMessage(m3.content, m3.created_at.epochSeconds, persons[0])
                )
                .setSmallIcon(R.drawable.logo_app)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

            with(NotificationManagerCompat.from(this)) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                Log.d(TAG,"SENDIN NOTIFICATION")
                notify(grupo.id.toInt(), notification)
            }

        }catch(e:Exception){
            Log.d(TAG,e.localizedMessage?:"")
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private suspend fun getBitmap(url:String):Bitmap{
        val bitmap = CoroutineScope(Dispatchers.IO).async {

        val loader = ImageLoader(applicationContext)
        val request = ImageRequest.Builder(applicationContext)
            .data(url.ifBlank { "https://cdn-icons-png.flaticon.com/128/847/847969.png" })
            .allowHardware(false) // Disable hardware bitmaps.
            .transformations(CircleCropTransformation())
            .build()

        val result = (loader.execute(request) as SuccessResult).drawable
        val bitmap = (result as BitmapDrawable).bitmap
            return@async bitmap
        }
        return bitmap.await()
    }


    private fun needsToBeScheduled() = true

    // [START on_new_token]
    /**
     * Called if the FCM registration token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the
     * FCM registration token is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token New Token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        sendRegistrationToServer(token)
    }
    // [END on_new_token]

    private fun scheduleJob() {
        // [START dispatch_job]
        val work = OneTimeWorkRequest.Builder(MyWorker::class.java)
            .build()
        WorkManager.getInstance(this)
            .beginWith(work)
            .enqueue()
        // [END dispatch_job]
    }

    private fun handleNow() {
        Log.d(TAG, "Short lived task is done.")
    }

    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implement this method to send token to your app server.
        Log.d(TAG, "sendRegistrationTokenToServer($token)")
    }


    companion object {
        private const val TAG = "DEBUG_APP_NOTIFICATIONS"
    }

    private fun isAppRunning(context: Context): Boolean {
        try{
        val packageName = context.packageName
        val activityManager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val procInfos = activityManager.runningAppProcesses
        if (procInfos != null) {
            for (processInfo in procInfos) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND &&
                    processInfo.processName.equals(packageName)) {
                    return true;
                }
            }
        }
        return false
        }catch(e:Exception){
            Log.d(TAG,e.localizedMessage?:"")
            return false
        }
    }

    internal class MyWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {
        override fun doWork(): Result {
            // TODO(developer): add long running task here.
            return Result.success()
        }
    }
}
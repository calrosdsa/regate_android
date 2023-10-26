@file:Suppress("DEPRECATION")

package app.regate

//import com.google.firebase.example.messaging.R

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.util.Log
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import app.regate.data.AppRoomDatabase
import app.regate.data.dto.notifications.MessageGroupPayload
import app.regate.data.dto.notifications.MessagePayload
import app.regate.data.dto.notifications.SalaConflictPayload
import app.regate.data.dto.notifications.SalaPayload
import app.regate.data.dto.notifications.TypeNotification
import app.regate.notifications.HandleNotificationAccount
import app.regate.notifications.HandleNotificationGrupo
import app.regate.notifications.HandleNotificationSala
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json



class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    private val salaHandler = HandleNotificationSala()
    private val accountHandler = HandleNotificationAccount()
    private val grupoHandler = HandleNotificationGrupo()
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
                scope.launch {
                    try {
                        val chat = db.chatDao().getChat(messages[0].message.chat_id)
                        if(chat != null){
                        Log.d(TAG,"Success CHat $chat")
                        Log.d(TAG,"Success $messages")
                        grupoHandler.sendNotificationGroupMessage(messages,chat,applicationContext)
                        }
                    }catch (e:Exception){
                        throw  e
                    }
                }
            AppRoomDatabase.destroyInstance()
            }catch(e:Exception){
                Log.d(TAG,"ERROR HERE")
                Log.d(TAG,e.localizedMessage?:"")
            }
            }
            if(TypeNotification.NOTIFICATION_SALA_CREATION.ordinal == data["type"]?.toInt()){
                try{

                val newSala = Json.decodeFromString<SalaPayload>(data["payload"].toString())
                    scope.launch {
                        salaHandler.sendNotificationSalaCreation(applicationContext,newSala)
                    }
                }catch (e:Exception){
                    Log.d(TAG,e.localizedMessage?:"")
                }
            }
            if(TypeNotification.NOTIFICATION_SALA_RESERVATION_CONFLICT.ordinal == data["type"]?.toInt()){
                try{
                    val payload = Json.decodeFromString<SalaConflictPayload>(data["payload"].toString())
                    scope.launch {
                    salaHandler.sendNotificationSalaConflict(applicationContext,payload,data["payload"].toString())
                    }
                }catch (e:Exception){
                    Log.d(TAG,e.localizedMessage?:"")
                }
            }
            if(TypeNotification.NOTIFICATION_SALA_HAS_BEEN_RESERVED.ordinal == data["type"]?.toInt()) {
                try {
                    val payload = Json.decodeFromString<MessagePayload>(data["payload"].toString())
                    scope.launch {
                        salaHandler.sendNotificationSalaHasBeenReserved(applicationContext, payload)
                    }
                } catch (e: Exception) {
                    Log.d(TAG, e.localizedMessage ?: "")
                }
            }
            if(TypeNotification.NOTIFICATION_BILLING.ordinal == data["type"]?.toInt()){
                try{
                    val payload = Json.decodeFromString<MessagePayload>(data["payload"].toString())
//                    scope.launch {
                        accountHandler.sendNotificationBilling(applicationContext,payload)
//                    }
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
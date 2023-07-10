@file:Suppress("DEPRECATION")

package app.regate

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Icon
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import androidx.core.graphics.drawable.IconCompat
import app.regate.common.resources.R
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import app.regate.home.MainActivity
import coil.ImageLoader
import coil.request.ImageRequest
//import com.google.firebase.example.messaging.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import okhttp3.internal.notify

class MyFirebaseMessagingService : FirebaseMessagingService() {

    // [START receive_message]
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
            sendNotificationGroupMessage()

            // Check if data needs to be processed by long running job
            if (needsToBeScheduled()) {
                // For long-running tasks (10 seconds or more) use WorkManager.
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

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    private fun needsToBeScheduled() = true

    // [START on_new_token]
    /**
     * Called if the FCM registration token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the
     * FCM registration token is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

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



    @SuppressLint("RestrictedApi")
    @RequiresApi(Build.VERSION_CODES.P)
    private fun sendNotificationGroupMessage() {
        try {
            val CHANNEL_ID = applicationContext.getString(R.string.chat_group_channel_id)
            var bitmap:Bitmap? = null
            val loader = ImageLoader(this)
            val req = ImageRequest.Builder(this)
                .data("https://static.vecteezy.com/system/resources/previews/021/548/095/original/default-profile-picture-avatar-user-avatar-icon-person-icon-head-icon-profile-picture-icons-default-anonymous-user-male-and-female-businessman-photo-placeholder-social-network-avatar-portrait-free-vector.jpg") // demo link
                .target { result ->
                     bitmap = (result as BitmapDrawable).bitmap
                }
                .build()
            loader.enqueue(req)

//            val icon =
//                Icon.createWithContentUri("https://static.vecteezy.com/system/resources/previews/021/548/095/original/default-profile-picture-avatar-user-avatar-icon-person-icon-head-icon-profile-picture-icons-default-anonymous-user-male-and-female-businessman-photo-placeholder-social-network-avatar-portrait-free-vector.jpg")
            val person = Person.Builder()
                .setName("User")
//            .setIcon()
                .setIcon(bitmap?.let { IconCompat.createWithBitmap(it) })
                .build()
            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setStyle(
                    NotificationCompat.MessagingStyle("Me")
                        .setConversationTitle("Team lunch")
                        .addMessage("Hello", 0L, person)
                        .addMessage("Hi", 0L, person)
                        .addMessage("Hi", 0L, person) // Pass in null for user.
                        .addMessage("What's up?", 0L, person)
                        .addMessage("Not much", 200L, person)
//                .addMessage("How about lunch?", 300L, person)
                )
                .setSmallIcon(R.drawable.logo_app)
                .build()

            with(NotificationManagerCompat.from(this)) {
                // notificationId is a unique int for each notification that you must define
//            notify(notificationId, builder.build())
                if (ActivityCompat.checkSelfPermission(
                        applicationContext,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                notify(1, notification)
            }
        }catch(e:Exception){
            Log.d(TAG,e.localizedMessage?:"")
        }
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }

    internal class MyWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {
        override fun doWork(): Result {
            // TODO(developer): add long running task here.
            return Result.success()
        }
    }
}
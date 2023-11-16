package app.regate.notifications

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.net.toUri
import app.regate.common.resources.R
import app.regate.constant.AppUrl
import app.regate.constant.MainPages
import app.regate.constant.Route
import app.regate.data.AppRoomDatabase
import app.regate.data.dto.notifications.MessagePayload
import app.regate.home.MainActivity

class HandleNotificationAccount {
    companion object{
        private const val TAG = "DEBUG_APP_NOTIFICATIONS"
    }
    fun sendNotificationBilling(context: Context, payload: MessagePayload){
        try{
//            val db = AppRoomDatabase.getInstance(context)
//            db.notificationDao().upsert(
//                Notification(
//                    content = payload.message,
//                    typeEntity = TypeEntity.BILLING,
//                    entityId = payload.id
//                )
//            )
//            AppRoomDatabase.destroyInstance()
            val taskDetailIntent = Intent(
                Intent.ACTION_VIEW,
                "$AppUrl/${Route.NOTIFICATIONS}/${MainPages.Notifications}".toUri(),
                context,
                MainActivity::class.java
            )
            val taskBuilder = TaskStackBuilder.create(context)
            taskBuilder.addNextIntentWithParentStack(taskDetailIntent)
            val pendingIntent = taskBuilder.getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)

            val CHANNEL_ID = context.getString(R.string.notification_billing_channel)
            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(payload.title)
                .setContentText(payload.message)
                .setSmallIcon(R.drawable.logo_app)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

            with(NotificationManagerCompat.from(context)) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                notify(payload.id.toInt(), notification)
            }
        }catch(e:Exception){
            AppRoomDatabase.destroyInstance()
            Log.d(TAG,e.localizedMessage?:"")
        }
    }
}
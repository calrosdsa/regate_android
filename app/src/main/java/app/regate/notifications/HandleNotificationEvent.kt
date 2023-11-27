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
import app.regate.constant.AppUrl1
import app.regate.constant.MainPages
import app.regate.constant.Route
import app.regate.data.AppRoomDatabase
import app.regate.data.dto.system.NotificationDto
import app.regate.data.mappers.toNotification
import app.regate.home.MainActivity

class HandleNotificationEvent {
    companion object{
        private const val TAG = "DEBUG_APP_NOTIFICATIONS"
    }
    suspend fun sendNotificationEvent(context: Context, payload: NotificationDto){
        try{
            val defaultImage = "https://cdn-icons-png.flaticon.com/128/4239/4239989.png"
            val image= Util.getBitmap(payload.image?:"",context,defaultImage)
            val db = AppRoomDatabase.getInstance(context)
            db.notificationDao().upsert(
               payload.toNotification()
                   .copy(
                       image =payload.image?:defaultImage
                   )
            )
            AppRoomDatabase.destroyInstance()
            val taskDetailIntent = Intent(
                Intent.ACTION_VIEW,
                "$AppUrl1/${Route.NOTIFICATIONS}/${MainPages.Notifications}".toUri(),
                context,
                MainActivity::class.java
            )
            val taskBuilder = TaskStackBuilder.create(context)
            taskBuilder.addNextIntentWithParentStack(taskDetailIntent)
            val pendingIntent = taskBuilder.getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)

            val CHANNEL_ID = context.getString(R.string.notification_billing_channel)
            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(payload.title)
                .setContentText(payload.content)
                .setSmallIcon(R.drawable.logo_app)
                .setLargeIcon(image)
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
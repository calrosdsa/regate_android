package app.regate

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
import app.regate.data.dto.notifications.MessagePayload
import app.regate.data.dto.notifications.SalaPayload
import app.regate.home.MainActivity

class HandleNotifications {

    companion object{
        private const val TAG = "DEBUG_APP_NOTIFICATIONS"
    }

 fun sendNotificationSalaCreation(context: Context, payload: SalaPayload){
    try{
        val taskDetailIntent = Intent(
            Intent.ACTION_VIEW,
            "https://example.com/sala_id=${payload.id}".toUri(),
            context,
            MainActivity::class.java
        )
        val taskBuilder = TaskStackBuilder.create(context)
        taskBuilder.addNextIntentWithParentStack(taskDetailIntent)
        val pendingIntent = taskBuilder.getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
        val CHANNEL_ID = context.getString(R.string.notification_sala_channel)
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(context.getString(R.string.new_room_created))
            .setContentText(payload.titulo)
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
        Log.d(TAG,e.localizedMessage?:"")
    }
   }

    fun sendNotificationSalaConflict(context: Context, payload: MessagePayload){
        try{
            val taskDetailIntent = Intent(
                Intent.ACTION_VIEW,
                "https://example.com/sala_id=${payload.id}".toUri(),
                context,
                MainActivity::class.java
            )
            val taskBuilder = TaskStackBuilder.create(context)
            taskBuilder.addNextIntentWithParentStack(taskDetailIntent)
            val pendingIntent = taskBuilder.getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
            val CHANNEL_ID = context.getString(R.string.notification_sala_channel)
            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(context.getString(R.string.problem_with_room))
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
            Log.d(TAG,e.localizedMessage?:"")
        }
    }

    fun sendNotificationSalaHasBeenReserved(context: Context, payload: MessagePayload){
        try{
            val taskDetailIntent = Intent(
                Intent.ACTION_VIEW,
                "https://example.com/sala_id=${payload.id}".toUri(),
                context,
                MainActivity::class.java
            )
            val taskBuilder = TaskStackBuilder.create(context)
            taskBuilder.addNextIntentWithParentStack(taskDetailIntent)
            val pendingIntent = taskBuilder.getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
            val CHANNEL_ID = context.getString(R.string.notification_sala_channel)
            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(context.getString(R.string.room_has_been_reserved))
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
            Log.d(TAG,e.localizedMessage?:"")
        }
    }

}

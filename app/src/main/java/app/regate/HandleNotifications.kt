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
import app.regate.constant.Route
import app.regate.data.AppRoomDatabase
import app.regate.data.daos.NotificationDao
import app.regate.data.dto.notifications.MessagePayload
import app.regate.data.dto.notifications.SalaConflictPayload
import app.regate.data.dto.notifications.SalaPayload
import app.regate.home.MainActivity
import app.regate.inject.ApplicationComponent
import app.regate.models.Notification
import app.regate.models.TypeEntity
import me.tatarka.inject.annotations.Inject

class HandleNotifications {
    companion object{
        private const val TAG = "DEBUG_APP_NOTIFICATIONS"
    }

 suspend fun sendNotificationSalaCreation(context: Context, payload: SalaPayload){
    try{
        val title = context.getString(R.string.new_room_created)
        val db = AppRoomDatabase.getInstance(context)
        db.notificationDao().upsert(
            Notification(
                title = title,
                content = payload.titulo,
                typeEntity = TypeEntity.SALA,
                entityId = payload.id
            )
        )
        AppRoomDatabase.destroyInstance()
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
            .setContentTitle(title)
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
        AppRoomDatabase.destroyInstance()
        Log.d(TAG,e.localizedMessage?:"")
    }
   }

    suspend fun sendNotificationSalaConflict(context: Context, payload: SalaConflictPayload,payloadString:String){
        try{
            val title = context.getString(R.string.problem_with_room)
            val db = AppRoomDatabase.getInstance(context)
            db.notificationDao().upsert(
                Notification(
                    content = "Lamentamos informarte que alguien más ha reservado la cancha que habías seleccionado para la sala.",
                    typeEntity = TypeEntity.NONE,
                    entityId = payload.id,
                    title = title
                )
            )
            AppRoomDatabase.destroyInstance()
            val taskDetailIntent = Intent(
                Intent.ACTION_VIEW,
                "https://example.com/discover/data=${payloadString}".toUri(),
                context,
                MainActivity::class.java
            )
            val taskBuilder = TaskStackBuilder.create(context)
            taskBuilder.addNextIntentWithParentStack(taskDetailIntent)
            val pendingIntent = taskBuilder.getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
            val CHANNEL_ID = context.getString(R.string.notification_sala_channel)
//            val snoozeIntent = Intent(this, MyBroadcastReceiver::class.java).apply {
//                action = ACTION_SNOOZE
//                putExtra(EXTRA_NOTIFICATION_ID, 0)
//            }
//            val snoozePendingIntent: PendingIntent =
//                PendingIntent.getBroadcast(this, 0, snoozeIntent, 0)
            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(payload.message)
                .setSmallIcon(R.drawable.logo_app)
//                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .addAction(
                    androidx.core.R.drawable.notification_action_background,"Ver mas opciones",
                    pendingIntent)
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

    suspend fun sendNotificationSalaHasBeenReserved(context: Context, payload: MessagePayload){
        try{
            val title = context.getString(R.string.room_has_been_reserved)
            val db = AppRoomDatabase.getInstance(context)
            db.notificationDao().upsert(
                Notification(
                    content = payload.message,
                    title = title,
                    typeEntity = TypeEntity.SALA,
                    entityId = payload.id
                )
            )
            AppRoomDatabase.destroyInstance()
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
                .setContentTitle(title)
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
                "https://example.com/${Route.NOTIFICATIONS}".toUri(),
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

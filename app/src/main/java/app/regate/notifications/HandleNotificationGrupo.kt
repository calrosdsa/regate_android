@file:Suppress("DEPRECATION")

package app.regate.notifications

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import androidx.core.app.TaskStackBuilder
import androidx.core.graphics.drawable.IconCompat
import androidx.core.net.toUri
import app.regate.MyFirebaseMessagingService
import app.regate.common.resources.R
import app.regate.data.dto.notifications.MessageGroupPayload
import app.regate.home.MainActivity
import app.regate.models.Grupo

class HandleNotificationGrupo {
    companion object{
        private const val TAG = "DEBUG_APP_NOTIFICATIONS"
    }
    @SuppressLint("RestrictedApi")
    suspend fun sendNotificationGroupMessage(
        messages:List<MessageGroupPayload>, grupo: Grupo, context: Context
    ) {
        try {
//            val dasd = messages.elementAt(2)
            val m1 = messages[2]
            val m2 = messages[1]
            val m3 = messages[0]
            val taskDetailIntent = Intent(
                Intent.ACTION_VIEW,
                "https://example.com/chat-grupo/grupo_id=${grupo.id}".toUri(),
                context,
                MainActivity::class.java
            )
            val taskBuilder = TaskStackBuilder.create(context)
            taskBuilder.addNextIntentWithParentStack(taskDetailIntent)
            val pendingIntent = taskBuilder.getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)

            val persons = mutableListOf<Person>()
            messages.map {
                val person = Person.Builder()
                    .setIcon(IconCompat.createWithBitmap(getBitmap(it.profile_photo?:"",context)))
                    .setName("${it.profile_name} ${it.profile_apellido?:""}" )
                    .build()
                persons.add(person)
            }
//            Log.d(TAG,bit.toString())
            val CHANNEL_ID = context.getString(R.string.chat_group_channel_id)
            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
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

            with(NotificationManagerCompat.from(context)) {
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
}
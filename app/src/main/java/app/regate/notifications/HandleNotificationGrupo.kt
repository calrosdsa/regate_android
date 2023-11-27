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
import app.regate.common.resources.R
import app.regate.constant.AppUrl1
import app.regate.constant.Route
import app.regate.data.dto.notifications.MessageGroupPayload
import app.regate.home.MainActivity
import app.regate.models.chat.Chat

class HandleNotificationGrupo {
    companion object{
        private const val TAG = "DEBUG_APP_NOTIFICATIONS"
    }
    @SuppressLint("RestrictedApi")
    suspend fun sendNotificationGroupMessage(
        messages:List<MessageGroupPayload>, chat: Chat, context: Context
    ) {
        try {
//            val dasd = messages.elementAt(2)
//            val lastMessage= messages[0]
//            val db = AppRoomDatabase.getInstance(context)
//            db.myGroupsDao().deleteByGroupId(grupo.id)
//            db.myGroupsDao().upsert(
//                MyGroups(
//                    group_id = grupo.id,
//                    request_estado = GrupoRequestEstado.JOINED,
//                    last_message = lastMessage.content,
//                    last_message_created = lastMessage.created_at,
//                    messages_count = 1,
//                )
//            )
//            db.myGroupsDao().updateLastMessageGrupo(grupo.id,lastMessage.content,lastMessage.created_at)
//            db.messageProfileDao().upsert(
//                Message(
//                    id = lastMessage.id,
//                    content = lastMessage.content,
//                    grupo_id = lastMessage.grupo_id,
//                    profile_id = lastMessage.profile_id,
//                    created_at = lastMessage.created_at,
//                    reply_to = lastMessage.reply_to,
//                    type_message = lastMessage.type_message,
//                )
//            )
//            AppRoomDatabase.destroyInstance()
            val m1 = messages[2]
            val m2 = messages[1]
            val m3 = messages[0]
            val taskDetailIntent = Intent(
                Intent.ACTION_VIEW,
                "$AppUrl1/${Route.CHAT_GRUPO}/${chat.id}/${chat.parent_id}/${chat.type_chat}".toUri(),
                context,
                MainActivity::class.java
            )

            Log.d("DEBUG_APP","$AppUrl1/${Route.CHAT_GRUPO}/${chat.id}/${chat.parent_id}/${chat.type_chat}")
//            val pendingIntent = TaskStackBuilder.create(context).run {
//                addNextIntentWithParentStack(taskDetailIntent)
//                getPendingIntent(0,
//                    PendingIntent.FLAG_IMMUTABLE)
//            }
            val taskBuilder = TaskStackBuilder.create(context)
            taskBuilder.addNextIntentWithParentStack(taskDetailIntent)
            val pendingIntent = taskBuilder.getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)

            val persons = mutableListOf<Person>()
            messages.map {

                val person = Person.Builder()
                    .setIcon(IconCompat.createWithBitmap(Util.getBitmap(it.profile.profile_photo?:"",context)))
                    .setName("${it.profile.name} ${it.profile.apellido?:""}" )
                    .build()
                persons.add(person)
            }
//            Log.d(TAG,bit.toString())
            val CHANNEL_ID = context.getString(R.string.chat_group_channel_id)
            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setStyle(
                    NotificationCompat.MessagingStyle("Me")
                        .setConversationTitle(chat.name)
                        .setGroupConversation(true)
                        .addMessage(m1.message.content, m1.message.created_at.epochSeconds, persons[2])
                        .addMessage(m2.message.content, m2.message.created_at.epochSeconds, persons[1])
                        .addMessage(m3.message.content, m3.message.created_at.epochSeconds, persons[0])
                )
                .setSmallIcon(R.drawable.logo_app)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
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
                notify(chat.id.toInt(), notification)
            }

        }catch(e:Exception){
//            AppRoomDatabase.destroyInstance()
            Log.d(TAG,e.localizedMessage?:"")
        }
    }
}
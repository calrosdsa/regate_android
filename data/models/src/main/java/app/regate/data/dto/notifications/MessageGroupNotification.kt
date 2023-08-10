package app.regate.data.dto.notifications

import app.regate.data.dto.empresa.grupo.GrupoMessageDto
import app.regate.data.dto.empresa.grupo.ReplyMessage
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class MessageGroupNotification (
    val entity:GrupoMessageDto,
    val priority:String,
    val tyoe:String,
    val title:String,
)

@Serializable
data class MessageGroupPayload(
    val content: String,
    val created_at: Instant,
    val id: Long = 0,
//    val profile_id: Long,
    val grupo_id:Long,
    val profile_name:String,
    val profile_apellido:String? = null,
    val profile_photo:String? = null
)


enum class TypeNotification {
    NOTIFICATION_GROUP,
    NOTIFICATION_MESSAGE_COMPLEJO,
    NOTIFICATION_SALA_CREATION,
    NOTIFICATION_SALA_RESERVATION_CONFLICT,
}


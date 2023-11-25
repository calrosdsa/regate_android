package app.regate.data.dto.notifications

import app.regate.data.dto.empresa.grupo.GrupoMessageDto
import app.regate.data.dto.empresa.grupo.GrupoMessageType
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
    val message:GrupoMessageDto,
    val profile:ProfileBaseDto
)


@Serializable
data class ProfileBaseDto(
    val id:Long,
    val name:String,
    val apellido:String? = null,
    val profile_photo:String? = null
)

enum class TypeNotification {
    NOTIFICATION_GROUP,
    NOTIFICATION_MESSAGE_COMPLEJO,
    NOTIFICATION_SALA_CREATION,
    NOTIFICATION_SALA_RESERVATION_CONFLICT,
    NOTIFICATION_SALA_HAS_BEEN_RESERVED,
    NOTIFICATION_BILLING,
    NOTIFICATION_EVENT
}


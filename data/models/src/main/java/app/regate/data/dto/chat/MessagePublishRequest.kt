package app.regate.data.dto.chat

import app.regate.data.dto.empresa.grupo.GrupoMessageDto
import kotlinx.serialization.Serializable


@Serializable
data class MessagePublishRequest(
    val message:GrupoMessageDto,
    val type_chat:Int,
    val chat_id:Long,
)


@Serializable
data class MessagePublishResponse(
    val id:Long
)
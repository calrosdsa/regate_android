package app.regate.data.dto.chat

import app.regate.models.TypeEntity
import kotlinx.serialization.Serializable

@Serializable
data class MessageEvent(
    val type:String = MessageEventType.EventTypeMessage,
    val payload:String
  )

data class NotifyNewUserRequest(
    val typeEntity: TypeEntity,
    val parentId:Long,
    val id:Long,
    val profileId:Long
)

@Serializable
data class IdDto(
    val id:Long
)
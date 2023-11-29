package app.regate.data.dto.chat

import kotlinx.serialization.Serializable

@Serializable
data class MessageEvent(
    val type:String = MessageEventType.EventTypeMessage,
    val payload:String
  )

@Serializable
data class NotifyNewUserRequest(
    val type_chat: Int,
    val parentId:Long,
    val id:Long,
    val profileId:Long,
    val is_out:Boolean= false,
)

@Serializable
data class IdDto(
    val id:Long
)
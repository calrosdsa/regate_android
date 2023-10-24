package app.regate.data.dto.chat

import kotlinx.serialization.Serializable

@Serializable
data class MessageEvent(
    val type:String = MessageEventType.EventTypeMessage,
    val payload:String
  )
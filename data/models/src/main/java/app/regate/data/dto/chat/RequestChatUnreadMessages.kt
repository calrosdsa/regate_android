package app.regate.data.dto.chat

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class RequestChatUnreadMessages(
    val chat_id:Long,
    val last_update_chat:Instant,
    val type_chat:Int
)

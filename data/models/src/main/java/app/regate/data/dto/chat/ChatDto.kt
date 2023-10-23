package app.regate.data.dto.chat

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class ChatDto(
    val id:Long =0,
    val photo:String? = null,
    val name:String = "",
    val last_message: String? = null,
    val last_message_created:Instant? = null,
    val messages_count:Int = 0
)

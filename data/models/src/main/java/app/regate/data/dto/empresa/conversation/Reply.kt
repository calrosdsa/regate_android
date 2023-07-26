package app.regate.data.dto.empresa.conversation

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Reply(
    val content: String = "",
    val created_at: Instant? = null,
    val conversation_id: Long = 0,
    val id: Long =0,
    val reply_to:Long? = null,
    val sender_id: Long = 0
)
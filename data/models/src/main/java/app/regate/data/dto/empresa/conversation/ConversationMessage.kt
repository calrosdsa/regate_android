package app.regate.data.dto.empresa.conversation

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class ConversationMessage(
    val content: String,
    val conversation_id: Long,
    val created_at: Instant? = null,
    val id: Long,
    val reply: Reply = Reply(),
    val reply_to: Long? = null,
    val sender_id: Long
)
package app.regate.data.dto.empresa.conversation

import app.regate.data.dto.empresa.grupo.GrupoMessageDto
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

@Serializable
data class PaginationConversationMessages(
    val nextPage:Int,
    val results:List<ConversationMessage>
)

@Serializable
data class ConversationId(
    val id:Long
)
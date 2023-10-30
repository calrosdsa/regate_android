package app.regate.data.dto.chat

import kotlinx.serialization.Serializable

@Serializable
data class DeleteMessageRequest(
    val id:Long,
    val ids:List<Long>,
    val chat_id:Long,
    val type_chat:Int
)

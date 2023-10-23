package app.regate.data.dto.chat

import kotlinx.serialization.Serializable

@Serializable
data class PaginateChatResponse(
    val results:List<ChatDto> = emptyList(),
    val page:Int
)

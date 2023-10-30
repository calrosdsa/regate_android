package app.regate.data.dto.chat

import kotlinx.serialization.Serializable

@Serializable
data class DeletedMessagesIds(
    val ids:List<Long> = emptyList()
)

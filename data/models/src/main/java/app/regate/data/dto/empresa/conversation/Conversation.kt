package app.regate.data.dto.empresa.conversation

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Conversation(
    val establecimiento_id: Long,
    val establecimiento_name: String,
    val establecimiento_photo: String,
    val id: Long,
    val last_message:String,
    val last_message_created:Instant,
)
package app.regate.data.dto.notifications

import kotlinx.serialization.Serializable

@Serializable
data class MessagePayload(
    val message:String,
//    val title:String,
    val id:Long
)



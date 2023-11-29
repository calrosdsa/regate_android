package app.regate.data.dto.chat

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class RequestUserGroupAndRoom(
    val parent_id:Long,
    val type_chat:Int,
    val last_updated:Instant? = null
)

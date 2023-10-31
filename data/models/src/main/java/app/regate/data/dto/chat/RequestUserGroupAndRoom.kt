package app.regate.data.dto.chat

import kotlinx.serialization.Serializable

@Serializable
data class RequestUserGroupAndRoom(
    val parent_id:Long,
    val type_chat:Int,
    val active_users_count:Int =0,
    val inactive_users_count:Int =0
)

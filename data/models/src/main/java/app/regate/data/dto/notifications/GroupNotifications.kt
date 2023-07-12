package app.regate.data.dto.notifications

import kotlinx.serialization.Serializable

@Serializable
data class SalaPayload(
    val id:Long,
    val titulo:String,
    val grupo_id:Long
)
package app.regate.data.dto.empresa.salas

import kotlinx.serialization.Serializable

@Serializable
data class JoinSalaRequest(
    val sala_id:Long,
    val precio:Int,
    val profile_id:Long
)

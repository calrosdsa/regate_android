package app.regate.data.dto.empresa.salas

import app.regate.data.dto.notifications.ProfileBaseDto
import kotlinx.serialization.Serializable

@Serializable
data class JoinSalaRequest(
    val sala_id:Long,
    val precio_sala:Double,
    val profile_id:Long,
    val cupos:Int,
    val grupo_Id:Long,
    val profile:ProfileBaseDto? = null,
)

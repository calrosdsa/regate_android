package app.regate.data.dto.empresa.salas

import kotlinx.serialization.Serializable

@Serializable
data class JoinSalaRequest(
    val sala_id:Long,
    val precio_sala:Int,
    val profile_id:Long,
    val cupos:Int,
    val grupo_Id:Long,
)

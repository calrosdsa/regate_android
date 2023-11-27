package app.regate.data.dto.empresa.salas

import app.regate.data.dto.account.user.ProfileDto
import kotlinx.serialization.Serializable

@Serializable
data class CompleteSalaRequest(
    val id:Int = 0,
    val sala_id:Long,
    val amount:Double
)

@Serializable
data class SalaCompleteDto(
    val id:Int,
    val sala_id: Long,
    val amount: Double,
    val profile:ProfileDto
)

@Serializable
data class SalaCompleteDetail(
    val history: List<SalaCompleteDto>,
    val precio:Double,
    val paid:Double,
    val rest:Double
)
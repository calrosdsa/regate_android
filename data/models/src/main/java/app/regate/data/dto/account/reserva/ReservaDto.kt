package app.regate.data.dto.account.reserva

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class ReservaDto(
    val created_at: Instant,
    val id: Long,
    val instalacion_id: Long,
    val establecimiento_id:Long,
    val instalacion_name: String = "",
    val paid: Double =0.0,
    val total_price: Double = 0.0,
    val start_date: Instant,
    val end_date:Instant,
    val profile_id: Long,
    val sala_id:Long? = null
)


package app.regate.data.dto.account.reserva

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class ReservaDto(
    val created_at: Instant,
    val id: Long,
    val instalacion_id: Int,
    val instalacion_name: String? = null,
    val paid: Int,
    val total_price:Int,
    val start_date: Instant,
    val end_date:Instant,
    val user_id: String
)


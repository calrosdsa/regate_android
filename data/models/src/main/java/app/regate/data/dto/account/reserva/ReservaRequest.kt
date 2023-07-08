package app.regate.data.dto.account.reserva

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class ReservaRequest(
   val cupos :List<CupoDto>,
   val total_price:Int,
   val paid:Int,
   val end_time:String,
   val establecimiento_id:Long
)

@Serializable
data class CupoDto(
    val instalacion_id:Long,
    val precio:Int,
    val start_date:String
)
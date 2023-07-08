package app.regate.data.dto.empresa.establecimiento

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class CupoInstaDto(
    val available: Boolean,
    val day: String,
    val price: Int,
    val reserva_id: Int? = null,
    val time: Instant,
    val instalacion_id: Long,
)

@Serializable
data class  CuposRequest(
    val start_date:String,
    val instalacion_id:Int
)
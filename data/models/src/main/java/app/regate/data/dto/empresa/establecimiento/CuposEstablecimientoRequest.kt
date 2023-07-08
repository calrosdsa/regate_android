package app.regate.data.dto.empresa.establecimiento

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class CuposEstablecimientoRequest(
    val cupos: Int,
    val establecimiento_id: Long,
    val minutes: Long,
    val day:Int,
    val date:LocalDate
)
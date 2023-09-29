package app.regate.data.dto.empresa.establecimiento

import app.regate.data.dto.empresa.salas.SalaDto
import kotlinx.serialization.Serializable

@Serializable
data class InitialData(
    val establecimientos:List<EstablecimientoDto>,
    val recommended:List<EstablecimientoDto>? = null,
    val near:List<EstablecimientoDto>? = null,
    val salas:List<SalaDto>
    )

@Serializable
data class InitialDataFilter(
    val categories:List<Long> = emptyList(),
    val lng:String? = null,
    val lat:String? = null,
    val isInit:Boolean = false
)
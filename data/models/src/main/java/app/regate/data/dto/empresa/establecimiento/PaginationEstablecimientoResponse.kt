package app.regate.data.dto.empresa.establecimiento

import kotlinx.serialization.Serializable

@Serializable
data class PaginationEstablecimientoResponse(
    val page:Int,
    val results:List<EstablecimientoDto>
)

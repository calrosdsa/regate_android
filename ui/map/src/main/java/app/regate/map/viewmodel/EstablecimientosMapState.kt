package app.regate.map.viewmodel

import app.regate.data.auth.AppAuthState
import app.regate.data.dto.empresa.establecimiento.EstablecimientoDto

data class EstablecimientosMapState(
    val loading:Boolean = false,
    val authState: AppAuthState? = null,
    val establecimientos:List<EstablecimientoDto> = emptyList(),
) {

    companion object {
        val Empty = EstablecimientosMapState()
    }
}

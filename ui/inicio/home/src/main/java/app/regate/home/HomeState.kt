package app.regate.home

import androidx.compose.runtime.Immutable
import app.regate.data.auth.AppAuthState
import app.regate.data.dto.empresa.establecimiento.EstablecimientoDto
import app.regate.data.dto.empresa.salas.SalaDto

@Immutable
data class HomeState (
    val loading:Boolean = false,
    val authState:AppAuthState? = null,
    val establecimientos:List<EstablecimientoDto> = emptyList(),
    val recommended :List<EstablecimientoDto> = emptyList(),
    val nearEstablecimientos:List<EstablecimientoDto> = emptyList(),
    val salas:List<SalaDto> = emptyList()
    ){

    companion object {
        val Empty = HomeState()
    }
}
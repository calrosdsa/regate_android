package app.regate.home

import androidx.compose.runtime.Immutable
import app.regate.data.auth.AppAuthState
import app.regate.data.dto.empresa.establecimiento.EstablecimientoDto

@Immutable
data class HomeState (
    val loading:Boolean = false,
    val authState:AppAuthState? = null,
    val establecimientos:List<EstablecimientoDto> = emptyList(),
    ){

    companion object {
        val Empty = HomeState()
    }
}
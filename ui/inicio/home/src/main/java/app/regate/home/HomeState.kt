package app.regate.home

import androidx.compose.runtime.Immutable
import app.regate.data.auth.AppAuthState
import app.regate.data.common.AddressDevice
import app.regate.data.dto.empresa.establecimiento.EstablecimientoDto
import app.regate.data.dto.empresa.establecimiento.InitialData
import app.regate.data.dto.empresa.salas.SalaDto

@Immutable
data class HomeState (
    val loading:Boolean = false,
    val authState:AppAuthState? = null,
    val data:InitialData? = null,
    val addressDevice:AddressDevice? = null
    ){

    companion object {
        val Empty = HomeState()
    }
}
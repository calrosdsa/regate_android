package app.regate.bottom.reserva

import androidx.compose.runtime.Immutable
import app.regate.api.UiMessage
import app.regate.data.auth.AppAuthState
import app.regate.models.Cupo
import app.regate.models.Establecimiento
import app.regate.models.Instalacion
import app.regate.models.Setting

@Immutable
data class BottomReservaState(
    val loading:Boolean = false,
    val message:UiMessage? = null,
    val setting: Setting? = null,
    val cupos:List<Cupo> = emptyList(),
    val isToken:Boolean =false,
    val authState:AppAuthState? = null,
    val totalPrice:Int? = null,
    val establecimiento: Establecimiento? = null,
    val instalacion:Instalacion? = null
){
    companion object{
        val Empty = BottomReservaState()
    }
}
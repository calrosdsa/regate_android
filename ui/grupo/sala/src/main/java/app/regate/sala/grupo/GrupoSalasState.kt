package app.regate.sala.grupo

import app.regate.api.UiMessage
import app.regate.data.auth.AppAuthState
import app.regate.data.dto.empresa.salas.SalaFilterData

data class GrupoSalasState(
    val loading:Boolean = false,
    val message:UiMessage? = null,
    val authState:AppAuthState = AppAuthState.LOGGED_OUT,
){
    companion object{
         val Empty = GrupoSalasState()
    }
}

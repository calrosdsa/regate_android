package app.regate.usersalas

import app.regate.api.UiMessage
import app.regate.data.auth.AppAuthState
import app.regate.data.dto.empresa.salas.SalaFilterData

data class UserSalasState(
    val loading:Boolean = false,
    val message:UiMessage? = null,
    val authState:AppAuthState = AppAuthState.LOGGED_OUT,
    val filterData: SalaFilterData = SalaFilterData()
){
    companion object{
         val Empty = UserSalasState()
    }
}

package app.regate.grupos.user_requests

import androidx.compose.runtime.Immutable
import app.regate.data.auth.AppAuthState
import app.regate.models.User

@Immutable
data class UserGrupoRequestsState(
    val loading:Boolean = false,
    val appAuthState: AppAuthState = AppAuthState.LOGGED_OUT,
    val user:User? = null,
    val confirmRequetsIds:List<Int> = mutableListOf(),
    val declineRequestIds:List<Int> = mutableListOf()
){
    companion object {
        val Empty = UserGrupoRequestsState()
    }
}

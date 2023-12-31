package app.regate.grupos

import androidx.compose.runtime.Immutable
import app.regate.api.UiMessage
import app.regate.data.auth.AppAuthState
import app.regate.models.grupo.MyGroups

@Immutable
data class GruposState(
    val loading:Boolean = false,
    val message:UiMessage? = null,
    val userGroups:List<MyGroups> = emptyList(),
    val authState: AppAuthState = AppAuthState.LOGGED_OUT
){
    companion object{
        val Empty = GruposState()
    }
}

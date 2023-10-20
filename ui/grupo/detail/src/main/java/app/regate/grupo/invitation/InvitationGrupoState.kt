package app.regate.grupo.invitation

import androidx.compose.runtime.Immutable
import app.regate.data.auth.AppAuthState
import app.regate.data.dto.empresa.grupo.GrupoDto
import app.regate.models.MyGroups
import app.regate.models.account.User

@Immutable
data class InvitationGrupoState(
    val loading:Boolean = false,
    val grupo:GrupoDto? = null,
    val myGroup:MyGroups? = null,
    val user: User?= null,
    val authState: AppAuthState = AppAuthState.LOGGED_OUT
){
    companion object{
        val Empty = InvitationGrupoState()
    }
}

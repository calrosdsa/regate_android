package app.regate.grupo.info

import androidx.compose.runtime.Immutable
import app.regate.data.auth.AppAuthState
import app.regate.data.dto.empresa.grupo.GrupoDto
import app.regate.grupo.invitation.InvitationGrupoState
import app.regate.models.grupo.MyGroups
import app.regate.models.account.User

@Immutable
data class InfoGrupoState(
    val loading:Boolean = false,
    val grupo:GrupoDto? = null,
    val myGroup: MyGroups? = null,
    val user: User?= null,
    val authState: AppAuthState = AppAuthState.LOGGED_OUT
){
    companion object{
        val Empty = InvitationGrupoState()
    }
}

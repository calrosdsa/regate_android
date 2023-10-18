package app.regate.grupo.invitationlink

import androidx.compose.runtime.Immutable
import app.regate.api.UiMessage
import app.regate.data.auth.AppAuthState
import app.regate.data.dto.empresa.grupo.GrupoDto
import app.regate.data.dto.empresa.grupo.setting.GrupoInvitationLinkDto
import app.regate.models.MyGroups
import app.regate.models.User

@Immutable
data class InvitationLinkState(
    val loading:Boolean = false,
    val message:UiMessage?=null,
    val user:User?= null,
    val invitationLink: GrupoInvitationLinkDto?= null,
    val authState: AppAuthState = AppAuthState.LOGGED_OUT
){
    companion object{
        val Empty = InvitationLinkState()
    }
}

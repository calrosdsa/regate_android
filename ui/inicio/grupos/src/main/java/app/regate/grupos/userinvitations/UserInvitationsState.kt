package app.regate.grupos.userinvitations

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import app.regate.api.UiMessage

@Immutable
data class UserInvitationsState(
    val loading:Boolean= false,
    val message:UiMessage? = null
){
    companion object{
        val Empty = UserInvitationsState()
    }
}

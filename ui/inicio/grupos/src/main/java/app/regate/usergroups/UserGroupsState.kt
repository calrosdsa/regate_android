package app.regate.usergroups

import app.regate.api.UiMessage
import app.regate.compoundmodels.GrupoWithMessage
import app.regate.data.auth.AppAuthState
import app.regate.models.Grupo

data class UserGroupsState(
    val loading:Boolean = false,
    val message:UiMessage? = null,
    val grupos:List<GrupoWithMessage> = emptyList(),
    val authState:AppAuthState = AppAuthState.LOGGED_OUT
){
    companion object{
        val Empty = UserGroupsState()
    }
}

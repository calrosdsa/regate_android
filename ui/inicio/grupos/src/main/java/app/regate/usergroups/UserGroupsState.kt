package app.regate.usergroups

import app.regate.api.UiMessage
import app.regate.models.Grupo

data class UserGroupsState(
    val loading:Boolean = false,
    val message:UiMessage? = null,
    val grupos:List<Grupo> = emptyList()

){
    companion object{
        val Empty = UserGroupsState()
    }
}

package app.regate.usergroups.mygroups

import androidx.compose.runtime.Immutable
import app.regate.api.UiMessage
import app.regate.models.Grupo
import app.regate.models.User

@Immutable
data class MyGroupsState(
    val loading:Boolean = false,
    val message:UiMessage? = null,
    val grupos:List<Grupo> = emptyList(),
    val user:User?= null,
    val selectedGroups:List<Grupo> = emptyList()

){
    companion object{
        val Empty = MyGroupsState()
    }
}

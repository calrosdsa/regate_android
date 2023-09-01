package app.regate.usergroups.mygroups

import androidx.compose.runtime.Immutable
import app.regate.api.UiMessage
import app.regate.models.Grupo

@Immutable
data class MyGroupsState(
    val loading:Boolean = false,
    val message:UiMessage? = null,
    val grupos:List<Grupo> = emptyList()

){
    companion object{
        val Empty = MyGroupsState()
    }
}

package app.regate.profile

import app.regate.api.UiMessage
import app.regate.models.Labels
import app.regate.models.Profile
import app.regate.models.account.User
import app.regate.models.grupo.Grupo

data class ProfileState(
    val loading:Boolean = false,
    val message:UiMessage? = null,
    val profile:Profile? = null,
    val user: User? = null,
    val categories:List<Labels> = emptyList(),
    val grupos:List<Grupo> = emptyList()
){
    companion object{
        val Empty = ProfileState()
    }
}
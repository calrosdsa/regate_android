package app.regate.profile

import app.regate.api.UiMessage
import app.regate.models.Profile
import app.regate.models.account.User

data class ProfileState(
    val loading:Boolean = false,
    val message:UiMessage? = null,
    val profile:Profile? = null,
    val user: User? = null
){
    companion object{
        val Empty = ProfileState()
    }
}
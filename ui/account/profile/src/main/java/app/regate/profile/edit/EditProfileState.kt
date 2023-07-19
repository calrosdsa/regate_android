package app.regate.profile.edit

import app.regate.api.UiMessage
import app.regate.models.Profile

data class EditProfileState(
    val loading:Boolean = false,
    val message:UiMessage? = null,
    val profile:Profile? = null
){
    companion object {
        val Empty = EditProfileState()
    }
}

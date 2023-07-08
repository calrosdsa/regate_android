package app.regate.settings

import app.regate.api.UiMessage
import app.regate.models.User

data class SettingState(
    val loading:Boolean = false,
    val message:UiMessage? = null,
    val user:User? = null
){
    companion object{
        val Empty = SettingState()
    }
}

package app.regate.media

import app.regate.api.UiMessage
import app.regate.data.auth.AppAuthState
import app.regate.models.User
import app.regate.settings.AppPreferences

data class SettingState(
    val loading:Boolean = false,
    val message:UiMessage? = null,
    val user:User? = null,
    val authState: AppAuthState? = null,
    val theme: AppPreferences.Theme = AppPreferences.Theme.SYSTEM,
    val useDynamicColors:Boolean = false
){
    companion object{
        val Empty = SettingState()
    }
}

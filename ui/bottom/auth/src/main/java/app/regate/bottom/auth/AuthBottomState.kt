package app.regate.bottom.auth

import androidx.compose.runtime.Immutable
import app.regate.api.UiMessage

@Immutable
data class AuthBottomState(
    val loading:Boolean = false,
    val message:UiMessage? = null,
){
    companion object{
        val Empty = AuthBottomState()
    }
}
package app.regate.auth.signin

import androidx.compose.runtime.Immutable
import app.regate.api.UiMessage

@Immutable
data class AuthState(
    val loading:Boolean = false,
    val genero: Genero? = null,
    val birthDay:String = "",
    val message:UiMessage? = null
){
    companion object {
        val Empty = AuthState()
    }
}
enum class Genero { MALE,FAMELE }


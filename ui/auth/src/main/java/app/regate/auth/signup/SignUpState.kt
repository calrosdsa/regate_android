package app.regate.auth.signup

import androidx.compose.runtime.Immutable
import app.regate.api.UiMessage

@Immutable
data class SignUpState(
    val genero: Genero? = null,
    val birthDay:String = "",
    val loading:Boolean = false,
    val message:UiMessage? = null,
){
    companion object {
        val Empty = SignUpState()
    }
}
enum class Genero { MALE,FAMELE }


package app.regate.auth.signup

import androidx.compose.runtime.Immutable

@Immutable
data class SignUpState(
    val genero: Genero? = null,
    val birthDay:String = ""
){
    companion object {
        val Empty = SignUpState()
    }
}
enum class Genero { MALE,FAMELE }


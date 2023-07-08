package app.regate.auth

import androidx.compose.runtime.Immutable

@Immutable
data class AuthState(
    val loading:Boolean = false,
    val genero:Genero? = null,
    val birthDay:String = ""
){
    companion object {
        val Empty = AuthState()
    }
}
enum class Genero { MALE,FAMELE }


package app.regate.sala

import app.regate.api.UiMessage
import app.regate.data.auth.AppAuthState
import app.regate.data.dto.account.user.ProfileDto
import app.regate.data.dto.empresa.salas.SalaDto
import app.regate.models.Instalacion
import app.regate.models.User

data class SalaState(
    val loading:Boolean = false,
    val message:UiMessage? = null,
    val user:User? = null,
    val sala: SalaDto? = null,
    val instalacion: Instalacion? = null,
    val authState:AppAuthState? = null,
    val profiles:List<ProfileDto> = emptyList()
){
    companion object{
        val Empty = SalaState()
    }
}

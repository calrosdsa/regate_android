package app.regate.sala

import app.regate.api.UiMessage
import app.regate.compoundmodels.UserProfileGrupoAndSalaDto
import app.regate.data.auth.AppAuthState
import app.regate.data.dto.empresa.salas.SalaDetail
import app.regate.models.account.User

data class SalaState(
    val loading:Boolean = false,
    val message:UiMessage? = null,
    val user: User? = null,
//    val sala: SalaDto? = null,
//    val instalacion: Instalacion? = null,
    val authState:AppAuthState? = null,
    val data:SalaDetail? = null,
    val users:List<UserProfileGrupoAndSalaDto> = emptyList(),
//    val profiles:List<ProfileDto> = emptyList()
){
    companion object{
        val Empty = SalaState()
    }
}

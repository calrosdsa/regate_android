package app.regate.grupo

import app.regate.api.UiMessage
import app.regate.compoundmodels.UserProfileGrupo
import app.regate.data.auth.AppAuthState
import app.regate.data.dto.account.user.ProfileDto
import app.regate.data.dto.empresa.salas.SalaDto
import app.regate.models.Grupo
import app.regate.models.Instalacion
import app.regate.models.User

data class GrupoState(
    val loading:Boolean = false,
    val message:UiMessage? = null,
    val user:User? = null,
    val authState:AppAuthState? = null,
    val usersProfileGrupo: List<UserProfileGrupo> = emptyList(),
    val grupo:Grupo? = null,
    val profiles:List<ProfileDto> = emptyList(),
    val salas:List<SalaDto> = emptyList()
){
    companion object{
        val Empty = GrupoState()
    }
}

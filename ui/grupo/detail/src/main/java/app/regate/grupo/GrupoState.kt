package app.regate.grupo

import app.regate.api.UiMessage
import app.regate.compoundmodels.UserProfileGrupoAndSalaDto
import app.regate.data.auth.AppAuthState
import app.regate.data.dto.empresa.salas.SalaDto
import app.regate.models.grupo.Grupo
import app.regate.models.account.User

data class GrupoState(
    val loading:Boolean = true,
    val message:UiMessage? = null,
    val user: User? = null,
    val authState:AppAuthState? = null,
    val usersProfileGrupo: List<UserProfileGrupoAndSalaDto> = emptyList(),
    val grupo: Grupo? = null,
    val salas:List<SalaDto> = emptyList(),
    val currentUser:UserProfileGrupoAndSalaDto? = null,
    val selectedUser:UserProfileGrupoAndSalaDto? = null,
){
    companion object{
        val Empty = GrupoState(
            loading = true
        )
    }
}

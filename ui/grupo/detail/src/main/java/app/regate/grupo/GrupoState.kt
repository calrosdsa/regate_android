package app.regate.grupo

import app.regate.api.UiMessage
import app.regate.compoundmodels.UserProfileGrupoAndSala
import app.regate.data.auth.AppAuthState
import app.regate.data.dto.empresa.salas.SalaDto
import app.regate.models.Grupo
import app.regate.models.account.User

data class GrupoState(
    val loading:Boolean = true,
    val message:UiMessage? = null,
    val user: User? = null,
    val authState:AppAuthState? = null,
    val usersProfileGrupo: List<UserProfileGrupoAndSala> = emptyList(),
    val grupo:Grupo? = null,
    val salas:List<SalaDto> = emptyList(),
    val currentUser:UserProfileGrupoAndSala? = null,
    val selectedUser:UserProfileGrupoAndSala? = null,
){
    companion object{
        val Empty = GrupoState(
            loading = true
        )
    }
}

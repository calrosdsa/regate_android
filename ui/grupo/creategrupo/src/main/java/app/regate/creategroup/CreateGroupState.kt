package app.regate.creategroup

import app.regate.api.UiMessage
import app.regate.compoundmodels.InstalacionCupos
import app.regate.data.auth.AppAuthState
import app.regate.data.dto.empresa.salas.SalaRequestDto
import app.regate.models.grupo.Grupo
import app.regate.models.account.User

data class CreateGroupState (
    val loading:Boolean = false,
    val message:UiMessage? = null,
    val user: User? = null,
    val authState:AppAuthState? = null,
    val instalacionCupos:InstalacionCupos? = null,
    val salaData:SalaRequestDto = SalaRequestDto(),
    val group: Grupo? = null,
//    val cupos:List<Cupo> = emptyList()
){
    companion object{
        val Empty = CreateGroupState()
    }
}




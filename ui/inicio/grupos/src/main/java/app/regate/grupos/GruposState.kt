package app.regate.grupos

import androidx.compose.runtime.Immutable
import app.regate.api.UiMessage
import app.regate.data.dto.empresa.grupo.GrupoDto
import app.regate.models.Grupo
import app.regate.models.MyGroups

@Immutable
data class GruposState(
    val loading:Boolean = false,
    val message:UiMessage? = null,
    val userGroups:List<MyGroups> = emptyList(),
){
    companion object{
        val Empty = GruposState()
    }
}

package app.regate.grupos

import androidx.compose.runtime.Immutable
import app.regate.api.UiMessage
import app.regate.data.dto.empresa.grupo.GrupoDto
import app.regate.models.Grupo

@Immutable
data class GruposState(
    val loading:Boolean = false,
    val message:UiMessage? = null,
    val grupos:List<GrupoDto> = emptyList()
){
    companion object{
        val Empty = GruposState()
    }
}

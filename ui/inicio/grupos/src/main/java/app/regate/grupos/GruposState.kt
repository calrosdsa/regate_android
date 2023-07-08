package app.regate.grupos

import androidx.compose.runtime.Immutable
import app.regate.api.UiMessage
import app.regate.models.Grupo

@Immutable
data class GruposState(
    val loading:Boolean = false,
    val message:UiMessage? = null,
    val grupos:List<Grupo> = emptyList()
){
    companion object{
        val Empty = GruposState()
    }
}

package app.regate.instalacion

import androidx.compose.runtime.Immutable
import app.regate.api.UiMessage
import app.regate.data.dto.empresa.establecimiento.CupoInstaDto
import app.regate.models.Instalacion
@Immutable
data class InstalacionState(
    val loading:Boolean = false,
    val instalacion:Instalacion? = null,
    val message:UiMessage? = null,
){
    companion object{
        val Empty = InstalacionState()
    }
}
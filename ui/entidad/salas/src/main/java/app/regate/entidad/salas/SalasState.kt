package app.regate.entidad.salas

import androidx.compose.runtime.Immutable
import app.regate.api.UiMessage
import app.regate.data.dto.empresa.establecimiento.CupoEstablecimiento
import app.regate.data.dto.empresa.salas.SalaDto
import app.regate.models.Instalacion

@Immutable
data class SalasState(
    val loading:Boolean = false,
   val message:UiMessage? = null,
    val salas:List<SalaDto> = emptyList()
){
    companion object{
        val Empty = SalasState()
    }
}


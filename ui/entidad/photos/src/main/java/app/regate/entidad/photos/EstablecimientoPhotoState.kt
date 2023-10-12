package app.regate.entidad.photos

import androidx.compose.runtime.Immutable
import app.regate.api.UiMessage
import app.regate.data.dto.empresa.establecimiento.PhotoDto
import app.regate.models.Instalacion
@Immutable
data class EstablecimientoPhotoState(
    val loading:Boolean = false,
    val message:UiMessage? = null,
    val photos:List<PhotoDto> = emptyList()
){
    companion object{
        val Empty = EstablecimientoPhotoState()
    }
}
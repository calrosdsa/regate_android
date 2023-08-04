package app.regate.createsala.establecimiento

import app.regate.api.UiMessage
import app.regate.data.dto.empresa.establecimiento.EstablecimientoDto
import app.regate.data.dto.empresa.establecimiento.InitialDataFilter
import app.regate.models.Establecimiento

data class EstablecimientoFilterState(
    val loading:Boolean = false,
    val message:UiMessage? = null,
    val establecimientosFav:List<Establecimiento> = emptyList(),
    val filterData:InitialDataFilter = InitialDataFilter(),
){
    companion object{
        val Empty = EstablecimientoFilterState()
    }
}

package app.regate.create.sala.establecimiento

import app.regate.api.UiMessage
import app.regate.data.dto.empresa.establecimiento.InitialDataFilter
import app.regate.models.establecimiento.Establecimiento

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

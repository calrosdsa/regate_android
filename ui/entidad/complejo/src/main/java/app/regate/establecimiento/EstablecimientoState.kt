package app.regate.establecimiento

import androidx.compose.runtime.Immutable
import app.regate.api.UiMessage
import app.regate.compoundmodels.InstalacionCategoryCount
import app.regate.data.dto.empresa.salas.SalaDto
import app.regate.models.Establecimiento
import app.regate.models.Instalacion
import app.regate.models.Labels

@Immutable
data class EstablecimientoState(
    val loading:Boolean = false,
    val establecimiento: Establecimiento? = null,
    val message:UiMessage? = null,
    val instalacionCategoryCount: List<InstalacionCategoryCount> = emptyList(),
    val rules:List<Labels> = emptyList(),
    val amenities:List<Labels> = emptyList()
){
    companion object{
        val Empty = EstablecimientoState()
    }
}


data class Amenity(
    val title:String
)

data class CupoHorario(
    val initial:String,
    val end:String
){
    companion object {

        val horarios = listOf<CupoHorario>(
            CupoHorario(initial = "09:00:00",end ="10:00:00"),
            CupoHorario(initial = "10:00:00",end ="11:00:00"),
            CupoHorario(initial = "11:00:00",end ="12:00:00"),
            CupoHorario(initial = "12:00:00",end ="13:00:00"),
            CupoHorario(initial = "15:00:00",end ="16:00:00"),
            CupoHorario(initial = "17:00:00",end ="18:00:00"),
            CupoHorario(initial = "18:00:00",end ="19:00:00"),
            CupoHorario(initial = "19:00:00",end ="20:00:00")
        )

    }
}


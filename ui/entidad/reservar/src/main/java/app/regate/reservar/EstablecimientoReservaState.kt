package app.regate.reservar

import androidx.compose.runtime.Immutable
import app.regate.compoundmodels.InstalacionCategoryCount
import app.regate.data.common.HoraIntervalo
import app.regate.data.dto.empresa.establecimiento.CupoEstablecimiento
import app.regate.data.dto.empresa.instalacion.InstalacionAvailable
import app.regate.models.Instalacion
import app.regate.models.Labels
import app.regate.models.Setting
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toKotlinTimeZone
import kotlinx.datetime.toLocalDateTime
import java.time.ZoneId

@Immutable
data class EstablecimientoReservaState(
    val loading:Boolean = false,
    val instalaciones:List<Instalacion> = emptyList(),
    val instalacionesAvailables:List<InstalacionAvailable> = emptyList(),
    val establecimientoCupos:List<CupoEstablecimiento> = emptyList(),
    val setting:Setting? = null,
    val horaIntervalo:List<HoraIntervalo> = listOf(HoraIntervalo(text="30 minutos", value = 30),
        HoraIntervalo(text="1 hora", value = 60) ,
        HoraIntervalo(text="1:30 hora", value = 90),
        HoraIntervalo(text="2 horas", value = 120),
        HoraIntervalo(text="3 horas", value = 180),
        ),
    val filter:Filter = Filter.Default,
    val selectedTime:Instant? = null,
    val categories:List<InstalacionCategoryCount> = emptyList(),
    val selectedCategory:InstalacionCategoryCount? = null
){
    companion object{
        val Empty = EstablecimientoReservaState()
    }
}



data class Filter(
    val minutes: Long,
    val currentDate:LocalDateTime,
    val category_id:Long,
) {
    companion object {
        val Default = Filter(
            minutes = 60,
            currentDate = Clock.System.now().toLocalDateTime(
                ZoneId.systemDefault().toKotlinTimeZone()
            ),
            category_id = 0
        )
    }
}

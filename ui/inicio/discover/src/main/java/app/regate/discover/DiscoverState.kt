package app.regate.discover

import androidx.compose.runtime.Immutable
import app.regate.api.UiMessage
import app.regate.data.common.AddressDevice
import app.regate.data.common.HoraIntervalo
import app.regate.data.dto.account.reserva.ReservaDto
import app.regate.data.dto.empresa.instalacion.FilterInstalacionData
import app.regate.data.dto.empresa.instalacion.InstalacionDto
import app.regate.models.Labels
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toKotlinTimeZone
import kotlinx.datetime.toLocalDateTime
import java.time.ZoneId

@Immutable
data class DiscoverState(
    val loading:Boolean = false,
    val message:UiMessage? = null,
    val reservas: List<ReservaDto> = emptyList(),
    val filter:FilterInstalacionData = FilterInstalacionData(),
    val addressDevice: AddressDevice? = null,
    val horaIntervalo:List<HoraIntervalo> = listOf(
        HoraIntervalo(text="30 minutos", value = 30),
        HoraIntervalo(text="1 hora", value = 60) ,
        HoraIntervalo(text="1:30 hora", value = 90),
        HoraIntervalo(text="2 horas", value = 120),
        HoraIntervalo(text="3 horas", value = 180),
    ),
    val categories:List<Labels> = emptyList(),
    val results:List<InstalacionDto> = emptyList(),
    val selectedCategory:Labels?= null
){
    companion object {
        val Empty = DiscoverState()
    }
}


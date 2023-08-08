package app.regate.data.dto.account.reserva

import app.regate.data.dto.empresa.establecimiento.EstablecimientoDto
import app.regate.data.dto.empresa.instalacion.InstalacionDto
import app.regate.models.Establecimiento
import kotlinx.serialization.Serializable

@Serializable
data class ReservaDetail(
    val reserva:ReservaDto,
    val establecimiento:EstablecimientoDto,
    val instalacion:InstalacionDto
)

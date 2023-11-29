package app.regate.data.dto.empresa.salas

import app.regate.data.dto.empresa.instalacion.InstalacionDto
import kotlinx.serialization.Serializable

@Serializable
data class SalaDetail(
    val sala:SalaDto,
    val instalacion:InstalacionDto
)

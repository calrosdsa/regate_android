package app.regate.data.dto.empresa.establecimiento

import kotlinx.serialization.Serializable

@Serializable
data class EstablecimientoDetailDto(
    val establecimiento:EstablecimientoDto,
    val setting_establecimiento: SettingEstablecimientoDto,
)

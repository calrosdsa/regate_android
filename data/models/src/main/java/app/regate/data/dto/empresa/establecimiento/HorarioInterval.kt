package app.regate.data.dto.empresa.establecimiento

import kotlinx.serialization.Serializable

@Serializable
data class HorarioInterval(
    val id: Int,
    val minutes: Long,
)
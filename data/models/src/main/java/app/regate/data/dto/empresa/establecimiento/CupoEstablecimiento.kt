package app.regate.data.dto.empresa.establecimiento

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class CupoEstablecimiento(
    val cupos: Int,
    val end_time: Instant,
    val start_time: Instant,
    val enabled:Boolean
)
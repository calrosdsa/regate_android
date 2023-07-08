package app.regate.data.dto.empresa.instalacion

import kotlinx.serialization.Serializable


@Serializable
data class InstalacionesAvailables(
    val instalaciones: List<InstalacionAvailable>,
    val request:InstalacionRequest
)


@Serializable
data class InstalacionAvailable(
    val instalacion_id: Long,
    val precio: Int,
    val portada:String,
    val name:String
)
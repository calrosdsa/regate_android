package app.regate.data.dto.empresa.instalacion

import kotlinx.serialization.Serializable


@Serializable
data class InstalacionRequest(
    val date_time: List<String>,
    val day: Int,
    val establecimiento_id: Long,
    val time: List<String>,
    val category_id:Long,
)
package app.regate.data.dto.empresa.instalacion

import kotlinx.serialization.Serializable

@Serializable
data class InstalacionDto(
    val id: Long,
    val name: String,
    val description: String? = null,
    val precio_hora: Int? = null,
    val cantidad_personas: String? = null,
    val establecimiento_id: Long,
    val category_id: Int?=null,
    val category_name: String? = null,
    val portada:String? = null,
    val amenities:List<Long> = emptyList(),
    val distance:Double? = null
)




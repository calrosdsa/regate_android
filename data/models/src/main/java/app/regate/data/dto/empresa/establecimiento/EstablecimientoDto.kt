package app.regate.data.dto.empresa.establecimiento

import kotlinx.serialization.Serializable

@Serializable
data class EstablecimientoDto(
    val address: String? = null,
    val created_at: String? = null,
    val email: String? = null,
    val empresa_id: Int? = null,
    val id: Int,
    val latitud: String? = null,
    val longitud: String? = null,
    val name: String,
    val description:String? = null,
    val is_open:Boolean? = null,
    val phone_number: String? = null,
    val photo: String,
    val portada: String,
    val amenities:List<Long> = emptyList(),
    val rules:List<Long> = emptyList()
)
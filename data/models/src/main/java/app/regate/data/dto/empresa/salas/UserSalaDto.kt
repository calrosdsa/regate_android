package app.regate.data.dto.empresa.salas

import kotlinx.serialization.Serializable

@Serializable
data class UserSalaDto(
    val profile_id:Long,
    val nombre:String,
    val apellido:String? = null,
    val profile_photo:String? = null,
    val id:Long = 0,
)

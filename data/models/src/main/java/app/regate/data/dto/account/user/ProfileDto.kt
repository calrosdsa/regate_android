package app.regate.data.dto.account.user

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto(
    val created_at: Instant,
    val email: String,
    val nombre: String,
    val apellido: String? = null,
    val profile_id: Long,
    val profile_photo: String?= null,
    val user_id: Long,
)
package app.regate.data.dto.account.user

import app.regate.data.dto.empresa.grupo.GrupoDto
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto(
    val uuid:String ="",
    val created_at: Instant?=null,
    val email: String = "",
    val nombre: String,
    val apellido: String? = null,
    val profile_id: Long,
    val profile_photo: String?= null,
    val user_id: Long = 0,
)


@Serializable
data class PaginationProfilesResponse(
    val results:List<ProfileDto>,
    val page:Int
)


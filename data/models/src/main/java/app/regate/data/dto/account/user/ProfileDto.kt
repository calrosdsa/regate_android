package app.regate.data.dto.account.user

import app.regate.data.dto.empresa.grupo.GrupoDto
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDetailDto(
    val profile:ProfileDto,
    val categories:List<Int> = emptyList(),
    val establecimientos:List<EstablecimientoItemDto> = emptyList(),
)

@Serializable
data class  EstablecimientoItemDto(
    val id:Long,
    val name:String,
    val photo:String? = null,
)
@Serializable
data class ProfileCategoryRequest(
    val category_id:Int,
    val should_delete:Boolean,
    val should_insert:Boolean,
)

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


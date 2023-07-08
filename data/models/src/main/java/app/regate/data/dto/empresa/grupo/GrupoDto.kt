package app.regate.data.dto.empresa.grupo

import app.regate.data.dto.account.user.ProfileDto
import app.regate.data.dto.empresa.salas.SalaDto
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class GrupoDto(
    val created_at: Instant? = null,
    val descripcion: String? = null,
    val id: Long,
    val name: String,
    val photo: String? = null
)

@Serializable
data class GrupoResponse(
    val grupo: GrupoDto,
    val profiles:List<ProfileDto>,
    val salas:List<SalaDto>
)

@Serializable
data class AddUserGrupoRequest(
    val grupo_id:Long,
    val profile_id:Long
)

@Serializable
data class FilterGrupoData(
    val category_id:Long
)
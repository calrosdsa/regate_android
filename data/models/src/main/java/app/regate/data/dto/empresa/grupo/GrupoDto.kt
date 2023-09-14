package app.regate.data.dto.empresa.grupo

import app.regate.data.dto.empresa.salas.SalaDto
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class GrupoDto(
    val id: Long,
    val descripcion: String? = null,
    val created_at: Instant? = null,
    val name: String,
    val photo: String? = null,
    val visibility:Int = 0,
    val profile_id:Long = 0,
    val last_message:String = "",
    val last_message_created: Instant = Clock.System.now(),
    val messages_count:Int = 0,
)

@Serializable
data class PaginationGroupsResponse(
    val results:List<GrupoDto>,
    val page:Int
)

@Serializable
data class GrupoResponse(
    val grupo: GrupoDto,
    val profiles:List<UserGrupoDto>,
    val salas:List<SalaDto>
)

@Serializable
data class UserGrupoDto(
    val nombre: String,
    val apellido: String? = null,
    val profile_id: Long,
    val profile_photo: String?= null,
    val is_admin:Boolean,
    //Only for user grupo table
    val id:Long = 0
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
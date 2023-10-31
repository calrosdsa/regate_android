package app.regate.data.dto.empresa.grupo

import app.regate.data.dto.empresa.salas.SalaDto
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class GrupoDto(
    val id: Long = 0,
    val uuid:String = "",
    val descripcion: String? = null,
    val created_at: Instant? = null,
    val name: String = "Grupo",
    val photo: String? = null,
    val visibility:Int = GrupoVisibility.PUBLIC.ordinal,
    val is_visible:Boolean = false,
    val profile_id:Long = 0,
    val last_message:String = "",
    val last_message_created: Instant? = null,
    val messages_count:Int = 0,
    val members:Int = 0,
    val isMyGroup:Boolean = false,
    val grupo_request_estado: Int = GrupoRequestEstado.NONE.ordinal
)

enum class GrupoVisibility {
    PUBLIC,
    PRIVATE;
    companion object {
        fun fromInt(value: Int) = GrupoVisibility.values().first { it.ordinal == value }
    }
}

enum class GrupoRequestEstado {
    NONE,
    JOINED,
    PENDING;
    companion object {
        fun fromInt(value: Int) = GrupoRequestEstado.values().first { it.ordinal == value }
    }
}

enum class UserGrupoRequesEstado {
    NONE,
    PENDING,
    ACCEPTED,
    DECLINED;
    companion object {
        fun fromInt(value: Int) = UserGrupoRequesEstado.values().first { it.ordinal == value }
    }
}

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
data class  UserGrupoDto(
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
package app.regate.data.dto.empresa.grupo

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class PendingRequest(
    val grupo_id:Long,
    val profile_id:Long
)

@Serializable
data class PendingRequestUserDto(
    val profile_id:Int = 0,
    val nombre:String = "",
    val apellido:String? = null,
    val profile_photo:String? = null,
    val created_at:Instant =  Clock.System.now(),
    val grupo_id: Int = 0
)

@Serializable
data class UserGrupoRequestDto(
    val profile_id:Int = 0,
    val grupo_name:String = "",
    val grupo_photo:String? = null,
    val created_at:Instant =  Clock.System.now(),
    val grupo_id: Int = 0,
    val estado:Int = 0,
)

@Serializable
data class PaginationPendingRequestUser(
    val page:Int,
    val results:List<PendingRequestUserDto> = emptyList()
)

@Serializable
data class  PaginationUserGrupoRequest(
    val page: Int,
    val results:List<UserGrupoRequestDto> = emptyList()
)

@Serializable
data class PendingRequestCount(
    val count:Int = 0
)

enum class GrupoPendingRequestEstado {
    NONE,
    PENDING,
    ACCEPTED,
    DECLINED,

}
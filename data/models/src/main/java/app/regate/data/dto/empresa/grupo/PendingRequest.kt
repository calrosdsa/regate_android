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
data class PendingRequestUser(
    val profile_id:Int = 0,
    val nombre:String = "",
    val apellido:String? = null,
    val profile_photo:String? = null,
    val created_at:Instant =  Clock.System.now(),
    val grupo_id: Int = 0
)

@Serializable
data class PaginationPendingRequestUser(
    val page:Int,
    val results:List<PendingRequestUser> = emptyList()
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
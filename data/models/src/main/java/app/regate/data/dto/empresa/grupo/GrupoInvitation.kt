import kotlinx.serialization.Serializable

@Serializable
data class GrupoInvitationRequest(
    val profile_id:Long,
    val grupo_id:Long
)

@Serializable
data class PaginationInvitationResponse(
    val page:Int,
    val results:List<UserInvitationDto>,
)
@Serializable
data class PaginationUserInvitationsResponse(
    val page:Int,
    val results:List<UserGroupInvitationDto>,
)



@Serializable
data class UserInvitationDto(
    val profile_id: Long,
    val nombre:String,
    val apellido:String? = null,
    val profile_photo:String? = null,
    val grupo_id: Long,
    val estado:Int
)
@Serializable
data class UserGroupInvitationDto(
    val grupo_id: Long,
    val profile_id:Long,
    val photo:String? = null,
    val name:String,
    val estado:Int
)

enum class InvitationEstado{
    NONE,
    PENDIENTE,
    ACEPTADO,
    DECLINADO,
}
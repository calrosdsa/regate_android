package app.regate.data.dto.empresa.grupo.setting

import kotlinx.serialization.Serializable

@Serializable
data class GrupoInvitationLinkDto(
    val grupo_id:Long,
    val id_link:String
)

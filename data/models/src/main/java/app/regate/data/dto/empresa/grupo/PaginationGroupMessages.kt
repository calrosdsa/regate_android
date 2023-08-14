package app.regate.data.dto.empresa.grupo

import kotlinx.serialization.Serializable

@Serializable
data class PaginationGroupMessages(
    val page:Int,
    val results:List<GrupoMessageDto>
)

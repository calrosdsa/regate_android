package app.regate.data.dto.empresa.grupo

import app.regate.data.dto.empresa.salas.SalaDto
import kotlinx.serialization.Serializable

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
data class JoinGrupoResponse(
    val chat_id:Long=0
)
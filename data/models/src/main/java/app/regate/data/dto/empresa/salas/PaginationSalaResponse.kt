package app.regate.data.dto.empresa.salas

import kotlinx.serialization.Serializable


@Serializable
data class PaginationSalaResponse(
    val results:List<SalaDto>,
    val page:Int
)

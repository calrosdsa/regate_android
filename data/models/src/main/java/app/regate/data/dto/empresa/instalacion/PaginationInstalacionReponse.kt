package app.regate.data.dto.empresa.instalacion

import kotlinx.serialization.Serializable


@Serializable
data class PaginationInstalacionReponse (
    val results:List<InstalacionDto>,
    val page:Int
        )
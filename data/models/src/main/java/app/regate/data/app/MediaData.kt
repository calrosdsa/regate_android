package app.regate.data.app

import kotlinx.serialization.Serializable

@Serializable
data class MediaData(
    val images:List<String> = emptyList()
)

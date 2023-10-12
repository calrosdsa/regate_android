package app.regate.data.dto.empresa.establecimiento

import kotlinx.serialization.Serializable

@Serializable
data class PhotoDto(
    val parent_id:Int=0,
    val url:String ="",
)

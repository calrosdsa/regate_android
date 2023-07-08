package app.regate.data.dto.empresa.labels

import kotlinx.serialization.Serializable

@Serializable
data class LabelDto(
    val id:Long,
    val name:String,
    val thumbnail:String? = null
)
package app.regate.data.dto.account

import kotlinx.serialization.Serializable

@Serializable
data class Me(
    val result:String,
    val isNull:String? = null
)

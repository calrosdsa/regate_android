package app.regate.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class SearchFilterRequest(
    val query:String = "",
    val latitud:Double = 0.0,
    val longitud:Double = 0.0,
)
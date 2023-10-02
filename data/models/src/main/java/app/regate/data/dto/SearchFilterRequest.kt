package app.regate.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class SearchFilterRequest(
    val query:String = "",
    val latitud:String = "0",
    val longitud:String = "0",
)
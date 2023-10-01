package app.regate.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class SearchFilterRequest(
    val query:String = "complejo:* | deportivo11:*",
    val latitud:String = "0",
    val longitud:String = "0",
)
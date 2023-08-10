package app.regate.data.dto.notifications

import kotlinx.serialization.Serializable


@Serializable
data class SalaConflict(
    val message:String,
    val id:Long
)
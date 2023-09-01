package app.regate.data.dto.notifications

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable


@Serializable
data class SalaConflictPayload(
    val id:Long,
    val horas:List<String>,
    val created_at:Instant,
    val message:String,
)
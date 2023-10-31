package app.regate.data.dto.empresa.salas

import kotlinx.serialization.Serializable

@Serializable
data class CreateSalaResponse(
    val id:Long = 0,
    val chat_id:Long =0,
    val message:String = ""
)

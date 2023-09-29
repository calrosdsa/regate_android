package app.regate.data.dto.empresa.grupo

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

enum class GrupoMessageType{
    MESSAGE,
    INSTALACION,
    SALA
}

@Serializable
data class MessageInstalacionPayload(
    val id:Int,
    val establecimiento_id:Int,
    val photo:String? = null,
    val total_price:Int = 0,
    val cupos:List<CupoInstalacion> = emptyList(),
    val name:String,
)

@Serializable
data class MessageSalaPayload(
    val id:Int = 0,
    val titulo:String = "",
    val cupos:Int = 0,
    val precio:Int =0,
    val precio_cupo:Double =0.0,
    val start:String = "",
    val end:String = ""
)

@Serializable
data class  CupoInstalacion(
    val time:Instant,
    val price:Double
)

@Serializable
data class GrupoMessageData(
    val type_data:Int,
    val content:String = "",
    val data:String,
)

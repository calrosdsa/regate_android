package app.regate.data.dto.empresa.grupo

import app.regate.data.dto.empresa.establecimiento.CupoInstaDto
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

enum class GrupoMessageType{
    MESSAGE,
    INSTALACION
}

@Serializable
data class GrupoMessageInstalacion(
    val id:Int,
    val establecimiento_id:Int,
    val photo:String? = null,
    val total_price:Int = 0,
    val cupos:List<CupoInstalacion> = emptyList(),
    val name:String,
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

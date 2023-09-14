package app.regate.data.dto.empresa.salas

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class SalaDto(
    val id: Long,
    val instalacion_id: Long,
    val establecimiento_id: Long,
    val cupos: Int,
    val descripcion: String,
    val titulo: String,
    val precio: Int,
    val paid:Double,
    val horas:List<String>,
    val created_at: String,
    val precio_cupo:Double = 0.0,
    val users:Int,
    val grupo_id:Long,
    val estado:Int,
    val establecimiento_name: String = "",
    val establecimiento_photo: String? = null,
    val category_id: Int = 0,
)

enum class SalaEstado{
    AVAILABLE,
    UNAVAILABLE,
    RESERVED,
}

@Serializable
data class SalaRequestDto(
    val category_id: Int = 0,
//    val created_at: String,
    val cupos: Int? = null,
    val descripcion: String = "",
    val establecimiento_id: Long = 0,
//    val id: Long,
    val instalacion_id: Long = 0,
    val precio: Int = 100000,
    val horas:List<Instant> = emptyList(),
    val titulo: String = "",
//    val start_time:String = "",
//    val end_time:String = "",
//    val fecha:String = "",
    val grupo_id: Long? = null
)
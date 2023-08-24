package app.regate.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "sala"
)
data class SalaEntity(
    @PrimaryKey(autoGenerate = true)
    override val id: Long = 0,
    val category_id: Int = 0,
    val created_at: String = "",
    val cupos: Int = 0,
    val descripcion: String ="",
    val establecimiento_id: Long = 0,
    val instalacion_id: Long = 0,
    val precio: Int = 0,
    val paid:Double = 0.0,
    val titulo: String = "",
    val precio_cupo:Double = 0.0,
    val users:Int = 0,
    val grupo_id:Long =0,
    val estado:Int =0,
    val horas:List<String> = emptyList(),
    val establecimiento_name: String = "",
    val establecimiento_photo: String? = null
):AppEntity

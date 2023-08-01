package app.regate.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
@Entity(
    tableName = "instalaciones",
    indices = [
        Index(value = ["establecimiento_id"])
              ],
    foreignKeys = [
        ForeignKey(
            entity = Establecimiento::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("establecimiento_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Instalacion (
    @PrimaryKey
    override val id: Long,
    val cantidad_personas: Int? = null,
    val category_id: Int? = null,
    val category_name: String? = null,
    val description: String? = null,
    val establecimiento_id: Long,
    val name: String,
    val precio_hora: Int? = null,
    val portada:String? = null
):AppEntity
package app.regate.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity(
    tableName = "cupos",
    indices = [
        Index(value = ["instalacion_id"])
    ],
)
data class Cupo(
    @PrimaryKey(autoGenerate = true)
    override val id: Long = 0,
    val time:Instant,
    val instalacion_id: Long,
    val price:Double,
):AppEntity

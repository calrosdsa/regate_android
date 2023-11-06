package app.regate.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import app.regate.data.dto.empresa.establecimiento.AttentionScheduleTimeDto
import app.regate.models.establecimiento.Establecimiento

@Entity(
    tableName= "attention_schedule",
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
data class AttentionSchedule(
    @PrimaryKey
    override val id: Long = 0,
    val day_week:Int= 0,
    val establecimiento_id:Long = 0,
    val open:Boolean = false,
    val closed:Boolean = false,
    val schedule_interval:List<AttentionScheduleTimeDto> = emptyList()
):AppEntity
package app.regate.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import app.regate.data.dto.empresa.establecimiento.HorarioInterval
import app.regate.data.dto.empresa.establecimiento.PaidType
import app.regate.models.establecimiento.Establecimiento

@Entity(
    tableName= "settings",
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
data class Setting(
    @PrimaryKey
    val uuid:String,
//    override val id: Long = 0,
    val paid_type:PaidType? = null,
    val establecimiento_id:Long,
    val payment_for_reservation:Int? = null,
    val horario_interval:List<HorarioInterval>
)
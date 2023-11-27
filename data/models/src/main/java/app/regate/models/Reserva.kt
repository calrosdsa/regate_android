package app.regate.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity(
    tableName = "reservas"
)
data class Reserva(
    @PrimaryKey
    override val id: Long = 0,
    val instalacion_id:Long,
    val description:String? =null,
    val instalacion_name:String = "",
    val establecimiento_id:Long,
    val pagado: Double = 0.0,
    val total_price: Double = 0.0,
    val start_date: Instant,
    val end_date: Instant,
    val estado:Int = 0,
    val instalacion_photo:String? = null,
    val user_id: Long,
    var created_at:Instant
):AppEntity



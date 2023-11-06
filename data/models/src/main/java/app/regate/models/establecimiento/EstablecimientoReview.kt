package app.regate.models.establecimiento

import androidx.room.Entity
import app.regate.data.dto.account.user.ProfileDto

@Entity(
    tableName = "establecimiento_review",
    primaryKeys = ["establecimiento_id", ""]
)
data class EstablecimientoReview(
    val establecimiento_id:Long,
    val review:String,
    val score:Int,
    val profile: ProfileDto? = null
)

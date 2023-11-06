package app.regate.models

import androidx.room.Entity
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Entity(
    tableName = "profile_category",
    primaryKeys = ["profile_id","category_id"]
)
data class ProfileCategory(
    val profile_id:Long,
    val category_id:Int,
    val created_at:Instant = Clock.System.now(),
)

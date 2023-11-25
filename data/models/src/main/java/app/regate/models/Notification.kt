package app.regate.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "notification"
)
data class Notification(
    @PrimaryKey(autoGenerate = true)
    override val id: Long = 0,
    val title:String? = null,
    val content:String = "",
    val entityId:Long?= null,
    val typeEntity:TypeEntity? = null,
    val read:Boolean = false,
    val image:String? = null,
    val created_at:Instant = Clock.System.now()
):AppEntity

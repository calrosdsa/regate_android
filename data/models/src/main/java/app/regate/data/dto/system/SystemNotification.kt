package app.regate.data.dto.system

import androidx.room.Entity
import androidx.room.PrimaryKey
import app.regate.models.TypeEntity
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class NotificationCount(
    val count:Int
)

@Serializable
data class NotificationDto(
    val id: Long = 0,
    val title:String? = null,
    val content:String = "",
    val entity_id:Long?= null,
    val type_entity: Int = TypeEntity.NONE.ordinal,
    val read:Boolean = false,
    val image:String? = null,
    val profile_id:Int = 0,
    val created_at: Instant = Clock.System.now()
)
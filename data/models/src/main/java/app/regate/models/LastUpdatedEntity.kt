package app.regate.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Entity(
    tableName = "last_updated_entity"
)
data class LastUpdatedEntity(
    @PrimaryKey
    val entity_id:UpdatedEntity,
    val created_at:Instant = Clock.System.now()
)

package app.regate.models.establecimiento

import androidx.room.Entity
import androidx.room.PrimaryKey
import app.regate.models.AppEntity

@Entity(
    tableName = "favorite_establecimiento"
)
data class FavoriteEstablecimiento(
    @PrimaryKey(autoGenerate = true)
    override val id: Long = 0,
    val establecimiento_id:Long
): AppEntity

package app.regate.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "favorite_establecimiento"
)
data class FavoriteEstablecimiento(
    @PrimaryKey(autoGenerate = true)
    override val id: Long = 0,
    val establecimiento_id:Long
):AppEntity

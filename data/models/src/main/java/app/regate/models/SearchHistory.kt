package app.regate.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Entity(
    tableName = "search_history"
)
data class SearchHistory(
    @PrimaryKey(autoGenerate = true)
    override val id: Long = 0,
    val query:String = "",
    val created_at:Instant? = Clock.System.now()
):AppEntity

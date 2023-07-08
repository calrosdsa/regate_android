package app.regate.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity(
    tableName = "grupos",
)
data class Grupo(
    @PrimaryKey
    override val id: Long,
    val name:String,
    val description: String? = null,
    val created_at: Instant? = null,
    val photo:String? = null,
):AppEntity
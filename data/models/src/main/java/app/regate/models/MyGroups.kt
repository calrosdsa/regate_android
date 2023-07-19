package app.regate.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "my_groups",
)
data class MyGroups(
    @PrimaryKey(autoGenerate = true)
    override val id: Long = 0,
    val group_id:Long
):AppEntity

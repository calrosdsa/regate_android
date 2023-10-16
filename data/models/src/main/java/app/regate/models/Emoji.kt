package app.regate.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "emoji",
)
data class Emoji(
    @PrimaryKey(autoGenerate = false)
    val id:Int = 0,
    val emoji:String = "",
    val description:String = "",
    val category:String = ""
)

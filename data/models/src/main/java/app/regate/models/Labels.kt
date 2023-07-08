package app.regate.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "labels",
    primaryKeys = ["id", "name"]
)
data class Labels(
    val id:Long = 0,
    val name:String = "",
    val thumbnail:String? = null,
    val type_label:LabelType? = null
)

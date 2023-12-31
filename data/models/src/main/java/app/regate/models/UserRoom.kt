package app.regate.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "user_room",
//    indices = [
//        Index(value = ["grupo_id"])
//    ],
//    foreignKeys = [
//        ForeignKey(
//            entity = Grupo::class,
//            parentColumns = arrayOf("id"),
//            childColumns = arrayOf("grupo_id"),
//            onUpdate = ForeignKey.CASCADE,
//            onDelete = ForeignKey.CASCADE
//        )
//    ],
)
data class UserRoom(
    @PrimaryKey(autoGenerate = true)
    override val id: Long = 0,
    val profile_id:Long,
    val sala_id:Long,
    val is_admin:Boolean = false,
    val is_out:Boolean = false
):AppEntity

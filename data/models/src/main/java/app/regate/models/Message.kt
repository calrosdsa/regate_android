package app.regate.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity(
    tableName = "messages",
//    indices = [
//        Index(value = ["profile_id"])
//    ],
//    foreignKeys = [
//        ForeignKey(
//            entity = Profile::class,
//            parentColumns = arrayOf("id"),
//            childColumns = arrayOf("profile_id"),
//            onUpdate = ForeignKey.CASCADE,
//            onDelete = ForeignKey.CASCADE
//        )
//    ]
)
data class Message(
    @PrimaryKey
    override val id: Long,
    val grupo_id:Long,
    val content:String,
    val created_at:Instant,
    val profile_id:Long,
    val reply_to:Long? = null,
):AppEntity

package app.regate.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import app.regate.data.dto.empresa.grupo.GrupoMessageType
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
    val chat_id:Long,
    val content:String,
    val data:String? = null,
    val created_at:Instant,
    val type_message:Int = GrupoMessageType.MESSAGE.ordinal,
    val profile_id:Long,
    val reply_to:Long? = null,
    val sended:Boolean = false,
    val readed:Boolean = false,
    val parent_id:Long = 0,
):AppEntity

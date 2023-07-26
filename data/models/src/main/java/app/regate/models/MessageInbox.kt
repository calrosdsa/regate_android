package app.regate.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity(
    tableName = "message_inbox"
)
data class MessageInbox(
    @PrimaryKey
    override val id: Long,
    val conversation_id:Long,
    val content:String,
    val created_at: Instant,
    val sender_id:Long,
    val reply_to:Long? = null,
    val sended:Boolean = false,
):AppEntity

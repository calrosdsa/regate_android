package app.regate.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity(
    tableName = "message_sala"
)
data class MessageSala(
    @PrimaryKey
    override val id: Long,
    val sala_id:Long,
    val content:String,
    val created_at: Instant,
    val profile_id:Long,
    val reply_to:Long? = null,
    val sended:Boolean = false,
):AppEntity

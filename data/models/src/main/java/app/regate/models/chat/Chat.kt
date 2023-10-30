package app.regate.models.chat

import androidx.room.Entity
import androidx.room.PrimaryKey
import app.regate.data.dto.chat.TypeChat
import app.regate.models.AppEntity
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Entity(
    tableName = "chat"
)
data class Chat(
    @PrimaryKey
    override val id: Long = 0,
    val photo:String? = null,
    val name:String = "",
    val last_message:String? = null,
    val last_message_created:Instant? = null,
    val messages_count:Int = 0,
    val type_chat:Int = TypeChat.TYPE_CHAT_GRUPO.ordinal,
    val is_message_deleted:Boolean = false,
    val parent_id:Long = 0,
    val updated_at:Instant = Clock.System.now()
):AppEntity

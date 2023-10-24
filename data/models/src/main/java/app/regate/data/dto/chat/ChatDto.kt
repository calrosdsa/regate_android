package app.regate.data.dto.chat

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class ChatDto(
    val id:Long =0,
    val photo:String? = null,
    val name:String = "",
    val type_chat:Int =  TypeChat.TYPE_CHAT_GRUPO.ordinal,
    val parent_id:Long = 0
)

enum class TypeChat {
    NONE,
    TYPE_CHAT_GRUPO,
    TYPE_CHAT_INBOX_ESTABLECIMIENTO;
}
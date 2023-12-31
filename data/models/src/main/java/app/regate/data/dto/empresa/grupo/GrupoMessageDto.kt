package app.regate.data.dto.empresa.grupo

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class GrupoMessageDto(
    val content: String,
    val data:String? = null,
    val created_at: Instant = Clock.System.now(),
    val id: Long = 0,
    val local_id:Long = 0,
    val profile_id: Long,
    val chat_id:Long,
    val type_message:Int = GrupoMessageType.MESSAGE.ordinal,
    val reply_to:Long? = null,
    val reply_message: ReplyMessage = ReplyMessage.Empty,
    val parent_id:Long = 0,
    val is_deleted:Boolean = false,
    //only for conversation message
    val is_user:Boolean = false,
    val is_read:Boolean = true,
    )

@Serializable
data class ReplyMessage(
    val content: String = "",
    val created_at: String = "",
    val type_message:Int = GrupoMessageType.MESSAGE.ordinal,
    val id: Long = 0,
    val profile_id: Long = 0,
    val grupo_id:Long = 0,
    val data:String? = null,
    val reply_to:Long? = null,
){
    companion object{
        val  Empty = ReplyMessage()
    }
}


@Serializable
data class Sala(
    val cupos:Int,
    val users:Int,
    val sala_id:Int,
    val nombre:String
)

@Serializable
data class GrupoEvent(
    val type:String,
    val message: GrupoMessageDto,
    val sala:Sala? = null
)
object GrupoEventType {
    val GrupoEventSala = "sala"
    val GrupoEventMessage = "message"
    val GrupoEventIgnore = "ignore"
    val GrupoEvnetUser = "user"
}




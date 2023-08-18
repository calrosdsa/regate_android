package app.regate.data.dto.empresa.salas

import app.regate.data.dto.empresa.grupo.GrupoMessageDto
import app.regate.data.dto.empresa.grupo.Sala
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class MessageSalaDto(
    val content: String,
    val created_at: Instant?=null,
    val id: Long = 0,
    val profile_id: Long,
    val sala_id:Long,
    val reply_to:Long? = null,
    val reply_message: ReplyMessageSala = ReplyMessageSala.Empty
)

@Serializable
data class ReplyMessageSala(
    val content: String = "",
    val created_at: String = "",
    val id: Long = 0,
    val profile_id: Long = 0,
    val sala_id:Long = 0,
    val reply_to:Long? = null,
){
    companion object{
        val  Empty = ReplyMessageSala()
    }
}


@Serializable
data class MessageSalaPagination(
    val results:List<MessageSalaDto> = emptyList(),
    val nextPage:Int,
)


@Serializable
data class SalaEvent(
    val type:String,
    val message: MessageSalaDto,
//    val sala: Sala? = null
)
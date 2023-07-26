package app.regate.data.mappers

import app.regate.data.dto.empresa.conversation.ConversationMessage
import app.regate.data.dto.empresa.grupo.GrupoMessageDto
import app.regate.models.Message
import app.regate.models.MessageInbox
import kotlinx.datetime.Clock
import me.tatarka.inject.annotations.Inject

@Inject
class MessageConversationToMessage:Mapper<ConversationMessage,MessageInbox> {
    override suspend fun map(from: ConversationMessage): MessageInbox {
        return MessageInbox(
            id = from.id,
            conversation_id = from.conversation_id,
            sender_id = from.sender_id,
            content = from.content,
            created_at = from.created_at?: Clock.System.now(),
            reply_to = from.reply_to,
            sended = true
        )
    }
}


//@Inject
//class MessageToMessageDto:Mapper<Message,GrupoMessageDto> {
//    override suspend fun map(from: Message): GrupoMessageDto {
//        return GrupoMessageDto(
//            id = from.id,
//            profile_id = from.profile_id,
//            grupo_id = from.grupo_id,
//            content = from.content,
//            created_at = from.created_at,
//            reply_to = from.reply_to,
//        )
//    }
//}
package app.regate.data.mappers

import app.regate.data.dto.empresa.grupo.GrupoMessageDto
import app.regate.models.Message
import kotlinx.datetime.Clock
import me.tatarka.inject.annotations.Inject

@Inject
class MessageDtoToMessage:Mapper<GrupoMessageDto,Message> {
    override suspend fun map(from: GrupoMessageDto): Message {
        return Message(
            id = from.id,
            profile_id = from.profile_id,
            chat_id = from.chat_id,
            content = from.content,
            created_at = from.created_at,
            reply_to = from.reply_to,
            type_message = from.type_message,
            data = from.data,
            parent_id = from.parent_id,
            sended = true,
            readed = false,
            is_deleted = from.is_deleted
        )
    }
}


@Inject
class MessageToMessageDto:Mapper<Message,GrupoMessageDto> {
    override suspend fun map(from: Message): GrupoMessageDto {
        return GrupoMessageDto(
            id = from.id,
            profile_id = from.profile_id,
            chat_id = from.chat_id,
            content = from.content,
            created_at = from.created_at,
            data = from.data,
            type_message = from.type_message,
            reply_to = from.reply_to,
            parent_id = from.parent_id
        )
    }
}
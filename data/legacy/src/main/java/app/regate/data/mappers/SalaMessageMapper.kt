package app.regate.data.mappers

import app.regate.data.dto.empresa.grupo.GrupoMessageDto
import app.regate.data.dto.empresa.salas.MessageSalaDto
import app.regate.models.Message
import app.regate.models.MessageSala
import kotlinx.datetime.Clock
import me.tatarka.inject.annotations.Inject

@Inject
class MessageDtoToMessageSala:Mapper<MessageSalaDto, MessageSala> {
    override suspend fun map(from: MessageSalaDto): MessageSala {
        return MessageSala(
            id = from.id,
            profile_id = from.profile_id,
            sala_id = from.sala_id,
            content = from.content,
            created_at = from.created_at?: Clock.System.now(),
            reply_to = from.reply_to,
            sended = true
        )
    }
}


@Inject
class MessageToMessageSalaDto:Mapper<MessageSala, MessageSalaDto> {
    override suspend fun map(from: MessageSala): MessageSalaDto {
        return MessageSalaDto(
            id = from.id,
            profile_id = from.profile_id,
            sala_id = from.sala_id,
            content = from.content,
            created_at = from.created_at,
            reply_to = from.reply_to,
        )
    }
}
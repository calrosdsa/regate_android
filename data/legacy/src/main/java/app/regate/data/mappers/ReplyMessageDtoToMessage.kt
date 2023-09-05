package app.regate.data.mappers

import app.regate.data.dto.empresa.grupo.ReplyMessage
import app.regate.models.Message
import kotlinx.datetime.toInstant
import me.tatarka.inject.annotations.Inject

@Inject
class ReplyMessageDtoToMessage:Mapper<ReplyMessage,Message> {
    override suspend fun map(from: ReplyMessage): Message {
        return Message(
            id = from.id,
            content = from.content,
            profile_id = from.profile_id,
            created_at = from.created_at.toInstant(),
            type_message = from.type_message,
            grupo_id = from.grupo_id
        )
    }
}
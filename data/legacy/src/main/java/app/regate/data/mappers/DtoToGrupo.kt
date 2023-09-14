package app.regate.data.mappers

import app.regate.data.dto.empresa.grupo.GrupoDto
import app.regate.models.Grupo
import me.tatarka.inject.annotations.Inject

@Inject
class DtoToGrupo:Mapper<GrupoDto,Grupo> {
    override suspend fun map(from: GrupoDto): Grupo {
        return Grupo(
            id = from.id,
            created_at = from.created_at,
            description = from.descripcion,
            name = from.name,
            photo = from.photo,
            profile_id = from.profile_id,
            visibility = from.visibility,
            last_message = from.last_message,
            last_message_created = from.last_message_created,
            messages_count = from.messages_count
        )
    }
}
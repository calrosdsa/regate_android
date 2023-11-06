package app.regate.data.mappers

import app.regate.data.dto.empresa.grupo.GrupoDto
import app.regate.models.grupo.Grupo
import me.tatarka.inject.annotations.Inject

@Inject
class DtoToGrupo:Mapper<GrupoDto, Grupo> {
    override suspend fun map(from: GrupoDto): Grupo {
        return Grupo(
            id = from.id,
            uuid = from.uuid,
            created_at = from.created_at,
            description = from.descripcion,
            name = from.name,
            photo = from.photo,
            profile_id = from.profile_id,
            visibility = from.visibility,
            is_visible = from.is_visible
        )
    }
}
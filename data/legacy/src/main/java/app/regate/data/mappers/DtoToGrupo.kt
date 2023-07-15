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
            user_id = from.user_id
         )
    }
}
package app.regate.data.mappers

import app.regate.data.dto.empresa.grupo.UserGrupoDto
import app.regate.models.grupo.UserGrupo
import me.tatarka.inject.annotations.Inject

@Inject
class DtoToUserGrupo:MapperWithAttr<UserGrupoDto, UserGrupo> {
    override suspend fun map(from: UserGrupoDto,id:Long): UserGrupo {
        return UserGrupo(
            profile_id = from.profile_id,
            grupo_id = id,
            is_admin = from.is_admin,
            id = from.id,
        )
    }
}
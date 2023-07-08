package app.regate.data.mappers

import app.regate.data.dto.account.user.ProfileDto
import app.regate.models.UserGrupo
import me.tatarka.inject.annotations.Inject

@Inject
class DtoToUserGrupo:MapperWithAttr<ProfileDto,UserGrupo> {
    override suspend fun map(from: ProfileDto,id:Long): UserGrupo {
        return UserGrupo(
            id = from.user_grupo_id,
            profile_id = from.profile_id,
            grupo_id = id
        )
    }
}
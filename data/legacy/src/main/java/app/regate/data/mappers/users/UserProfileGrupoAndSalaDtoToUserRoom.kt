package app.regate.data.mappers.users

import app.regate.compoundmodels.UserProfileGrupoAndSalaDto
import app.regate.data.dto.empresa.grupo.UserGrupoDto
import app.regate.data.mappers.Mapper
import app.regate.models.UserRoom
import app.regate.models.grupo.UserGrupo
import app.regate.models.user.Profile
import me.tatarka.inject.annotations.Inject

@Inject
class UserProfileGrupoAndSalaDtoToUserRoom: Mapper<UserProfileGrupoAndSalaDto, UserRoom> {
    override suspend fun map(from: UserProfileGrupoAndSalaDto): UserRoom {
        return UserRoom(
            id = from.id,
            profile_id = from.profile_id,
            is_out = from.is_out,
            sala_id = from.parent_id,
            is_admin = from.is_admin
        )
    }
}
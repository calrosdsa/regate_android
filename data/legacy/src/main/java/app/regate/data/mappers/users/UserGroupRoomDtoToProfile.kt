package app.regate.data.mappers.users

import app.regate.compoundmodels.UserProfileGrupoAndSalaDto
import app.regate.data.mappers.Mapper
import app.regate.models.user.Profile
import me.tatarka.inject.annotations.Inject

@Inject
class UserGroupRoomDtoToProfile: Mapper<UserProfileGrupoAndSalaDto, Profile> {
    override suspend fun map(from: UserProfileGrupoAndSalaDto): Profile {
        return Profile(
            id = from.profile_id,
            nombre = from.nombre,
            apellido = from.apellido,
            profile_photo = from.profile_photo
        )
    }
}
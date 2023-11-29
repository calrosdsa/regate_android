package app.regate.data.mappers.users

import app.regate.data.dto.account.user.ProfileDto
import app.regate.data.mappers.Mapper
import app.regate.models.user.Profile
import me.tatarka.inject.annotations.Inject

@Inject
class DtoToProfile: Mapper<ProfileDto, Profile> {
    override suspend fun map(from: ProfileDto): Profile {
        return Profile(
            id = from.profile_id,
            user_id = from.user_id,
            profile_photo = from.profile_photo,
            nombre = from.nombre,
            apellido = from.apellido,
            email = from.email,
            created_at = from.created_at
        )
    }
}
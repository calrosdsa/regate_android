package app.regate.data.mappers.users

import app.regate.data.dto.notifications.ProfileBaseDto
import app.regate.data.mappers.Mapper
import app.regate.models.user.Profile
import me.tatarka.inject.annotations.Inject

@Inject
class ProfileToProfileBaseDto:Mapper<Profile,ProfileBaseDto> {
    override suspend fun map(from: Profile): ProfileBaseDto {
        return ProfileBaseDto(
            id = from.id,
            profile_photo = from.profile_photo,
            name = from.nombre,
            apellido = from.apellido
        )
    }
}
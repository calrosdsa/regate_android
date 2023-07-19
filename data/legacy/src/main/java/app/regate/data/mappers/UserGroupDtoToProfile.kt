package app.regate.data.mappers

import app.regate.data.dto.empresa.grupo.UserGrupoDto
import app.regate.models.Profile
import me.tatarka.inject.annotations.Inject

@Inject
class UserGroupDtoToProfile:Mapper<UserGrupoDto,Profile> {
    override suspend fun map(from: UserGrupoDto): Profile {
        return Profile(
            id = from.profile_id,
            profile_photo = from.profile_photo,
            nombre = from.nombre,
            apellido = from.apellido,
        )
    }
}
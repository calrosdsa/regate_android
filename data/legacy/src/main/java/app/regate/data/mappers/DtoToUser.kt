package app.regate.data.mappers

import app.regate.data.dto.account.auth.UserDto
import app.regate.models.User
import me.tatarka.inject.annotations.Inject

@Inject
class DtoToUser:Mapper<UserDto,User> {
    override suspend fun map(from: UserDto): User = User(
        user_id = from.user_id,
        email = from.email,
        username = from.username,
        nombre = from.nombre,
        apellido = from.apellido,
        profile_photo = from.profile_photo,
        estado = from.estado,
        coins = from.coins,
        profile_id = from.profile_id
    )
}
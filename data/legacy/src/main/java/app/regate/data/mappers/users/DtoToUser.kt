package app.regate.data.mappers.users

import app.regate.data.dto.account.auth.UserDto
import app.regate.data.mappers.Mapper
import app.regate.models.account.User
import me.tatarka.inject.annotations.Inject

@Inject
class DtoToUser: Mapper<UserDto, User> {
    override suspend fun map(from: UserDto): User = User(
        user_id = from.user_id,
        email = from.email,
        username = from.username,
        estado = from.estado,
//        coins = from.coins,
        profile_id = from.profile_id
    )
}
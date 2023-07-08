package app.regate.data.users

import app.regate.data.dto.account.user.ProfileDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import me.tatarka.inject.annotations.Inject

@Inject
class UsersDataSourceImpl(
    private val client:HttpClient
): UsersDataSource {
    override suspend fun getProfile(id: Long): ProfileDto {
        return client.get("v1/users/profile/${id}/").body()
    }

}
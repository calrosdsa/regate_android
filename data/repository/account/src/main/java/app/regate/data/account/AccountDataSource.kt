package app.regate.data.account

import app.regate.data.dto.account.auth.LoginRequest
import app.regate.data.dto.account.auth.LoginResponse
import app.regate.data.dto.account.auth.UserDto
import app.regate.data.dto.account.auth.FcmRequest
import app.regate.data.dto.account.user.ProfileDto
import app.regate.models.User

interface AccountDataSource {
    suspend fun me(): UserDto
    suspend fun login(d: LoginRequest): LoginResponse
    suspend fun updateUser(user:User)
    suspend fun socialLogin(user: UserDto): LoginResponse
    suspend fun saveFcmToken(data:FcmRequest)

}


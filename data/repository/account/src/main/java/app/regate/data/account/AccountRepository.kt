package app.regate.data.account

import app.regate.data.daos.UserDao
import app.regate.data.dto.account.auth.LoginRequest
import app.regate.data.dto.account.auth.LoginResponse
import app.regate.data.dto.account.auth.UserDto
import app.regate.data.dto.account.auth.FcmRequest
import app.regate.inject.ApplicationScope
import app.regate.models.User
import me.tatarka.inject.annotations.Inject
@ApplicationScope
@Inject
class AccountRepository(
    private val accountDataSourceImpl: AccountDataSourceImpl,
    private val userDao: UserDao
//    private val establecimientoDao: EstablecimientoDao
){
    suspend fun clearAuthData(){
        userDao.deleteUser()
    }
    suspend fun me(): UserDto {
        return accountDataSourceImpl.me()
    }

    suspend fun updateUser(user:User){
        accountDataSourceImpl.updateUser(user)
    }
    suspend fun  login(d: LoginRequest): LoginResponse {
        return accountDataSourceImpl.login(d)
    }
    suspend fun  socialLogin(user: UserDto): LoginResponse {
        return accountDataSourceImpl.socialLogin(user)
    }
    suspend fun saveFcmToken(data:FcmRequest){
        accountDataSourceImpl.saveFcmToken(data)
    }
}
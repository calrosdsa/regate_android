package app.regate.data.account

import app.regate.data.daos.ProfileDao
import app.regate.data.daos.UserDao
import app.regate.data.dto.account.auth.LoginRequest
import app.regate.data.dto.account.auth.LoginResponse
import app.regate.data.dto.account.auth.UserDto
import app.regate.data.dto.account.auth.FcmRequest
import app.regate.data.dto.account.auth.SocialRequest
import app.regate.data.mappers.DtoToUser
import app.regate.inject.ApplicationScope
import app.regate.models.Profile
import app.regate.models.User
import app.regate.settings.AppPreferences
import app.regate.util.AppCoroutineDispatchers
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
@ApplicationScope
@Inject
class AccountRepository(
    private val accountDataSourceImpl: AccountDataSourceImpl,
    private val userDao: UserDao,
    private val dtoToUser: DtoToUser,
    private val profileDao: ProfileDao,
    private val dispatchers: AppCoroutineDispatchers,
    private val appPreferences: AppPreferences
//    private val establecimientoDao: EstablecimientoDao
){
    suspend fun clearAuthData(){
        userDao.deleteUser()
    }
    suspend fun me(): UserDto {
        return accountDataSourceImpl.me()
    }
    suspend fun updateFcmToken(token:String){
        withContext(dispatchers.io){
            accountDataSourceImpl.updateFcmToken(token)
        }
    }

    private suspend fun updateUser(user:User){
            userDao.upsert(user)
    }
    private suspend fun insertProfile(user:UserDto){
        profileDao.upsert(Profile(
            id = user.profile_id,
            user_id = user.user_id,
            profile_photo = user.profile_photo,
            nombre = user.nombre,
            apellido = user.apellido
        ))
    }
    suspend fun  login(d: LoginRequest) {
        withContext(dispatchers.computation){
          accountDataSourceImpl.login(d).also {
            updateUser(dtoToUser.map(it.user))
            insertProfile(it.user)
        }
        }
    }
    suspend fun  socialLogin(user: UserDto){
        withContext(dispatchers.computation){
         accountDataSourceImpl.socialLogin(SocialRequest(user = user,fcm_token = appPreferences.fcmToken)).also {
            updateUser(dtoToUser.map(it.user))
             insertProfile(it.user)
        }
        }
    }
    suspend fun saveFcmToken(data:FcmRequest){
        accountDataSourceImpl.saveFcmToken(data)
    }
}
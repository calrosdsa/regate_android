package app.regate.data.account

import app.regate.data.daos.FavoriteEstablecimientoDao
import app.regate.data.daos.MyGroupsDao
import app.regate.data.daos.NotificationDao
import app.regate.data.daos.ProfileDao
import app.regate.data.daos.ReservaDao
import app.regate.data.daos.UserDao
import app.regate.data.dto.account.auth.LoginRequest
import app.regate.data.dto.account.auth.UserDto
import app.regate.data.dto.account.auth.FcmRequest
import app.regate.data.dto.account.auth.SignUpRequest
import app.regate.data.dto.account.auth.SocialRequest
import app.regate.data.dto.account.billing.ConsumePaginationResponse
import app.regate.data.dto.account.billing.DepositPaginationResponse
import app.regate.data.dto.account.billing.MontoRetenidoPaginationRespone
import app.regate.data.mappers.DtoToUser
import app.regate.inject.ApplicationScope
import app.regate.models.user.Profile
import app.regate.models.account.User
import app.regate.settings.AppPreferences
import app.regate.util.AppCoroutineDispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject
@ApplicationScope
@Inject
class AccountRepository(
    private val accountDataSourceImpl: AccountDataSourceImpl,
    private val userDao: UserDao,
    private val dtoToUser: DtoToUser,
    private val profileDao: ProfileDao,
    private val dispatchers: AppCoroutineDispatchers,
    private val appPreferences: AppPreferences,
    private val myGroupsDao: MyGroupsDao,
    private val favoriteEstablecimientoDao: FavoriteEstablecimientoDao,
    private val notificationDao: NotificationDao,
    private val reservaDao: ReservaDao,
//    private val establecimientoDao: EstablecimientoDao
){
    suspend fun resendEmailVerification(id: Long){
        accountDataSourceImpl.resendEmailVerification(id)
    }
    suspend fun clearAuthData(){
        withContext(dispatchers.computation){
            try{
                userDao.deleteUser()
                myGroupsDao.deleteAll()
                favoriteEstablecimientoDao.removeAll()
                notificationDao.deleteAll()
                reservaDao.deleteAll()
            }catch(e:Exception){
                //TODO()
            }
        }
    }
    suspend fun me(): UserDto {
        return accountDataSourceImpl.me()
    }
    suspend fun updateFcmToken(token:String){
        withContext(dispatchers.io){
            try{
            accountDataSourceImpl.updateFcmToken(token)
            }catch(e:Exception){
                //TODO()
            }
        }
    }

    private suspend fun updateUser(user: User){
            userDao.upsert(user)
    }
    private suspend fun insertProfile(user:UserDto){
        profileDao.upsert(
            Profile(
            id = user.profile_id,
            user_id = user.user_id,
            profile_photo = user.profile_photo,
            nombre = user.nombre,
            apellido = user.apellido
        )
        )
    }
    suspend fun  login(d: LoginRequest) {
        withContext(dispatchers.computation){
        val fcmToken = appPreferences.fcmToken
        val categories = try { Json.decodeFromString(appPreferences.categories) }catch(e:Exception){ emptyList<Int>() }
        val data = d.copy(
            fcm_token = fcmToken,
            categories = categories
        )
            try {
                accountDataSourceImpl.login(data).also {
                    updateUser(dtoToUser.map(it.user))
                    insertProfile(it.user)
                }
            }catch (e:Exception){
                //TODO()
        }
        }
    }
    suspend fun signUp(d:SignUpRequest){
        withContext(dispatchers.computation){
            try{
                val fcmToken = appPreferences.fcmToken
                val categories = try { Json.decodeFromString(appPreferences.categories) }catch(e:Exception){ emptyList<Int>() }
                val data = d.copy(fcm_token = fcmToken, categories = categories)
            accountDataSourceImpl.signUp(data).also {
                updateUser(dtoToUser.map(it.user))
                insertProfile(it.user)
            }
            }catch(e:Exception){
                throw  e
            }
        }
    }
    suspend fun verifyEmail(otp:Int){
        withContext(dispatchers.computation) {
            val user = userDao.getUser(0)
            accountDataSourceImpl.verifyEmail(userId = user.user_id, otp = otp).also {
                updateUser(dtoToUser.map(it.user))
                insertProfile(it.user)
            }
        }
    }
    suspend fun  socialLogin(user: UserDto) {
        withContext(dispatchers.computation) {
            try {
                val categories = try { Json.decodeFromString(appPreferences.categories) }catch(e:Exception){ emptyList<Int>() }
                accountDataSourceImpl.socialLogin(
                    SocialRequest(
                        user = user,
                        fcm_token = appPreferences.fcmToken,
                        categories = categories
                    )
                ).also {
                    updateUser(dtoToUser.map(it.user))
                    insertProfile(it.user)
                }
            } catch (e: Exception) {
                //TODO()
            }
        }
    }
    suspend fun saveFcmToken(data:FcmRequest){
        withContext(dispatchers.computation){
        try{
        accountDataSourceImpl.saveFcmToken(data)
        }catch(e:Exception){
            //TODO()
        }
        }
    }

    suspend fun getDepositPagination(page:Int):DepositPaginationResponse{
        return accountDataSourceImpl.getDeposits(page)
    }
    suspend fun getConsumePagination(page:Int):ConsumePaginationResponse{
       return accountDataSourceImpl.getConsume(page)
    }
    suspend fun getMontoRetenidoPagination(page:Int):MontoRetenidoPaginationRespone{
        return accountDataSourceImpl.getMontoRetenido(page)
    }
}
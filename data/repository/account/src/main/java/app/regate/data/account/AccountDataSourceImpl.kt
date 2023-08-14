package app.regate.data.account

import app.regate.data.auth.AppAuthAuthStateWrapper
import app.regate.data.auth.AuthRepository
import app.regate.data.auth.store.AuthStore
import app.regate.data.daos.UserDao
import app.regate.data.dto.account.auth.LoginRequest
import app.regate.data.dto.account.auth.LoginResponse
import app.regate.data.dto.account.auth.UserDto
import app.regate.data.dto.account.auth.FcmRequest
import app.regate.data.dto.account.auth.SocialRequest
import app.regate.data.dto.account.billing.ConsumePaginationResponse
import app.regate.data.dto.account.billing.DepositPaginationResponse
import app.regate.data.dto.account.user.ProfileDto
import app.regate.data.mappers.DtoToUser
import app.regate.models.User
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import me.tatarka.inject.annotations.Inject

@Inject
class AccountDataSourceImpl(
    private val client:HttpClient,
    private val authRepository:AuthRepository,
    private val authStore: AuthStore,
    private val userDao: UserDao,
    private val dtoToUser: DtoToUser,
//    private val dispatchers: AppCoroutineDispatchers,
):AccountDataSource {

    override suspend fun me(): UserDto {
        val token = authStore.get()?.accessToken
        return client.get("/v1/account/"){
            header("Authorization","Bearer $token")
        }.body<UserDto>().also {
            val user = dtoToUser.map(it)
            userDao.upsert(user)
        }
    }

    override suspend fun login(d: LoginRequest): LoginResponse {
        val res =  client.post("/v1/account/login/"){
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(email = d.email, password = d.password))
        }.body<LoginResponse>()
//        userDao.upsert(dtoToUser.map(res.user))
        return res.also {
            authRepository.clearAuth()
        val state = AppAuthAuthStateWrapper(res.access_token)
        authRepository.onNewAuthState(state)
        }
    }

    override suspend fun socialLogin(request: SocialRequest): LoginResponse {
        val res =  client.post("/v1/account/social-login/"){
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body<LoginResponse>()

//        userDao.upsert(dtoToUser.map(res.user))
        return res.also {
            authRepository.clearAuth()
            val state = AppAuthAuthStateWrapper(res.access_token)
            authRepository.onNewAuthState(state)
        }
    }

    override suspend fun saveFcmToken(data: FcmRequest) {
        client.post("/v1/notification/fcm-token/"){
            contentType(ContentType.Application.Json)
            setBody(data)
        }
    }

    override suspend fun updateFcmToken(fcm_token:String){
        val token = authStore.get()?.accessToken
        if(!token.isNullOrBlank()){
        client.get("v1/account/update-fcm-token/${fcm_token}/"){
            header("Authorization","Bearer $token")
        }
        }
    }

    override suspend fun getDeposits(page:Int):DepositPaginationResponse {
        val token = authStore.get()?.accessToken
        return client.get("/v1/account/billing/deposits/?page=${page}"){
            header("Authorization","Bearer $token")
        }.body()
    }

    override suspend fun getConsume(page:Int):ConsumePaginationResponse {
        val token = authStore.get()?.accessToken
        return client.get("/v1/account/billing/deposits/?page=${page}"){
            header("Authorization","Bearer $token")
        }.body()
    }


}
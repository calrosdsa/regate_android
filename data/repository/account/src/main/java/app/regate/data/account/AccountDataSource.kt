package app.regate.data.account

import app.regate.data.dto.account.auth.LoginRequest
import app.regate.data.dto.account.auth.AuthResponse
import app.regate.data.dto.account.auth.UserDto
import app.regate.data.dto.account.auth.FcmRequest
import app.regate.data.dto.account.auth.SignUpRequest
import app.regate.data.dto.account.auth.SocialRequest
import app.regate.data.dto.account.billing.ConsumePaginationResponse
import app.regate.data.dto.account.billing.DepositPaginationResponse

interface AccountDataSource {
    suspend fun me(): UserDto
    suspend fun login(d: LoginRequest): AuthResponse
    suspend fun signUp(d:SignUpRequest):AuthResponse
    suspend fun verifyEmail(userId:Long,otp:Int):AuthResponse
    suspend fun socialLogin(request: SocialRequest): AuthResponse
    suspend fun saveFcmToken(data:FcmRequest)
    suspend fun updateFcmToken(fcm_token:String)
    suspend fun getDeposits(page: Int):DepositPaginationResponse
    suspend fun getConsume(page:Int):ConsumePaginationResponse
    suspend fun resendEmailVerification(id:Long)
}


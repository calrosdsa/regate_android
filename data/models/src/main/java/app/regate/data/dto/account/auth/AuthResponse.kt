package app.regate.data.dto.account.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val access_token: String,
    val user: UserDto
)

@Serializable
data class UserDto(
    val email: String,
    val estado: Int,
    val user_id: Long,
    val otp:Int= 0,
    val username: String,
    val profile_photo:String?=null,
    val nombre:String,
    val apellido:String? = null,
    val social_id:String? = null,
    val profile_id:Long
)


package app.regate.data.dto.account.auth

import kotlinx.serialization.Serializable

@Serializable
data class SocialRequest(
    val user:UserDto,
    val fcm_token:String
)

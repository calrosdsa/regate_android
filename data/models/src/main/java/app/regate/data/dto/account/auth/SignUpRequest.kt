package app.regate.data.dto.account.auth

import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequest(
    val password:String,
    val username:String,
    val email:String,
    val fcm_token:String = "",
)

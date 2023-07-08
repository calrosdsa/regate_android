package app.regate.data.dto.account.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest (
    val email:String,
    val password:String
)
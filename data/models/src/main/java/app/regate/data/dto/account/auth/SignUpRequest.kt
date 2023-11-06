package app.regate.data.dto.account.auth

import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequest(
    val user:UserDto,
    val fcm_token:String = "",
    val categories:List<Int> = emptyList()
)

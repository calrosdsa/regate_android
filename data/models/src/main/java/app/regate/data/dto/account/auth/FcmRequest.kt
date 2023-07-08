package app.regate.data.dto.account.auth

import kotlinx.serialization.Serializable

@Serializable
data class FcmRequest(
    val fcm_token:String,
    val category_id:Int
)
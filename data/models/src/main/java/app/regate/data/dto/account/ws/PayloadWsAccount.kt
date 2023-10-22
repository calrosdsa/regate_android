package app.regate.data.dto.account.ws

import kotlinx.serialization.Serializable

@Serializable
data class PayloadUserBalanceUpdate(
    val profile_id:Long,
    val amount:Double,
    val should_add:Boolean,
)
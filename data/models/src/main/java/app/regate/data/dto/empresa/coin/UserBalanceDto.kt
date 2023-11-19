package app.regate.data.dto.empresa.coin

import kotlinx.serialization.Serializable

@Serializable
data class UserBalanceDto(
    val balance_id:Int,
    val coins:Double,
    val profile_id:Int,
    val retain_coin:Double
)

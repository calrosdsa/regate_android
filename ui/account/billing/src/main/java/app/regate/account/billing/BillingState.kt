package app.regate.account.billing

import app.regate.api.UiMessage
import app.regate.data.dto.empresa.coin.UserBalanceDto
import app.regate.models.account.UserBalance

data class BillingState(
    val loading:Boolean = false,
    val message:UiMessage? = null,
    val balance: UserBalance? = null
){
    companion object{
        val Empty = BillingState()
    }
}

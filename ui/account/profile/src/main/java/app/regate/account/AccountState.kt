package app.regate.account

import app.regate.api.UiMessage
import app.regate.compoundmodels.UserProfile
import app.regate.data.auth.AppAuthState
import app.regate.data.common.AddressDevice
import app.regate.data.dto.empresa.coin.UserBalanceDto
import app.regate.models.account.UserBalance

data class AccountState(
    val loading: Boolean = false,
    val message: UiMessage? = null,
    val user: UserProfile? = null,
    val authState: AppAuthState? = null,
    val addressDevice: AddressDevice? = null,
    val userBalance: UserBalance? = null,
    val unreadNotifications:Int = 0
){
    companion object{
        val Empty = AccountState()
    }
}

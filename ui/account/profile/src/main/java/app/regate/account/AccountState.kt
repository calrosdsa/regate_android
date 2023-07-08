package app.regate.account

import app.regate.api.UiMessage
import app.regate.data.auth.AppAuthState
import app.regate.data.common.AddressDevice
import app.regate.models.User

data class AccountState(
    val loading:Boolean = false,
    val message:UiMessage? = null,
    val user:User? = null,
    val authState:AppAuthState? = null,
    val addressDevice: AddressDevice? = null
){
    companion object{
        val Empty = AccountState()
    }
}

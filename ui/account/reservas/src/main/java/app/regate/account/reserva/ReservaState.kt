package app.regate.account.reserva

import app.regate.api.UiMessage
import app.regate.compoundmodels.ReservaDetail
import app.regate.data.auth.AppAuthState

data class ReservaState(
    val loading:Boolean = false,
    val message:UiMessage?= null,
    val data:ReservaDetail? = null,
    val authState:AppAuthState = AppAuthState.LOGGED_OUT
) {
    companion object{
        val Empty = ReservaState()
    }
}
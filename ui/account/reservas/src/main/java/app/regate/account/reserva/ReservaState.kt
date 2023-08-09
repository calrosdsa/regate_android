package app.regate.account.reserva

import app.regate.api.UiMessage
import app.regate.compoundmodels.ReservaDetail

data class ReservaState(
    val loading:Boolean = false,
    val message:UiMessage?= null,
    val data:ReservaDetail? = null
) {
    companion object{
        val Empty = ReservaState()
    }
}
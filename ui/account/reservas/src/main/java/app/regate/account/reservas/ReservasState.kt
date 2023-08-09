package app.regate.account.reservas

import androidx.compose.runtime.Immutable
import app.regate.api.UiMessage
import app.regate.models.Reserva

@Immutable
data class ReservasState(
    val loading:Boolean = false,
    val message:UiMessage? = null,
    val reservas: List<Reserva> = emptyList(),
){
    companion object {
        val Empty = ReservasState()
    }
}

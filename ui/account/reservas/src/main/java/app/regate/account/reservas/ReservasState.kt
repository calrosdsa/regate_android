package app.regate.discover

import androidx.compose.runtime.Immutable
import app.regate.api.UiMessage
import app.regate.data.dto.account.reserva.ReservaDto

@Immutable
data class ReservasState(
    val loading:Boolean = false,
    val message:UiMessage? = null,
    val reservas: List<ReservaDto> = emptyList(),
){
    companion object {
        val Empty = ReservasState()
    }
}

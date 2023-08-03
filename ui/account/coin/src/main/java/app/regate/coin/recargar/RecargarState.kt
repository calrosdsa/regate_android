package app.regate.coin.recargar

import app.regate.api.UiMessage
import app.regate.data.auth.AppAuthState
import app.regate.data.dto.empresa.coin.RecargaCoinDto

data class RecargarState(
    val loading:Boolean = false,
    val message:UiMessage? = null,
    val coins:List<RecargaCoinDto> = emptyList(),
    val authState:AppAuthState = AppAuthState.LOGGED_OUT
){
    companion object {
        val Empty = RecargarState()
    }
}
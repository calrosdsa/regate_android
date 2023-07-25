package app.regate.coin.recargar

import app.regate.api.UiMessage
import app.regate.data.dto.empresa.coin.RecargaCoinDto

data class RecargarState(
    val loading:Boolean = false,
    val message:UiMessage? = null,
    val coins:List<RecargaCoinDto> = emptyList()
){
    companion object {
        val Empty = RecargarState()
    }
}
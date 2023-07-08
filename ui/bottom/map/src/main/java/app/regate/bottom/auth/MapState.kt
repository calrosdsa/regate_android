package app.regate.bottom.auth

import androidx.compose.runtime.Immutable
import app.regate.api.UiMessage

@Immutable
data class MapState(
    val loading:Boolean = false,
    val message:UiMessage? = null,
){
    companion object{
        val Empty = MapState()
    }
}
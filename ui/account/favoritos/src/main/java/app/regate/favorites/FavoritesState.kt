package app.regate.favorites

import app.regate.api.UiMessage
import app.regate.models.establecimiento.Establecimiento


data class FavoritesState(
    val loading:Boolean = false,
    val message: UiMessage? = null,
    val establecimientos:List<Establecimiento> = emptyList()
){
    companion object {
        val Empty = FavoritesState()
    }
}

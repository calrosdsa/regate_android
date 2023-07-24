package app.regate.favorites

import app.regate.api.UiMessage
import app.regate.models.Establecimiento


data class FavoritesState(
    val loading:Boolean = false,
    val message: UiMessage? = null,
    val establecimientos:List<Establecimiento> = emptyList()
){
    companion object {
        val Empty = FavoritesState()
    }
}

package app.regate.favorites

import app.regate.api.UiMessage


data class FavoritesState(
    val loading:Boolean = false,
    val message: UiMessage? = null
){
    companion object {
        val Empty = FavoritesState()
    }
}

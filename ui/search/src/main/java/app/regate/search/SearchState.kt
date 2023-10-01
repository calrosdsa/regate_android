package app.regate.search

import app.regate.api.UiMessage
import app.regate.data.auth.AppAuthState

data class SearchState(
    val loading: Boolean = false,
    val message: UiMessage? = null,
    val authState: AppAuthState = AppAuthState.LOGGED_OUT
) {
    companion object {
        val Empty = SearchState()
    }
}
package app.regate.search.history

import app.regate.api.UiMessage
import app.regate.data.auth.AppAuthState

data class HistorySearchState(
    val loading: Boolean = false,
    val message: UiMessage? = null,
    val authState: AppAuthState = AppAuthState.LOGGED_OUT
) {
    companion object {
        val Empty = HistorySearchState()
    }
}
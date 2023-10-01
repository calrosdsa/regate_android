package app.regate.search.grupos

import app.regate.api.UiMessage
import app.regate.data.auth.AppAuthState

data class SearchGruposState(
    val loading: Boolean = false,
    val message: UiMessage? = null,
    val authState: AppAuthState = AppAuthState.LOGGED_OUT
) {
    companion object {
        val Empty = SearchGruposState()
    }
}
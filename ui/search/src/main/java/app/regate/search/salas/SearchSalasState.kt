package app.regate.search.salas

import androidx.compose.runtime.Immutable
import app.regate.api.UiMessage
import app.regate.data.auth.AppAuthState
import app.regate.data.dto.SearchFilterRequest

@Immutable
data class SearchSalasState(
    val loading: Boolean = false,
    val message: UiMessage? = null,
    val authState: AppAuthState = AppAuthState.LOGGED_OUT,
    val filterData:SearchFilterRequest = SearchFilterRequest()
) {
    companion object {
        val Empty = SearchSalasState()
    }
}
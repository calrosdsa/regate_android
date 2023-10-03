package app.regate.search.grupos

import androidx.compose.runtime.Immutable
import app.regate.api.UiMessage
import app.regate.data.auth.AppAuthState
import app.regate.data.dto.SearchFilterRequest
import app.regate.search.salas.SearchSalasState

@Immutable
data class SearchGruposState(
    val loading: Boolean = false,
    val message: UiMessage? = null,
    val authState: AppAuthState = AppAuthState.LOGGED_OUT,
    val filterData:SearchFilterRequest = SearchFilterRequest()
) {
    companion object {
        val Empty = SearchSalasState()
    }
}
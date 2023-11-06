package app.regate.search.grupos

import androidx.compose.runtime.Immutable
import app.regate.api.UiMessage
import app.regate.data.auth.AppAuthState
import app.regate.data.dto.SearchFilterRequest
import app.regate.models.grupo.MyGroups

@Immutable
data class SearchGruposState(
    val loading: Boolean = false,
    val message: UiMessage? = null,
    val authState: AppAuthState = AppAuthState.LOGGED_OUT,
    val filterData:SearchFilterRequest = SearchFilterRequest(),
    val userGroups:List<MyGroups> = emptyList()
) {
    companion object {
        val Empty = SearchGruposState()
    }
}
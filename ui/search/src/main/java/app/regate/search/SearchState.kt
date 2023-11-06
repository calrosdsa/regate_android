package app.regate.search

import app.regate.api.UiMessage
import app.regate.data.auth.AppAuthState
import app.regate.data.common.AddressDevice
import app.regate.data.dto.SearchFilterRequest
import app.regate.data.dto.account.user.ProfileDto
import app.regate.data.dto.empresa.grupo.GrupoDto
import app.regate.models.grupo.MyGroups
import app.regate.models.SearchHistory

data class SearchState(
    val loading: Boolean = false,
    val message: UiMessage? = null,
    val authState: AppAuthState = AppAuthState.LOGGED_OUT,
    val history:List<SearchHistory> = emptyList(),
    val grupos:List<GrupoDto> = emptyList(),
    val profiles:List<ProfileDto> = emptyList(),
    val filterData:SearchFilterRequest = SearchFilterRequest(),
    val userGroups:List<MyGroups> = emptyList(),
    val addressDevice:AddressDevice? = null
) {
    companion object {
        val Empty = SearchState()
    }
}
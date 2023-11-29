package app.regate.grupo.invitations

import androidx.compose.runtime.Immutable
import app.regate.api.UiMessage
import app.regate.data.common.AddressDevice
import app.regate.data.dto.SearchFilterRequest

@Immutable
data class InviteUserState(
    val loading:Boolean = false,
    val message: UiMessage? = null,
    val addressDevice: AddressDevice? = null,
    val selectedIds:HashMap<Long,Int> = HashMap(),
    val filterData:SearchFilterRequest = SearchFilterRequest(),
    val usersGrupo:List<Long> = emptyList()
){
    companion object {
        val Empty = InviteUserState()
    }
}

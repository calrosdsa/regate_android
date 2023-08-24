package app.regate.usersalas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import app.regate.api.UiMessageManager
import app.regate.data.dto.empresa.salas.SalaDto
import app.regate.data.dto.empresa.salas.SalaFilterData
import app.regate.data.sala.SalaRepository
import app.regate.domain.observers.ObserveAuthState
import app.regate.domain.pagination.PaginationUserSalas
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import me.tatarka.inject.annotations.Inject

@Inject
class UserSalasViewModel(
    observeAuthState: ObserveAuthState,
    private val salaRepository: SalaRepository
):ViewModel() {
    val pagedList: Flow<PagingData<SalaDto>> = Pager(PAGING_CONFIG){
        PaginationUserSalas(){page->
            salaRepository.getSalasUser(page)
        }
    }.flow.cachedIn(viewModelScope)
    private val uiMessageManager = UiMessageManager()
    val state:StateFlow<UserSalasState> = combine(
        uiMessageManager.message,
        observeAuthState.flow,
    ){message,authState ->
        UserSalasState(
            message=  message,
            authState = authState,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = UserSalasState.Empty
    )

    init{
        observeAuthState(Unit)
//        pagingInteractor(ObservePagerSalas.Params(PAGING_CONFIG))

    }


    companion object {
        val PAGING_CONFIG = PagingConfig(
            pageSize = 20,
            initialLoadSize = 20,
            prefetchDistance = 5
        )
        val FILTER_DATA = SalaFilterData()
    }
}
package app.regate.search.grupos

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import app.regate.api.UiMessageManager
import app.regate.data.dto.empresa.establecimiento.EstablecimientoReview
import app.regate.data.establecimiento.EstablecimientoRepository
import app.regate.domain.observers.ObserveAuthState
import app.regate.domain.pagination.PaginationReviews
import app.regate.util.ObservableLoadingCounter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class SearchGroupsViewModel(
    @Assisted savedStateHandle: SavedStateHandle,
    observeAuthState: ObserveAuthState,
    private val establecimientoRepository: EstablecimientoRepository,
) : ViewModel() {

    private val establecimientoId = savedStateHandle.get<Long>("id")?: 0
    private val  loaderCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    val pagedList: Flow<PagingData<EstablecimientoReview>> = Pager(PAGING_CONFIG){
        PaginationReviews(isInit =true){page->
           establecimientoRepository.getEstablecimientoReviews(establecimientoId,page,20)
//            salaRepository.filterSalas()
        }
    }.flow.cachedIn(viewModelScope)
    val state:StateFlow<SearchGruposState> = combine(
        loaderCounter.observable,
        uiMessageManager.message,
        observeAuthState.flow
    ){loading,message,authState->
        SearchGruposState(
            loading = loading,
            message = message,
            authState = authState
        )
    }.stateIn(
        scope= viewModelScope,
        initialValue = SearchGruposState.Empty,
        started = SharingStarted.WhileSubscribed()
    )

    init {
        observeAuthState(Unit)
    }

    companion object {
        val PAGING_CONFIG = PagingConfig(
            pageSize = 20,
            initialLoadSize = 20,
            prefetchDistance = 5
        )
    }

    fun getEstablecimientoId(): Long {
        return establecimientoId
    }
}
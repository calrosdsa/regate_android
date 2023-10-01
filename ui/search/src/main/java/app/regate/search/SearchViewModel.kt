package app.regate.search

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import app.regate.api.UiMessageManager
import app.regate.data.dto.SearchFilterRequest
import app.regate.data.dto.empresa.establecimiento.EstablecimientoDto
import app.regate.data.establecimiento.EstablecimientoRepository
import app.regate.domain.observers.ObserveAuthState
import app.regate.domain.pagination.PaginationSearchEstablecimientos
import app.regate.util.ObservableLoadingCounter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class SearchViewModel(
    @Assisted savedStateHandle: SavedStateHandle,
    observeAuthState: ObserveAuthState,
    private val establecimientoRepository: EstablecimientoRepository,
) : ViewModel() {
    private val establecimientoId = savedStateHandle.get<Long>("id")?: 0
    private val  loaderCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val filterData = MutableStateFlow(FILTER_DATA)
    val pagedList: Flow<PagingData<EstablecimientoDto>> = Pager(PAGING_CONFIG){
        PaginationSearchEstablecimientos(loadingState = loaderCounter){page->
            Log.d("DEBUG_APP_",filterData.value.toString())
            establecimientoRepository.searcEstablecimientos(filterData.value,page,15)
        }
    }.flow.cachedIn(viewModelScope)
    val state:StateFlow<SearchState> = combine(
        loaderCounter.observable,
        uiMessageManager.message,
        observeAuthState.flow
    ){loading,message,authState->
        SearchState(
            loading = loading,
            message = message,
            authState = authState
        )
    }.stateIn(
        scope= viewModelScope,
        initialValue = SearchState.Empty,
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
        val FILTER_DATA = SearchFilterRequest()

    }

    fun getEstablecimientoId(): Long {
        return establecimientoId
    }
}
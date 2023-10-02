package app.regate.search.history

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import app.regate.api.UiMessageManager
import app.regate.data.labels.SearchRepository
import app.regate.domain.observers.ObserveAuthState
import app.regate.domain.pagination.PaginationHistorySearch
import app.regate.models.SearchHistory
import app.regate.util.ObservableLoadingCounter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class HistorySearchViewModel(
    @Assisted savedStateHandle: SavedStateHandle,
    observeAuthState: ObserveAuthState,
    private val searchRepository: SearchRepository
) : ViewModel() {

    private val establecimientoId = savedStateHandle.get<Long>("id")?: 0
    private val  loaderCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    val pagedList: Flow<PagingData<SearchHistory>> = Pager(PAGING_CONFIG){
        PaginationHistorySearch {page->
            Log.d("DEBUG_APP_PAGE",page.toString())
           searchRepository.getHistorySearch(page)
        }
    }.flow.cachedIn(viewModelScope)
    val state:StateFlow<HistorySearchState> = combine(
        loaderCounter.observable,
        uiMessageManager.message,
        observeAuthState.flow
    ){loading,message,authState->
        HistorySearchState(
            loading = loading,
            message = message,
            authState = authState
        )
    }.stateIn(
        scope= viewModelScope,
        initialValue = HistorySearchState.Empty,
        started = SharingStarted.WhileSubscribed()
    )

    init {
        observeAuthState(Unit)
    }

    companion object {
        val PAGING_CONFIG = PagingConfig(
            pageSize = 20,
            initialLoadSize = 20,
            prefetchDistance = 10
        )
    }

    fun getEstablecimientoId(): Long {
        return establecimientoId
    }
}
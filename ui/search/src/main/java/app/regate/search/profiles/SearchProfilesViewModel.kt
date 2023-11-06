package app.regate.search.profiles

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import app.regate.api.UiMessageManager
import app.regate.data.dto.SearchFilterRequest
import app.regate.data.dto.account.user.ProfileDto
import app.regate.data.users.UsersRepository
import app.regate.domain.observers.ObserveAuthState
import app.regate.domain.observers.search.ObserveLastSearchHistory
import app.regate.domain.pagination.search.PaginationSearchProfiles
import app.regate.search.SearchViewModel
import app.regate.search.salas.SearchSalasState
import app.regate.util.ObservableLoadingCounter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@Inject
class SearchProfilesViewModel(
    observeAuthState: ObserveAuthState,
    observeLastSearchHistory: ObserveLastSearchHistory,
    private val usersRepository: UsersRepository
) : ViewModel() {
    private val  loaderCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val filterData = MutableStateFlow(SearchViewModel.FILTER_DATA)
    val pagedList: Flow<PagingData<ProfileDto>> = Pager(PAGING_CONFIG){
        PaginationSearchProfiles(init =filterData.value.query.isNotBlank()){page->
           usersRepository.searchProfiles(filterData.value,page,20)
        }
    }.flow.cachedIn(viewModelScope)
    val state:StateFlow<SearchSalasState> = combine(
        loaderCounter.observable,
        uiMessageManager.message,
        observeAuthState.flow,
        filterData,
    ){loading,message,authState,filterData->
        SearchSalasState(
            loading = loading,
            message = message,
            authState = authState,
            filterData = filterData
        )
    }.stateIn(
        scope= viewModelScope,
        initialValue = SearchSalasState.Empty,
        started = SharingStarted.WhileSubscribed()
    )

    init {
        observeAuthState(Unit)
        observeLastSearchHistory(Unit)

        viewModelScope.launch {
        observeLastSearchHistory.flow.collectLatest { result->
            Log.d("DEBUG_APP_GRUPO",result.toString())
            val formatQuery = result.query.lowercase().trim().replace(" ",":* | ") + ":*"
            filterData.emit(filterData.value.copy(
                query = formatQuery
            ))
        }
        }
    }

    companion object {
        val PAGING_CONFIG = PagingConfig(
            pageSize = 20,
            initialLoadSize = 20,
            prefetchDistance = 5
        )
    }


}
package app.regate.search.grupos

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import app.regate.api.UiMessage
import app.regate.api.UiMessageManager
import app.regate.data.dto.ResponseMessage
import app.regate.data.dto.empresa.grupo.GrupoDto
import app.regate.data.grupo.GrupoRepository
import app.regate.domain.observers.ObserveAuthState
import app.regate.domain.observers.grupo.ObserveMyGroups
import app.regate.domain.observers.search.ObserveLastSearchHistory
import app.regate.domain.pagination.search.PaginationSearchGrupos
import app.regate.search.SearchViewModel
import app.regate.util.ObservableLoadingCounter
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
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
class SearchGroupsViewModel(
    observeAuthState: ObserveAuthState,
    observeLastSearchHistory: ObserveLastSearchHistory,
    observeMyGroups: ObserveMyGroups,
    private val grupoRepository: GrupoRepository,
) : ViewModel() {
    private val  loadingCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val filterData = MutableStateFlow(SearchViewModel.FILTER_DATA)
    val pagedList: Flow<PagingData<GrupoDto>> = Pager(PAGING_CONFIG){
        PaginationSearchGrupos(init =filterData.value.query.isNotBlank()){page->
           grupoRepository.searchGrupos(filterData.value,page,20)
//            salaRepository.filterSalas()
        }
    }.flow.cachedIn(viewModelScope)
    val state:StateFlow<SearchGruposState> = combine(
        loadingCounter.observable,
        uiMessageManager.message,
        observeAuthState.flow,
        observeMyGroups.flow,
        filterData,
    ){loading,message,authState,userGroups,filterData->
        SearchGruposState(
            loading = loading,
            message = message,
            authState = authState,
            filterData = filterData,
            userGroups = userGroups
        )
    }.stateIn(
        scope= viewModelScope,
        initialValue = SearchGruposState.Empty,
        started = SharingStarted.WhileSubscribed()
    )

    init {
        observeMyGroups(Unit)
        observeAuthState(Unit)
        observeLastSearchHistory(Unit)

        viewModelScope.launch {
        observeLastSearchHistory.flow.collectLatest { result->
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


    fun joinToGroup(groupId:Long,visibility: Int){
        viewModelScope.launch {
            try{
                loadingCounter.addLoader()
//                val visibilidad = if(visibility == GrupoVisibility.PUBLIC.ordinal) 1 else 2
                grupoRepository.joinGrupo(groupId,visibility)
//                getGrupo()
                loadingCounter.removeLoader()
//                Log.d("DEBUG_APP_ERROR",res.message)
            }catch(e: ResponseException){
                loadingCounter.removeLoader()
                uiMessageManager.emitMessage(UiMessage(message = e.response.body<ResponseMessage>().message))
                Log.d("DEBUG_APP_ERROR",e.response.body()?:"error $visibility")
            }
        }
    }

}
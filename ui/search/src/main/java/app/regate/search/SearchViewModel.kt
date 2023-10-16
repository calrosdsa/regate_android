package app.regate.search

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import app.regate.api.UiMessage
import app.regate.api.UiMessageManager
import app.regate.data.dto.ResponseMessage
import app.regate.data.dto.SearchFilterRequest
import app.regate.data.dto.account.user.ProfileDto
import app.regate.data.dto.empresa.establecimiento.EstablecimientoDto
import app.regate.data.dto.empresa.grupo.GrupoDto
import app.regate.data.establecimiento.EstablecimientoRepository
import app.regate.data.grupo.GrupoRepository
import app.regate.data.labels.SearchRepository
import app.regate.data.sala.SalaRepository
import app.regate.data.users.UsersRepository
import app.regate.domain.observers.ObserveAddressDevice
import app.regate.domain.observers.ObserveAuthState
import app.regate.domain.observers.grupo.ObserveMyGroups
import app.regate.domain.observers.search.ObserveRecentSearchHistory
import app.regate.domain.pagination.search.PaginationSearchEstablecimientos
import app.regate.extensions.combine
import app.regate.util.ObservableLoadingCounter
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class SearchViewModel(
    @Assisted savedStateHandle: SavedStateHandle,
    observeAuthState: ObserveAuthState,
    observeMyGroups:ObserveMyGroups,
    private val establecimientoRepository: EstablecimientoRepository,
    private val grupoRepository: GrupoRepository,
    private val profileRepository: UsersRepository,
    private val searchRepository:SearchRepository,
//    private val searchRepository: SearchRepository,
    observeRecentSearchHistory: ObserveRecentSearchHistory,
    observeAddressDevice: ObserveAddressDevice,
) : ViewModel() {
    private val establecimientoId = savedStateHandle.get<Long>("id")?: 0
    private val loadingCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val grupos = MutableStateFlow<List<GrupoDto>>(emptyList())
    private val profiles = MutableStateFlow<List<ProfileDto>>(emptyList())
    private val filterData = MutableStateFlow(FILTER_DATA)
    val pagedList: Flow<PagingData<EstablecimientoDto>> = Pager(PAGING_CONFIG){
        PaginationSearchEstablecimientos(loadingState = loadingCounter,init =filterData.value.query.isNotBlank() ){page->
            Log.d("DEBUG_APP_",filterData.value.toString())
            establecimientoRepository.searcEstablecimientos(filterData.value,page,15)
        }
    }.flow.cachedIn(viewModelScope)
    val state:StateFlow<SearchState> = combine(
        loadingCounter.observable,
        uiMessageManager.message,
        observeAuthState.flow,
        observeRecentSearchHistory.flow,
        grupos,
        profiles,
        observeMyGroups.flow,
        filterData,
        observeAddressDevice.flow,
    ){loading,message,authState,history,grupos,profiles,userGroups,
        filterData,addressDevice->
        SearchState(
            loading = loading,
            message = message,
            authState = authState,
            history = history,
            grupos = grupos,
            profiles = profiles,
            filterData = filterData,
            userGroups = userGroups,
            addressDevice = addressDevice

        )
    }.stateIn(
        scope= viewModelScope,
        initialValue = SearchState.Empty,
        started = SharingStarted.WhileSubscribed()
    )


    init {
        observeAuthState(Unit)
        observeRecentSearchHistory(Unit)
        observeMyGroups(Unit)
        observeAddressDevice(Unit)
        viewModelScope.launch {
            observeAddressDevice.flow.collectLatest {address->
                Log.d("DEBUG_APP_DISTANCE",address.toString())
            }
        }
    }

    companion object {
        val PAGING_CONFIG = PagingConfig(
            pageSize = 20,
            initialLoadSize = 20,
            prefetchDistance = 5
        )
        val FILTER_DATA = SearchFilterRequest()

    }

    fun search(query:String){
        viewModelScope.launch {
            try{
                addQueryToHistory(query)
                loadingCounter.addLoader()
                val formatQuery = query.lowercase().trim().replace(" ",":* | ") + ":*"
                val addressDevice = state.value.addressDevice
                val d = filterData.value.copy(
                    query = formatQuery,
                    longitud = addressDevice?.longitud?:0.0,
                    latitud = addressDevice?.latitud?:0.0,
                )
                filterData.emit(d)
//                delay(1000)
                Log.d("DEBUG_APP_SEARCH",d.query)
                val gruposData = grupoRepository.searchGrupos(d)
                Log.d("DEBUG_APP_SEARCH",gruposData.toString())
                val profilesData = profileRepository.searchProfiles(d)
                Log.d("DEBUG_APP_SEARCH",profilesData.toString())
//                delay(1000)
                grupos.emit(gruposData.results)
                profiles.emit(profilesData.results)
                loadingCounter.removeLoader()
            }catch (e:Exception){
                loadingCounter.removeLoader()
                Log.d("DEBUG_APP",e.localizedMessage?:"")
            }
        }
    }

    private fun addQueryToHistory(query: String){
        viewModelScope.launch {
            try{
                searchRepository.addSearchQueryToHistory(query)
            }catch (e:Exception){
                //TODO()
            }
        }
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

    fun getEstablecimientoId(): Long {
        return establecimientoId
    }
}
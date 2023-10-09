package app.regate.grupo.pending

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import app.regate.data.dto.SearchFilterRequest
import app.regate.data.dto.empresa.establecimiento.EstablecimientoDto
import app.regate.data.dto.empresa.grupo.PaginationPendingRequestUser
import app.regate.data.dto.empresa.grupo.PendingRequest
import app.regate.data.dto.empresa.grupo.PendingRequestUser
import app.regate.data.grupo.GrupoRepository
import app.regate.domain.observers.ObserveAuthState
import app.regate.domain.observers.ObserveUser
import app.regate.domain.pagination.PaginationPendingRequests
import app.regate.domain.pagination.search.PaginationSearchEstablecimientos
import app.regate.util.ObservableLoadingCounter
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class PendingRequestsViewModel(
    @Assisted savedStateHandle: SavedStateHandle,
    observeAuthState: ObserveAuthState,
    observeUser: ObserveUser,
    private val grupoRepository: GrupoRepository
):ViewModel() {
    private val grupoId = savedStateHandle.get<Long>("id")?:0
    val pagedList: Flow<PagingData<PendingRequestUser>> = Pager(PAGING_CONFIG){
        PaginationPendingRequests{page->
            try{
            grupoRepository.getPendingRequests(1,page)
            }catch(e:Exception){
                Log.d("DEBUG_APP",e.localizedMessage?:"")
                grupoRepository.getPendingRequests(1,page)
            }
        }
    }.flow.cachedIn(viewModelScope)
    private val confirmRequestIds = MutableStateFlow<List<Int>>(mutableListOf())
    private val declineRequestIds = MutableStateFlow<List<Int>>(mutableListOf())
    private val loadingCounter = ObservableLoadingCounter()
    val state:StateFlow<PendingRequestsState> = combine(
        loadingCounter.observable,
        observeAuthState.flow,
        observeUser.flow,
        confirmRequestIds,
        declineRequestIds
    ){loading,authState,user,confirmIds,declineIds->
        PendingRequestsState(
            loading = loading,
            appAuthState = authState,
            user = user,
            confirmRequetsIds = confirmIds,
            declineRequestIds = declineIds
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = PendingRequestsState.Empty
    )
    init {
        observeAuthState(Unit)
        observeUser(Unit)
    }

    fun declineRequest(profileId:Int){
        viewModelScope.launch {
            try{

                    val data =  PendingRequest(
                        grupo_id = grupoId,
                        profile_id = profileId.toLong()
                    )
                grupoRepository.declinePendingRequest(data)
              val ids = declineRequestIds.value.plus(profileId)
               declineRequestIds.emit(ids)
            }catch (e:Exception){
                Log.d("DEBUG_APP_RES", e.localizedMessage?:"")
            }
        }
    }

    fun confirmRequest(profileId:Int){
        viewModelScope.launch {
            try{
                    val data =  PendingRequest(
                        grupo_id = grupoId,
                        profile_id = profileId.toLong()
                    )
                    grupoRepository.confirmPendingRequest(data)
                   val ids = confirmRequestIds.value.plus(profileId)
                  confirmRequestIds.emit(ids)
            }catch (e:ResponseException){
              Log.d("DEBUG_APP_RES",e.response.bodyAsText())
            } catch (e:Exception){
                Log.d("DEBUG_APP_RES", e.localizedMessage?:"")
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
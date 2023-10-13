package app.regate.grupos.user_requests

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import app.regate.data.dto.empresa.grupo.PendingRequest
import app.regate.data.dto.empresa.grupo.UserGrupoRequestDto
import app.regate.data.grupo.GrupoRepository
import app.regate.domain.observers.ObserveAuthState
import app.regate.domain.observers.ObserveUser
import app.regate.domain.pagination.grupo.PaginationUserGrupoRequests
import app.regate.util.ObservableLoadingCounter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@Inject
class UserGrupoRequestsViewModel(
//    @Assisted savedStateHandle: SavedStateHandle,
    observeAuthState: ObserveAuthState,
    observeUser: ObserveUser,
    private val grupoRepository: GrupoRepository
):ViewModel() {
//    private val grupoId = savedStateHandle.get<Long>("id")?:0
    val pagedList: Flow<PagingData<UserGrupoRequestDto>> = Pager(PAGING_CONFIG){
        PaginationUserGrupoRequests{page->
            grupoRepository.getUserRequest(page)
        }
    }.flow.cachedIn(viewModelScope)
    private val confirmRequestIds = MutableStateFlow<List<Int>>(mutableListOf())
    private val declineRequestIds = MutableStateFlow<List<Int>>(mutableListOf())
    private val loadingCounter = ObservableLoadingCounter()
    val state:StateFlow<UserGrupoRequestsState> = combine(
        loadingCounter.observable,
        observeAuthState.flow,
        observeUser.flow,
        confirmRequestIds,
        declineRequestIds
    ){loading,authState,user,confirmIds,declineIds->
        UserGrupoRequestsState(
            loading = loading,
            appAuthState = authState,
            user = user,
            confirmRequetsIds = confirmIds,
            declineRequestIds = declineIds
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = UserGrupoRequestsState.Empty
    )
    init {
        observeAuthState(Unit)
        observeUser(Unit)
    }

    fun cancelRequest(profileId:Int, groupId:Int){
        viewModelScope.launch {
            try{
                    val data =  PendingRequest(
                        grupo_id = groupId.toLong(),
                        profile_id = profileId.toLong()
                    )
                grupoRepository.cancelPendingRequest(data)
              val ids = declineRequestIds.value.plus(groupId)
               declineRequestIds.emit(ids)
            }catch (e:Exception){
                Log.d("DEBUG_APP_RES", e.localizedMessage?:"")
            }
        }
    }

//    fun confirmRequest(profileId:Int){
//        viewModelScope.launch {
//            try{
//                    val data =  PendingRequest(
//                        grupo_id = grupoId,
//                        profile_id = profileId.toLong()
//                    )
//                    grupoRepository.confirmPendingRequest(data)
//                   val ids = confirmRequestIds.value.plus(profileId)
//                  confirmRequestIds.emit(ids)
//            }catch (e:ResponseException){
//              Log.d("DEBUG_APP_RES",e.response.bodyAsText())
//            } catch (e:Exception){
//                Log.d("DEBUG_APP_RES", e.localizedMessage?:"")
//            }
//        }
//    }

    companion object {
        val PAGING_CONFIG = PagingConfig(
            pageSize = 20,
            initialLoadSize = 20,
            prefetchDistance = 5
        )
    }
}
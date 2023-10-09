package app.regate.grupo.info

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessage
import app.regate.data.dto.ResponseMessage
import app.regate.data.dto.empresa.grupo.GrupoDto
import app.regate.data.dto.empresa.grupo.GrupoVisibility
import app.regate.data.dto.empresa.grupo.PendingRequest
import app.regate.data.grupo.GrupoRepository
import app.regate.domain.observers.ObserveAuthState
import app.regate.domain.observers.ObserveUser
import app.regate.domain.observers.grupo.ObserveMyGroupById
import app.regate.domain.observers.grupo.ObserveMyGroups
import app.regate.util.ObservableLoadingCounter
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch


@Inject
class InfoGrupoViewModel(
    @Assisted savedStateHandle: SavedStateHandle,
    private val grupoRepository: GrupoRepository,
    observeMyGroupById: ObserveMyGroupById,
    observeUser: ObserveUser,
    observeAuthState: ObserveAuthState
):ViewModel() {
    private val grupoId  = savedStateHandle.get<Long>("id")?:0
    private val loadingCounter = ObservableLoadingCounter()
    private val grupo = MutableStateFlow<GrupoDto?>(null)
    val state:StateFlow<InfoGrupoState> = combine(
        loadingCounter.observable,
        grupo,
        observeMyGroupById.flow,
        observeUser.flow,
        observeAuthState.flow
    ){loading,grupo,myGroup,user,authState->
        InfoGrupoState(
            loading = loading,
            grupo = grupo,
            myGroup = myGroup,
            authState = authState,
            user = user
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = InfoGrupoState.Empty
    )
    init {
        getData()
        observeMyGroupById(ObserveMyGroupById.Params(grupoId))
        observeAuthState(Unit)
        observeUser(Unit)
        viewModelScope.launch {
            observeMyGroupById.flow.collectLatest {
                try {
                Log.d("DEBUG_APP",it.toString())
                }catch (e:Exception){
                    Log.d("DEBUG_APP",e.localizedMessage?:"")
                }
            }
        }
    }
    fun getData() {
        viewModelScope.launch {
            try {
                loadingCounter.addLoader()
                val res = grupoRepository.getGrupo(grupoId)
                Log.d("DEBUG_APP_RES",res.toString())
                delay(1000)
                grupo.emit(res)
                loadingCounter.removeLoader()
            } catch (e: Exception) {
                loadingCounter.removeLoader()
                Log.d("DEBUG_APP",e.localizedMessage?:"")
            }
        }
    }
    fun joinToGroup(visibility: Int){
        viewModelScope.launch {
            try{
                loadingCounter.addLoader()
//                val visibilidad = if(visibility == GrupoVisibility.PUBLIC.ordinal) 1 else 2
                grupoRepository.joinGrupo(grupoId,visibility)
//                getGrupo()
                loadingCounter.removeLoader()
//                Log.d("DEBUG_APP_ERROR",res.message)
            }catch(e: ResponseException){
                loadingCounter.removeLoader()
                Log.d("DEBUG_APP_ERROR",e.response.body()?:"error $visibility")
            }
        }
    }

    fun cancelRequest(){
        viewModelScope.launch {
            try{
               state.value.user?.let {user ->
                val pendingRequest = PendingRequest(
                    profile_id = user.profile_id,
                    grupo_id = grupoId
                )
                grupoRepository.declinePendingRequest(pendingRequest)
               }
            }catch(e:Exception){
                Log.d("DEBUG_APP",e.localizedMessage?:"")
            }
        }
    }
}
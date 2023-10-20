package app.regate.grupo.invitation

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.data.dto.empresa.grupo.GrupoDto
import app.regate.data.dto.empresa.grupo.PendingRequest
import app.regate.data.grupo.GrupoRepository
import app.regate.domain.observers.ObserveAuthState
import app.regate.domain.observers.account.ObserveUser
import app.regate.domain.observers.grupo.ObserveMyGroupById
import app.regate.util.ObservableLoadingCounter
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch


@Inject
class InvitationGrupoViewModel(
    @Assisted savedStateHandle: SavedStateHandle,
    private val grupoRepository: GrupoRepository,
    private val observeMyGroupById: ObserveMyGroupById,
    observeUser: ObserveUser,
    observeAuthState: ObserveAuthState
):ViewModel() {
    private val uuid  = savedStateHandle.get<String>("uuid")?:""
    val loadingCounter = ObservableLoadingCounter()
    private val grupo = MutableStateFlow<GrupoDto?>(null)
    val state:StateFlow<InvitationGrupoState> = combine(
        grupo,
        observeMyGroupById.flow,
        observeUser.flow,
        observeAuthState.flow
    ){grupo,myGroup,user,authState->
        InvitationGrupoState(
            grupo = grupo,
            myGroup = myGroup,
            authState = authState,
            user = user
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = InvitationGrupoState.Empty
    )
    init {
        getData()
        observeAuthState(Unit)
        observeUser(Unit)
//        viewModelScope.launch {
//            observeMyGroupById.flow.collectLatest {
//                try {
//                Log.d("DEBUG_APP",it.toString())
//                }catch (e:Exception){
//                    Log.d("DEBUG_APP",e.localizedMessage?:"")
//                }
//            }
//        }
    }
    fun getData() {
        viewModelScope.launch {
            try {
                loadingCounter.addLoader()
                val res = grupoRepository.getGrupoByIdLink(uuid)
                observeMyGroupById(ObserveMyGroupById.Params(res.id))
                Log.d("DEBUG_APP_RES",res.toString())
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
                state.value.grupo?.id?.let {grupoId ->
                loadingCounter.addLoader()
//                val visibilidad = if(visibility == GrupoVisibility.PUBLIC.ordinal) 1 else 2
                grupoRepository.joinGrupo(grupoId,visibility)
//                getGrupo()
                loadingCounter.removeLoader()
                }
//                Log.d("DEBUG_APP_ERROR",res.message)
            }catch(e: ResponseException){
                loadingCounter.removeLoader()
                Log.d("DEBUG_APP_ERROR",e.response.body()?:"error $visibility")
            }
        }
    }

    fun cancelRequest(grupoId:Long){
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
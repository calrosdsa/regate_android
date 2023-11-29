package app.regate.grupo.info

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.data.chat.ChatRepository
import app.regate.data.dto.chat.NotifyNewUserRequest
import app.regate.data.dto.chat.TypeChat
import app.regate.data.dto.empresa.grupo.GrupoDto
import app.regate.data.dto.empresa.grupo.PendingRequest
import app.regate.data.grupo.GrupoRepository
import app.regate.domain.observers.ObserveAuthState
import app.regate.domain.observers.account.ObserveUser
import app.regate.domain.observers.grupo.ObserveMyGroupById
import app.regate.grupo.invitation.InvitationGrupoState
import app.regate.util.ObservableLoadingCounter
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch


@Inject
class InfoGrupoViewModel(
    @Assisted savedStateHandle: SavedStateHandle,
    private val grupoRepository: GrupoRepository,
    observeMyGroupById: ObserveMyGroupById,
    observeUser: ObserveUser,
    observeAuthState: ObserveAuthState,
    private val chatRepository: ChatRepository
):ViewModel() {
    private val groupId  = savedStateHandle.get<Long>("id")?:0
    private val loadingCounter = ObservableLoadingCounter()
    private val grupo = MutableStateFlow<GrupoDto?>(null)
    val state:StateFlow<InvitationGrupoState> = combine(
        loadingCounter.observable,
        grupo,
        observeMyGroupById.flow,
        observeUser.flow,
        observeAuthState.flow
    ){loading,grupo,myGroup,user,authState->
        InvitationGrupoState(
            loading = loading,
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
        observeMyGroupById(ObserveMyGroupById.Params(groupId))
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
                val res = grupoRepository.getGrupo(groupId)
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
                loadingCounter.addLoader()
//                val visibilidad = if(visibility == GrupoVisibility.PUBLIC.ordinal) 1 else 2
                val res = grupoRepository.joinGrupo(groupId,visibility,grupo.value)
                if (res != null) {
                    val notifyNewUserRequest = NotifyNewUserRequest(
                        id = res.id,
                        profileId = state.value.user?.profile_id ?: 0,
                        parentId = groupId,
                        type_chat = TypeChat.TYPE_CHAT_GRUPO.ordinal
                    )
                    Log.d("DEBUG_APP_DATA", notifyNewUserRequest.toString())
                    chatRepository.notifyNewUser(notifyNewUserRequest)
                }
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
                    grupo_id = groupId
                )
                grupoRepository.declinePendingRequest(pendingRequest)
               }
            }catch(e:Exception){
                Log.d("DEBUG_APP",e.localizedMessage?:"")
            }
        }
    }
}
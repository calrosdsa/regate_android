package app.regate.grupo.invitationlink

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessage
import app.regate.api.UiMessageManager
import app.regate.data.dto.empresa.grupo.setting.GrupoInvitationLinkDto
import app.regate.data.grupo.GrupoRepository
import app.regate.domain.observers.ObserveAuthState
import app.regate.domain.observers.account.ObserveUser
import app.regate.util.ObservableLoadingCounter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch


@Inject
class InvitationLinkViewModel(
    @Assisted savedStateHandle: SavedStateHandle,
    private val grupoRepository: GrupoRepository,
    observeUser: ObserveUser,
    observeAuthState: ObserveAuthState
):ViewModel() {
    private val id  = savedStateHandle.get<Long>("id")?:0
    private val loadingCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val invitationLink = MutableStateFlow<GrupoInvitationLinkDto?>(null)
    val state:StateFlow<InvitationLinkState> = combine(
        loadingCounter.observable,
        uiMessageManager.message,
        invitationLink,
        observeUser.flow,
        observeAuthState.flow
    ){loading,message,invitationLink,user,authState->
        InvitationLinkState(
            authState = authState,
            user = user,
            invitationLink = invitationLink,
            loading = loading,
            message = message
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = InvitationLinkState.Empty
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
                val res = grupoRepository.getOrInsertInvitationLink(id)
                Log.d("DEBUG_APP_RES",res.toString())
                invitationLink.emit(res)
                loadingCounter.removeLoader()
            } catch (e: Exception) {
                loadingCounter.removeLoader()
                Log.d("DEBUG_APP_ERROR",e.localizedMessage?:"")
            }
        }
    }

    fun resetInvitationLink(){
        viewModelScope.launch {
            try {
                loadingCounter.addLoader()
                val res = grupoRepository.resetInvitationLink(id)
//                delay(2000)
                Log.d("DEBUG_APP_RES",res.toString())
                invitationLink.emit(res)
                loadingCounter.removeLoader()
                uiMessageManager.emitMessage(UiMessage(message =
                "El enlace de invitación fue restablecido y se generó uno nuevo."))
            } catch (e: Exception) {
                loadingCounter.removeLoader()
                Log.d("DEBUG_APP_ERROR",e.localizedMessage?:"")
            }
        }
    }

    fun clearMessage(id:Long){
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }

}
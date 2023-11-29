package app.regate.grupos.userinvitations

import GrupoInvitationRequest
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import app.regate.api.UiMessageManager
import app.regate.data.grupo.GrupoRepository
import app.regate.domain.observers.account.ObserveUser
import app.regate.domain.observers.grupo.ObservePagerUserInvitations
import app.regate.util.ObservableLoadingCounter
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@Inject
class UserInvitationsViewModel(
    pagingInteractor:ObservePagerUserInvitations,
    observeUser: ObserveUser,
    private val grupoRepository: GrupoRepository,
):ViewModel() {
    val pagedList = pagingInteractor.flow.cachedIn(viewModelScope)
    private val loadingCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    val state:StateFlow<UserInvitationsState> = combine(
        loadingCounter.observable,
        uiMessageManager.message
    ){loading,message->
        UserInvitationsState(
            loading = loading,
            message = message
        )
    }.stateIn(
        scope = viewModelScope,
        initialValue = UserInvitationsState(),
        started = SharingStarted.WhileSubscribed()
    )

    init {
        observeUser(Unit)
        viewModelScope.launch {
            observeUser.flow.collectLatest {user->
                try{
                    pagingInteractor(ObservePagerUserInvitations.Params(
                        pagingConfig = PAGING_CONFIG,
                        profileId = user.profile_id
                    ))
                }catch (e:Exception){
                    Log.d("DEBUG_APP_",e.localizedMessage?:"")
                }
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
    fun clearMessage(id:Long){
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }

    fun declineInvitation(profileId:Long,grupoId:Long){
        viewModelScope.launch {
            try{
                val request1 = GrupoInvitationRequest(
                    profile_id = profileId,
                    grupo_id = grupoId
                )
                grupoRepository.declineGrupoInvitation(request1)
            }catch (e:Exception){
                Log.d("DEBUG_APP_",e.localizedMessage?:"")
            }
        }
    }

    fun acceptInvitation(profileId:Long,grupoId:Long){
        viewModelScope.launch {
            try{
                val request = GrupoInvitationRequest(
                    profile_id = profileId,
                    grupo_id = grupoId
                )
                grupoRepository.acceptGrupoInvition(request)
            }catch (e:Exception){
                Log.d("DEBUG_APP_",e.localizedMessage?:"")
            }
        }
    }



}
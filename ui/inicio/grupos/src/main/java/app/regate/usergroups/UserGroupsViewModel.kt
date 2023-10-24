package app.regate.usergroups

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import app.cash.paging.cachedIn
import app.regate.api.UiMessageManager
import app.regate.compoundmodels.MessageProfile
import app.regate.data.chat.ConversationRepository
import app.regate.data.grupo.GrupoRepository
import app.regate.domain.observers.ObserveAuthState
import app.regate.domain.observers.chat.ObservePagerChat
import app.regate.domain.observers.grupo.ObserveUserGroupsWithMessage
import app.regate.domain.observers.pagination.ObservePagerMessages
import app.regate.models.chat.Chat
import app.regate.util.ObservableLoadingCounter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@Inject
class UserGroupsViewModel(
    observeGrupos: ObserveUserGroupsWithMessage,
    private val grupoRepository: GrupoRepository,
    private val conversationRepository: ConversationRepository,
    observeAuthState: ObserveAuthState,
    pagingInteractor: ObservePagerChat,
//    private val updateFilterGrupos: UpdateFilterGrupos
):ViewModel() {
    private val loadingCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    val pagedList: Flow<PagingData<Chat>> =
        pagingInteractor.flow.cachedIn(viewModelScope)
    val state:StateFlow<UserGroupsState> = combine(
        observeGrupos.flow,
        loadingCounter.observable,
        uiMessageManager.message,
        observeAuthState.flow
    ){grupos,loading,message,authState->
        UserGroupsState(
            loading = loading,
            message = message,
            grupos = grupos,
            authState = authState
        )
    }.stateIn(
        scope = viewModelScope,
        initialValue = UserGroupsState.Empty,
        started = SharingStarted.WhileSubscribed()
    )

    init {
        pagingInteractor(ObservePagerChat.Params(PAGING_CONFIG))
        observeAuthState(Unit)
        observeGrupos(Unit)
        getUserGrupos()
        viewModelScope.launch {
            observeGrupos.flow.collectLatest{
                Log.d("DEBUG_APP_G",it.toString())
            }
        }
    }
    companion object {
        val PAGING_CONFIG = PagingConfig(
            pageSize = 20,
            initialLoadSize = 20,
        )
    }

    fun getUserGrupos(){
        viewModelScope.launch {
            try{
                conversationRepository.getUnreadMessages(1)
            }catch(e:Exception){
                Log.d("DEBUG_APP_!21",e.localizedMessage?:"")
            }
        }
    }
    }
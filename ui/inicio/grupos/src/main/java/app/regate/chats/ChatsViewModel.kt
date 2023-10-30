package app.regate.chats

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import app.cash.paging.cachedIn
import app.regate.api.UiMessageManager
import app.regate.data.chat.ChatRepository
import app.regate.domain.observers.ObserveAuthState
import app.regate.domain.observers.chat.ObservePagerChat
import app.regate.models.chat.Chat
import app.regate.util.ObservableLoadingCounter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@Inject
class ChatsViewModel(
//    private val grupoRepository: GrupoRepository,
    private val chatRepository: ChatRepository,
    observeAuthState: ObserveAuthState,
    pagingInteractor: ObservePagerChat,
//    private val updateFilterGrupos: UpdateFilterGrupos
):ViewModel() {
    private val loadingCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    val pagedList: Flow<PagingData<Chat>> =
        pagingInteractor.flow.cachedIn(viewModelScope)
    val state:StateFlow<ChatsState> = combine(
        loadingCounter.observable,
        uiMessageManager.message,
        observeAuthState.flow
    ){loading,message,authState->
        ChatsState(
            loading = loading,
            message = message,
            authState = authState
        )
    }.stateIn(
        scope = viewModelScope,
        initialValue = ChatsState.Empty,
        started = SharingStarted.WhileSubscribed()
    )

    init {
        pagingInteractor(ObservePagerChat.Params(PAGING_CONFIG))
        observeAuthState(Unit)
        getUserGrupos()
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
                chatRepository.getUnreadMessages(1)
            }catch(e:Exception){
                Log.d("DEBUG_APP_!21",e.localizedMessage?:"")
            }
        }
    }
    }
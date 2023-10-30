package app.regate.chats.mychats

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import app.cash.paging.cachedIn
import app.regate.api.UiMessageManager
import app.regate.data.dto.empresa.grupo.GrupoMessageData
import app.regate.data.dto.empresa.grupo.GrupoMessageDto
import app.regate.data.grupo.GrupoRepository
import app.regate.domain.observers.account.ObserveUser
import app.regate.domain.observers.chat.ObservePagerChat
import app.regate.domain.observers.grupo.ObserveUserGroups
import app.regate.models.chat.Chat
import app.regate.util.ObservableLoadingCounter
import app.regate.util.getLongUuid
import app.regate.util.now
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class MyChatsViewModel(
    @Assisted savedStateHandle: SavedStateHandle,
    observeGrupos: ObserveUserGroups,
    observeUser: ObserveUser,
    private val grupoRepository: GrupoRepository,
    pagingInteractor: ObservePagerChat,
//    private val updateFilterGrupos: UpdateFilterGrupos
):ViewModel() {
    private val data = savedStateHandle.get<String>("data") ?: ""
    private val loadingCounter = ObservableLoadingCounter()
    private val selectedChats = MutableStateFlow<List<Chat>>(emptyList())
    private val uiMessageManager = UiMessageManager()
    val pagedList: Flow<PagingData<Chat>> =
        pagingInteractor.flow.cachedIn(viewModelScope)
    val state: StateFlow<MyChatsState> = combine(
        observeGrupos.flow,
        loadingCounter.observable,
        uiMessageManager.message,
        selectedChats,
        observeUser.flow
    ) { grupos, loading, message,selectedGroups,user ->
        MyChatsState(
            loading = loading,
            message = message,
            grupos = grupos,
            selectedChats = selectedGroups,
            user = user
        )
    }.stateIn(
        scope = viewModelScope,
        initialValue = MyChatsState.Empty,
        started = SharingStarted.WhileSubscribed()
    )

    init {
        pagingInteractor(ObservePagerChat.Params(PAGING_CONFIG))
        observeGrupos(Unit)
        getUserGrupos()
        observeUser(Unit)
    }

    companion object {
        val PAGING_CONFIG = PagingConfig(
            pageSize = 20,
            initialLoadSize = 20,
        )
    }

    fun selectChat(d:Chat) {
        viewModelScope.launch {
            try {
                if (selectedChats.value.map { it.id }.contains(d.id)) {
                    val newList = selectedChats.value.filter { it.id != d.id }
                    selectedChats.emit(newList)
                } else {
                        val items = selectedChats.value + d
                        selectedChats.emit(items)
                }
            } catch (e: Exception) {
                Log.d("DEBUG_APP", e.localizedMessage ?: "")
            }
        }
    }
    fun getUserGrupos() {
        viewModelScope.launch {
            try {
                grupoRepository.myGroups()
            } catch (e: Exception) {
                Log.d("DEBUG_APP_!21", e.localizedMessage ?: "")
            }
        }
    }

    fun navigateToGroupChat(groupId: Long,context:Context,content:String,navigate: (Long, String) -> Unit,
    navigateUp:()->Unit) {
        try {
            if (data.isNotBlank()) {
                val grupoMessageData = Json.decodeFromString<GrupoMessageData>(data)
                val newData = Json.encodeToString(grupoMessageData.copy(content = content))
                if (selectedChats.value.size > 1) {
                    val messages = mutableListOf<GrupoMessageDto>()
                    selectedChats.value.map { grupo->
                        val message = state.value.user?.let {
                            GrupoMessageDto(
                                id = getLongUuid(),
                                content = content,
                                data = grupoMessageData.data,
                                type_message = grupoMessageData.type_data,
                                created_at = now(),
                                chat_id = grupo.id,
                                profile_id = it.profile_id
                            )
                        }
                        if (message != null) {
                            messages.add(message)
                        }
                    }
                    viewModelScope.launch {
                        try {
                            grupoRepository.sendShareMessage(messages)
                            Toast.makeText(context,"Mensages enviados...",Toast.LENGTH_SHORT).show()
                            delay(2000)
                            navigateUp()
                        } catch (e: Exception) {
                            Log.d("DEBUG_APP_ERROR",e.localizedMessage?:"")
                        }
                    }
                } else {
                    navigate(groupId, newData)
                }
            }
        } catch (e: Exception) {
            Log.d("DEBUG_APP", e.localizedMessage ?: "")
        }
    }
    fun getData():String{
        return data
    }

}
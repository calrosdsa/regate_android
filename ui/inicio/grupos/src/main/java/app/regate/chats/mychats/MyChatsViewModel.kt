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
import app.regate.data.chat.ChatRepository
import app.regate.data.dto.chat.MessagePublishRequest
import app.regate.data.dto.chat.TypeChat
import app.regate.data.dto.empresa.grupo.GrupoMessageData
import app.regate.data.dto.empresa.grupo.GrupoMessageDto
import app.regate.data.grupo.GrupoRepository
import app.regate.data.mappers.MessageDtoToMessage
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
    private val chatRepository: ChatRepository,
    private val messageDtoToMessage: MessageDtoToMessage,
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

    fun navigateToGroupChat(groupId: Long,context:Context,content:String,parentId:Long,
    typeChat: Int,navigate: (Long,Long,String,Int) -> Unit) {
        viewModelScope.launch {
            try {
                if (data.isNotBlank()) {
                    val grupoMessageData = Json.decodeFromString<GrupoMessageData>(data)
                    val newData = Json.encodeToString(grupoMessageData.copy(content = content))
                    if (selectedChats.value.size > 1) {
                        selectedChats.value.map { chat ->
                            val message = state.value.user?.let {
                                GrupoMessageDto(
                                    local_id = getLongUuid(),
                                    content = content,
                                    data = grupoMessageData.data,
                                    type_message = grupoMessageData.type_data,
                                    created_at = now(),
                                    chat_id = chat.id,
                                    profile_id = it.profile_id,
                                    parent_id = chat.parent_id,
                                    is_user = typeChat == TypeChat.TYPE_CHAT_INBOX_ESTABLECIMIENTO.ordinal
                                )
                            }
                            if (message != null) {
                                val messagePublish = MessagePublishRequest(
                                    message = message,
                                    type_chat = chat.type_chat,
                                    chat_id = chat.id
                                )
                                chatRepository.saveMessageLocal(messageDtoToMessage.map(message).copy(readed = true, id = message.local_id))
                                chatRepository.publishSharedMessage(messagePublish)
                            }
                        }
                        Toast.makeText(context, "Mensages enviados...", Toast.LENGTH_SHORT)
                            .show()
                        selectedChats.emit(emptyList())
                    } else {
                        Log.d("DEBUG_APP_DATA",newData)
                        navigate(groupId,parentId,newData,typeChat)
                    }
                }
            } catch (e: Exception) {
                Log.d("DEBUG_APP", e.localizedMessage ?: "")
            }
        }
    }
    fun getData():String{
        return data
    }

}
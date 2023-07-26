package app.regate.conversation

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import app.cash.paging.cachedIn
import app.regate.api.UiMessageManager
import app.regate.compoundmodels.MessageProfile
import app.regate.data.coin.ConversationRepository
import app.regate.data.common.MessageData
import app.regate.data.dto.empresa.conversation.ConversationMessage
import app.regate.data.dto.empresa.grupo.GrupoMessageDto
import app.regate.domain.observers.ObservePagerMessages
import app.regate.domain.observers.ObservePagerMessagesInbox
import app.regate.domain.observers.ObserveUser
import app.regate.inbox.ConversationsState
import app.regate.models.MessageInbox
import app.regate.util.ObservableLoadingCounter
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.HttpMethod
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import java.util.UUID

@Inject
class ConversationViewModel(
    @Assisted savedStateHandle: SavedStateHandle,
    private val client: HttpClient,
    private val conversationRepository: ConversationRepository,
    observeUser: ObserveUser,
    pagingInteractor:ObservePagerMessagesInbox
    ):ViewModel() {
    private val conversationId = savedStateHandle.get<Long>("id")?:0
    private val loadingCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val messageInbox = MutableStateFlow<MessageInbox?>(null)

    val pagedList: Flow<PagingData<MessageInbox>> =
        pagingInteractor.flow.cachedIn(viewModelScope)
    val state:StateFlow<ConversationsState>  = combine(
        loadingCounter.observable,
        uiMessageManager.message,
        observeUser.flow,
        messageInbox
    ){loading,message,user,messageInbox->
        ConversationsState(
            loading = loading,
            message = message,
            user = user,
            messageInbox = messageInbox
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ConversationState.Empty
    )
    init{
        syncMessages()
        observeUser(Unit)
        pagingInteractor(ObservePagerMessagesInbox.Params(PAGING_CONFIG,conversationId))
        Log.d("DEBUG_APP",conversationId.toString())
        viewModelScope.launch {
            try{
                client.webSocket(method = HttpMethod.Get, host = "172.20.20.76",
                port = 9091, path = "v1/ws/conversation/?id=$conversationId"){
                    launch { outputMessage() }
                    while (true){
                        val othersMessage = incoming.receive() as? Frame.Text
                        if (othersMessage != null) {
                            val data = Json.decodeFromString<ConversationMessage>(othersMessage.readText())
                            try{
                                conversationRepository.saveMessage(data)
                            }catch (e:Exception){
                                Log.d("DEBUG_APP_ERROR_",e.localizedMessage?:"")
                            }
                        }
                    }
                }
            }catch(e:Exception){
                Log.d("DEBUG_APP",e.localizedMessage?:"")
            }
        }
    }
    private fun syncMessages(){
        viewModelScope.launch {
            conversationRepository.syncMessages(conversationId)
        }
    }
    private suspend fun DefaultClientWebSocketSession.outputMessage(){
        messageInbox.filterNotNull().collectLatest {data->
            if(data.content != ""){
                state.value.user?.let {
                    val message = ConversationMessage(
                        id = data.id,
                        conversation_id = data.conversation_id,
                        sender_id = data.sender_id,
                        reply_to = data.reply_to,
                        content = data.content,
                        created_at = data.created_at
                    )
                    send(Json.encodeToString(message))
                }
            }
        }
    }

    fun sendMessage(message:MessageData){
        viewModelScope.launch {
            try{
                val data = MessageInbox(
                    content = message.content,
                    reply_to = message.reply_to,
                    sender_id = state.value.user?.profile_id?:0,
                    created_at = Clock.System.now(),
                    conversation_id = conversationId,
                    id = UUID.randomUUID().mostSignificantBits and Long.MAX_VALUE
                )
                conversationRepository.saveMessageLocal(data)
                messageInbox.tryEmit(data)
            }catch (e:Exception){
              Log.d("DEBUG_APP",message.toString())
            }
        }
    }

    companion object {
        val PAGING_CONFIG = PagingConfig(
            pageSize = 20,
            initialLoadSize = 20,
        )
    }
}
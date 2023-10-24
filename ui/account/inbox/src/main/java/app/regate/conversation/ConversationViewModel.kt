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
import app.regate.constant.HostMessage
import app.regate.data.chat.ChatRepository
import app.regate.data.common.MessageData
import app.regate.data.dto.empresa.conversation.ConversationMessage
import app.regate.data.dto.empresa.grupo.CupoInstalacion
import app.regate.data.instalacion.CupoRepository
import app.regate.domain.interactors.UpdateEstablecimiento
import app.regate.domain.observers.establecimiento.ObserveEstablecimientoDetail
import app.regate.domain.observers.account.ObserveUser
import app.regate.domain.observers.pagination.ObservePagerMessages
import app.regate.models.Message
import app.regate.models.MessageInbox
import app.regate.util.ObservableLoadingCounter
import app.regate.util.getLongUuid
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.HttpMethod
import io.ktor.websocket.Frame
import io.ktor.websocket.send
import kotlinx.coroutines.async
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
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class ConversationViewModel(
    @Assisted savedStateHandle: SavedStateHandle,
    private val client: HttpClient,
    private val chatRepository: ChatRepository,
    private val updateEstablecimiento: UpdateEstablecimiento,
    private val cupoRepository: CupoRepository,
    observeUser: ObserveUser,
    observeEstablecimientoDetail: ObserveEstablecimientoDetail,
//    pagingInteractor: ObservePagerMessagesInbox,
    pagingInteractor: ObservePagerMessages,
    ):ViewModel() {
    private val chatId = savedStateHandle.get<Long>("id")?:0
    private val establecimientoId = savedStateHandle.get<Long>("establecimientoId")?:0
    private val loadingCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val messageInbox = MutableStateFlow<MessageInbox?>(null)
    val pagedList: Flow<PagingData<MessageProfile>> =
        pagingInteractor.flow.cachedIn(viewModelScope)
    val state:StateFlow<ConversationState>  = combine(
        loadingCounter.observable,
        uiMessageManager.message,
        observeUser.flow,
        messageInbox,
        observeEstablecimientoDetail.flow
    ){loading,message,user,messageInbox,establecimiento->
        ConversationState(
            loading = loading,
            message = message,
            user = user,
            messageInbox = messageInbox,
            establecimiento = establecimiento
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ConversationState.Empty
    )
    init {
//        pagingInteractor(ObservePagerMessagesInbox.Params(PAGING_CONFIG,conversationId))
        pagingInteractor(ObservePagerMessages.Params(PAGING_CONFIG,chatId))

        viewModelScope.launch {
            updateEstablecimiento.executeSync(UpdateEstablecimiento.Params(
                id = establecimientoId
            ))
        }
        syncMessages()
        observeUser(Unit)
        observeEstablecimientoDetail(ObserveEstablecimientoDetail.Params(id = establecimientoId))
        Log.d("DEBUG_APP",chatId.toString())
        viewModelScope.launch {
            try{
//                client.webSocket(method = HttpMethod.Get, host = "172.20.20.76",
                client.webSocket(method = HttpMethod.Get, host = HostMessage.host,
                    port = HostMessage.port, path = "v1/ws/conversation/?id=$chatId"){
                    launch { outputMessage() }
                    while (true){
                        val othersMessage = incoming.receive() as? Frame.Text
                        if (othersMessage != null) {
//                            val data = Json.decodeFromString<ConversationMessage>(othersMessage.readText())
                            try{
//                                conversationRepository.saveMessage(data)
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
            chatRepository.syncMessages(chatId)
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

    fun sendMessage(messageData: MessageData,animateScroll:()->Unit){
        viewModelScope.launch {
            val message =  Message(
                content = messageData.content,
                reply_to = messageData.reply_to,
                profile_id = state.value.user?.profile_id?:0,
                created_at = Clock.System.now(),
                chat_id = chatId,
                id = getLongUuid(),
                type_message = messageData.type_message,
                data = messageData.data,
                readed = true
            )
            val res = async { chatRepository.saveMessageLocal(message) }
            res.await()
            animateScroll()
//            messageChat.tryEmit(message)
        }
    }

    fun navigateToInstalacionReserva(instalacionId:Long, establecimientoId:Long, cupos:List<CupoInstalacion>,
                                     navigate:(Long,Long)->Unit){
        viewModelScope.launch {
            try{
                val res = async { cupoRepository.insertDeleteCupos(cupos,instalacionId) }
                res.await()
                navigate(instalacionId,establecimientoId)
            }catch(e:Exception){
                //TODO()
            }
        }
    }

    companion object {
        val PAGING_CONFIG = PagingConfig(
            pageSize = 20,
            initialLoadSize = 20,
//            prefetchDistance = 1
        )
    }

//    companion object {
//        val PAGING_CONFIG = PagingConfig(
//            pageSize = 20,
//            initialLoadSize = 20,
//            prefetchDistance = 5
//        )
//    }
}
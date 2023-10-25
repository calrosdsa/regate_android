package app.regate.chat.sala

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.paging.cachedIn
import app.regate.api.UiMessageManager
import app.regate.compoundmodels.MessageProfile
import app.regate.compoundmodels.UserProfileGrupoAndSala
import app.regate.constant.HostMessage
import app.regate.data.app.EmojiCategory
import app.regate.data.chat.ChatRepository
import app.regate.data.common.MessageData
import app.regate.data.dto.chat.MessageEvent
import app.regate.data.dto.chat.MessageEventType
import app.regate.data.dto.chat.MessagePublishRequest
import app.regate.data.dto.chat.TypeChat
import app.regate.data.dto.empresa.grupo.CupoInstalacion
import app.regate.data.dto.empresa.grupo.GrupoMessageData
import app.regate.data.dto.empresa.grupo.GrupoMessageDto
import app.regate.data.instalacion.CupoRepository
import app.regate.data.labels.LabelRepository
import app.regate.domain.observers.ObserveAuthState
import app.regate.domain.observers.ObserveUsersSala
import app.regate.domain.observers.account.ObserveUser
import app.regate.domain.observers.chat.ObserveChat
import app.regate.domain.observers.pagination.ObservePagerMessages
import app.regate.extensions.combine
import app.regate.models.Emoji
import app.regate.models.Message
import app.regate.settings.AppPreferences
import app.regate.util.ObservableLoadingCounter
import app.regate.util.getLongUuid
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.http.HttpMethod
import io.ktor.util.InternalAPI
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class ChatSalaViewModel(
    @Assisted savedStateHandle: SavedStateHandle,
    private val client: HttpClient,
    private val chatRepository: ChatRepository,
    private val cupoRepository: CupoRepository,
    private val preferences: AppPreferences,
    private val labelRepository: LabelRepository,
    observeUser: ObserveUser,
    observeAuthState: ObserveAuthState,
    pagingInteractor: ObservePagerMessages,
    observeChat:ObserveChat,
    observeUsersSala: ObserveUsersSala
    ):ViewModel() {
    private val chatId = savedStateHandle.get<Long>("id") ?: 0
    private val salaId = savedStateHandle.get<Long>("salaId") ?: 0
    private var data = savedStateHandle.get<String>("data")?:""
    private val loadingCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val scrollToBottom = MutableStateFlow<Boolean?>(null)
    private val emojiData = MutableStateFlow<List<List<Emoji>>>(emptyList())
    val pagedList: Flow<androidx.paging.PagingData<MessageProfile>> =
        pagingInteractor.flow.cachedIn(viewModelScope)
    val state: StateFlow<ChatSalaState> = combine(
        loadingCounter.observable,
        uiMessageManager.message,
        observeUser.flow,
        observeAuthState.flow,
        scrollToBottom,
        emojiData,
        observeChat.flow,
        observeUsersSala.flow,
    ) { loading,message,user,authState,scrollToBottom,emojiData,chat,usersSala->
        ChatSalaState(
            loading = loading,
            message = message,
            user = user,
            scrollToBottom = scrollToBottom,
            authState = authState,
            emojiData = emojiData,
            chat = chat,
            usersSala = usersSala
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ChatSalaState.Empty
    )

    init {
        getData()
        observeUser(Unit)
        observeUsersSala(ObserveUsersSala.Params(id = salaId))
        observeChat(ObserveChat.Params(chatId))
        observeAuthState(Unit)
        pagingInteractor(ObservePagerMessages.Params(PAGING_CONFIG,chatId))
        viewModelScope.launch {
            chatRepository.observeMessages(chatId).collectLatest {
                Log.d("DEBUG_APP_MESSAGE",it.size.toString())
                val scroll = scrollToBottom.value?:false
                scrollToBottom.tryEmit(!scroll)
            }
        }
        viewModelScope.launch {
            observeUser.flow.collect{user ->
                try{
                    if(user.profile_id != 0L){
                        startWs(user.profile_id)
                    }
                    delay(100)
                    sendSharedMessage()
                    Log.d("DEBUG_APP_USER",user.toString())
                }catch(e:Exception){
                    Log.d("DEBUG_APP",e.localizedMessage?:"")
                }
            }
        }
        getEmojiData()
    }


    private fun getEmojiData(){
        viewModelScope.launch {
            val emoticonos = async { labelRepository.getEmojiByCategory(EmojiCategory.Emoticonos) }
            val people = async { labelRepository.getEmojiByCategory(EmojiCategory.Gente) }
            val animales = async { labelRepository.getEmojiByCategory(EmojiCategory.Animales) }
            val alimentos = async { labelRepository.getEmojiByCategory(EmojiCategory.Alimentos) }
            val viajar = async { labelRepository.getEmojiByCategory(EmojiCategory.Viajar) }
            val actividades = async { labelRepository.getEmojiByCategory(EmojiCategory.Actividades) }
            val objetos = async { labelRepository.getEmojiByCategory(EmojiCategory.Objetos) }
            val simbolos = async { labelRepository.getEmojiByCategory(EmojiCategory.Simbolos) }
            val banderas = async { labelRepository.getEmojiByCategory(EmojiCategory.Banderas) }
            emojiData.emit(awaitAll(emoticonos,people,animales,alimentos,viajar,actividades,objetos,simbolos,banderas))
        }
    }
    @OptIn(InternalAPI::class)
    @SuppressLint("SuspiciousIndentation")
    suspend fun startWs(profileId: Long){
        var cl:DefaultClientWebSocketSession? = null
        try {
            cl = client.webSocketSession(method = HttpMethod.Get, host = HostMessage.host,
//                client.webSocket(method = HttpMethod.Get, host = "172.20.20.76",
                port = HostMessage.port, path = "/v1/ws/suscribe/chat/?id=${chatId}&profileId=${profileId}")
            Log.d("DEBUG_APP","start ws.......")
            Log.d("DEBUG_APP_WS_AC",cl.isActive.toString())
//            cl.apply {
//                launch {
//                    outputMessage()
//                }
//            }
            if(cl.isActive){
                chatRepository.getChatUnreadMessages(chatId, TypeChat.TYPE_CHAT_SALA.ordinal)
                Log.d("DEBUG_APP","IS ACTIVE ASDASD AS")
            }
            cl.apply{
                for (message in incoming) {
                    message as? Frame.Text ?: continue
//                        val payload = Json.decodeFromString<WsAccountPayload>(message.readText())
                    val event = Json.decodeFromString<MessageEvent>(message.readText())
                    Log.d("DEBUG_APP",message.readText())
                    if (event.type == MessageEventType.EventTypeMessage) {
                        val payload = Json.decodeFromString<GrupoMessageDto>(event.payload)
//                            Log.d("DEBUG_APP_MESSAGE_REC",event.message.toString())
                        chatRepository.updateOrSaveMessage(payload,true)
//                            grupoRepository.updateLastMessage(grupoId,message.content,message.created_at)
                    }
                }

            }
            cl.start(emptyList())
        }catch (e:Exception){
            cl?.close()
            delay(1000)
            Log.d("DEBUG_ARR",e.localizedMessage?:"")
            startWs(profileId)
        }
    }

    companion object {
        val PAGING_CONFIG = androidx.paging.PagingConfig(
            pageSize = 20,
            initialLoadSize = 20,
//            prefetchDistance = 1
        )
    }
    //    @SuppressLint("SuspiciousIndentation")
//    suspend fun DefaultClientWebSocketSession.outputMessage(){
    private suspend fun outputMessage(data: Message){
        if (data.content == "" && data.data == null) return
        try{
            state.value.user?.let {
                Log.d("DEBUG_APP",it.toString())
                val message = GrupoMessageDto(
                    profile_id = data.profile_id,
                    chat_id = data.chat_id,
                    content = data.content,
                    created_at = data.created_at,
                    data = data.data,
                    type_message = data.type_message,
                    reply_to = data.reply_to,
                    parent_id = data.parent_id,
                    local_id = data.id
                )
                val event =  MessagePublishRequest(
                    message = message,
                    chat_id = chatId,
                    type_chat = TypeChat.TYPE_CHAT_SALA.ordinal
                )
                Log.d("DEBUG_APP_DATA",event.toString())
                chatRepository.publishMessage(event)
            }
        }catch(e:Exception){
            Log.d("DEBUG_APP",e.toString())
        }
    }

    fun getData(){
        viewModelScope.launch {
            try {
                chatRepository.updateUnreadMessages(chatId)
//                grupoRepository.getMessagesGrupo(grupoId)
            }catch(e: SerializationException){
                Log.d("DEBUG_ERROR",e.localizedMessage?:"")
            }catch (e:Exception){
                Log.d("DEBUG_ERROR",e.localizedMessage?:"")
            }
        }
    }
    private fun sendSharedMessage(){
        try {
            if(data.isBlank()) return
            Log.d("DEBUG_APP_DATA",data)
            val grupoMessageData = Json.decodeFromString<GrupoMessageData>(data)
            val message = MessageData(
                data = grupoMessageData.data,
                content = grupoMessageData.content,
                type_message = grupoMessageData.type_data
            )
            sendMessage(message,{})
        }catch (e:Exception){
            //todo()
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
                readed = true,
                parent_id = salaId
            )
            val res = async { chatRepository.saveMessageLocal(message) }
            res.await()
            animateScroll()
            outputMessage(message)

        }
    }
    fun getUserGrupo(profileId:Long): UserProfileGrupoAndSala?{
        return state.value.usersSala.find {
            it.profile_id == profileId
        }
    }
    fun clearMessage(id:Long){
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
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

    fun getKeyBoardHeight():Int{
        return preferences.keyBoardHeight
    }
    fun setKeyboardHeight(height:Int){
        preferences.keyBoardHeight = height
    }

    fun resetScroll(){
        viewModelScope.launch {
            scrollToBottom.tryEmit(null)
        }
    }


}
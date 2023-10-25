package app.regate.chat.grupo

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
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
import app.regate.data.grupo.GrupoRepository
import app.regate.data.instalacion.CupoRepository
import app.regate.data.labels.LabelRepository
import app.regate.domain.observers.ObserveAuthState
import app.regate.domain.observers.grupo.ObserveGrupo
import app.regate.domain.observers.pagination.ObservePagerMessages
import app.regate.domain.observers.account.ObserveUser
import app.regate.domain.observers.chat.ObserveChat
import app.regate.domain.observers.chat.ObserveUsersForChat
import app.regate.domain.observers.grupo.ObserveUsersGrupo
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

@OptIn(InternalAPI::class)
@Inject
class ChatGrupoViewModel(
    @Assisted savedStateHandle: SavedStateHandle,
    private val client:HttpClient,
    private val grupoRepository:GrupoRepository,
    private val chatRepository: ChatRepository,
    private val cupoRepository: CupoRepository,
    private val preferences:AppPreferences,
    private val labelRepository: LabelRepository,
    observeUser: ObserveUser,
    observeAuthState: ObserveAuthState,
//    observeGrupo: ObserveGrupo,
    pagingInteractor: ObservePagerMessages,
    observeUsers: ObserveUsersForChat,
    observeChat:ObserveChat,
):ViewModel() {
//    private val grupoId2:Long = savedStateHandle["id"]!!
    private val chatId:Long = savedStateHandle["id"]!!
    private val grupoId:Long = savedStateHandle.get<Long>("grupoId")?:0
    private val typeChat:Int = savedStateHandle.get<Int>("typeChat")?:TypeChat.TYPE_CHAT_GRUPO.ordinal
    private var data = savedStateHandle.get<String>("data")?:""
    private val loadingState = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val scrollToBottom = MutableStateFlow<Boolean?>(null)
    private val emojiData = MutableStateFlow<List<List<Emoji>>>(emptyList())
    val pagedList: Flow<PagingData<MessageProfile>> =
        pagingInteractor.flow.cachedIn(viewModelScope)
    val state:StateFlow<ChatGrupoState> = combine(
        loadingState.observable,
        uiMessageManager.message,
        observeUser.flow,
        observeAuthState.flow,
        observeChat.flow,
        observeUsers.flow,
        scrollToBottom,
        emojiData
    ){loading,message,user,authState,chat,usersGrupo,scrollToBottom,emojiData->
        ChatGrupoState(
            loading = loading,
            message = message,
            user = user,
            authState = authState,
            chat = chat,
            usersGrupo = usersGrupo,
            scrollToBottom = scrollToBottom,
            emojiData = emojiData
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ChatGrupoState.Empty
    )
    init {
        getData()
        observeUser(Unit)
        observeAuthState(Unit)
        observeChat(ObserveChat.Params(id = chatId))
        observeUsers(ObserveUsersForChat.Params(id = grupoId,typeChat = typeChat))
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
    @SuppressLint("SuspiciousIndentation")
    suspend fun startWs(profileId: Long){
        var cl:DefaultClientWebSocketSession? = null
        try {
            cl = client.webSocketSession(method = HttpMethod.Get, host = HostMessage.host,
//                client.webSocket(method = HttpMethod.Get, host = "172.20.20.76",
                port =HostMessage.port, path = "/v1/ws/suscribe/chat/?id=${chatId}&profileId=${profileId}")
            Log.d("DEBUG_APP","start ws.......")
            Log.d("DEBUG_APP_WS_AC",cl.isActive.toString())
//            cl.apply {
//                launch {
//                    outputMessage()
//                }
//            }
            if(cl.isActive){
                chatRepository.getChatUnreadMessages(chatId, typeChat)
                Log.d("DEBUG_APP","IS ACTIVE ASDASD AS")
            }
            cl.apply{
                    for (message in incoming) {
                        message as? Frame.Text ?: continue
//                        val payload = Json.decodeFromString<WsAccountPayload>(message.readText())
                        val event = Json.decodeFromString<MessageEvent>(message.readText())
                        if (event.type == MessageEventType.EventTypeMessage) {
                        Log.d("DEBUG_APP_MESSAGE_PAYLOAD",message.readText())
                        val payload = Json.decodeFromString<GrupoMessageDto>(event.payload)
//                            Log.d("DEBUG_APP_MESSAGE_REC",event.message.toString())
                        delay(1000)
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
        val PAGING_CONFIG = PagingConfig(
            pageSize = 20,
            initialLoadSize = 20,
//            prefetchDistance = 1
        )
    }
//    @SuppressLint("SuspiciousIndentation")
//    suspend fun DefaultClientWebSocketSession.outputMessage(){
    private suspend fun outputMessage(data:Message){
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
                        type_chat = typeChat
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
                grupoRepository.getUsersGroup(grupoId)
//                grupoRepository.getMessagesGrupo(grupoId)
            }catch(e:SerializationException){
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
                parent_id = grupoId
            )
            val res = async { chatRepository.saveMessageLocal(message) }
            res.await()
            animateScroll()
            outputMessage(message)

        }
    }
    fun clearMessage(id:Long){
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }

    fun getUserGrupo(profileId:Long):UserProfileGrupoAndSala?{
        return state.value.usersGrupo.find {
            it.profile_id == profileId
        }
    }

    fun navigateToInstalacionReserva(instalacionId:Long,establecimientoId:Long,cupos:List<CupoInstalacion>,
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
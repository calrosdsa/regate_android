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
import app.regate.compoundmodels.UserProfileGrupo
import app.regate.constant.HostMessage
import app.regate.data.app.EmojiCategory
import app.regate.data.chat.ChatRepository
import app.regate.data.common.MessageData
import app.regate.data.dto.chat.MessageEvent
import app.regate.data.dto.chat.MessageEventType
import app.regate.data.dto.chat.MessagePublishRequest
import app.regate.data.dto.chat.TypeChat
import app.regate.data.dto.empresa.grupo.CupoInstalacion
import app.regate.data.dto.empresa.grupo.GrupoEvent
import app.regate.data.dto.empresa.grupo.GrupoMessageData
import app.regate.data.dto.empresa.grupo.GrupoMessageDto
import app.regate.data.grupo.GrupoRepository
import app.regate.data.instalacion.CupoRepository
import app.regate.data.labels.LabelRepository
import app.regate.data.users.UsersRepository
import app.regate.domain.observers.ObserveAuthState
import app.regate.domain.observers.grupo.ObserveGrupo
import app.regate.domain.observers.pagination.ObservePagerMessages
import app.regate.domain.observers.account.ObserveUser
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
import io.ktor.websocket.send
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@OptIn(InternalAPI::class)
@Inject
class ChatGrupoViewModel(
    @Assisted savedStateHandle: SavedStateHandle,
    private val client:HttpClient,
    private val grupoRepository:GrupoRepository,
    private val usersRepository: UsersRepository,
    private val chatRepository: ChatRepository,
    private val cupoRepository: CupoRepository,
    private val preferences:AppPreferences,
    private val labelRepository: LabelRepository,
    observeUser: ObserveUser,
    observeAuthState: ObserveAuthState,
    observeGrupo: ObserveGrupo,
    pagingInteractor: ObservePagerMessages,
    observeUsersGrupo: ObserveUsersGrupo,
):ViewModel() {
//    private val grupoId2:Long = savedStateHandle["id"]!!
    private val chatId:Long = savedStateHandle["id"]!!
    private val grupoId:Long = savedStateHandle.get<Long>("grupoId")?:0
    private var data = savedStateHandle.get<String>("data")?:""
    private val loadingState = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val messageChat = MutableStateFlow<Message?>(null)
    private val scrollToBottom = MutableStateFlow<Boolean?>(null)
    private val emojiData = MutableStateFlow<List<List<Emoji>>>(emptyList())
    val pagedList: Flow<PagingData<MessageProfile>> =
        pagingInteractor.flow.cachedIn(viewModelScope)
    val state:StateFlow<ChatGrupoState> = combine(
        loadingState.observable,
        uiMessageManager.message,
        messageChat,
        observeUser.flow,
        observeAuthState.flow,
        observeGrupo.flow,
        observeUsersGrupo.flow,
        scrollToBottom,
        emojiData
    ){loading,message,messageChat,user,authState,grupo,usersGrupo,scrollToBottom,emojiData->
        ChatGrupoState(
            loading = loading,
            message = message,
            messageChat = messageChat,
            user = user,
            authState = authState,
            grupo = grupo,
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
        observeGrupo(ObserveGrupo.Param(id = grupoId))
        observeUsersGrupo(ObserveUsersGrupo.Params(id = grupoId))
        pagingInteractor(ObservePagerMessages.Params(PAGING_CONFIG,chatId))
        Log.d("DEBUG_APP_GRUPO_ID",grupoId.toString())
        viewModelScope.launch {
            startWs()
        }
        viewModelScope.launch {
            grupoRepository.observeMessages(chatId).collectLatest {
                Log.d("DEBUG_APP_MESSAGE",it.size.toString())
                val scroll = scrollToBottom.value?:false
                scrollToBottom.tryEmit(!scroll)
            }
        }
        viewModelScope.launch {
            observeUser.flow.collect{user ->
                try{
                    delay(100)
                    sendSharedMessage()
                    Log.d("DEBUG_APP_USER",user.toString())
                }catch(e:Exception){
                    Log.d("DEBUG_APP",e.localizedMessage?:"")
                }
            }
        }
        outputMessage()
        getEmojiData()

    }
    fun getEmojiData(){
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
    suspend fun startWs(){
        var cl:DefaultClientWebSocketSession? = null
        try {
            cl = client.webSocketSession(method = HttpMethod.Get, host = HostMessage.host,
//                client.webSocket(method = HttpMethod.Get, host = "172.20.20.76",
                port =HostMessage.port, path = "/v1/ws/suscribe/chat/?id=${chatId}&profileId=43")
            Log.d("DEBUG_APP","start ws.......")
            Log.d("DEBUG_APP_WS",cl.isActive.toString())
//            cl.apply {
//                launch {
//                    outputMessage()
//                }
//            }
//            if(cl.isActive){
//                syncMessages()
//            }
            cl.apply{
                    for (message in incoming) {
                        message as? Frame.Text ?: continue
//                        val payload = Json.decodeFromString<WsAccountPayload>(message.readText())
                        val event = Json.decodeFromString<MessageEvent>(message.readText())
                        Log.d("DEBUG_APP",message.readText())
                        if (event.type == MessageEventType.EventTypeMessage) {
                            val payload = Json.decodeFromString<GrupoMessageDto>(event.payload)
//                            Log.d("DEBUG_APP_MESSAGE_REC",event.message.toString())
                        chatRepository.saveMessage(payload,true)
//                            grupoRepository.updateLastMessage(grupoId,message.content,message.created_at)
                        }
//                    if (event.type == GrupoEventType.GrupoEvnetUser) {
//                        val id = event.message.profile_id
//                        try {
//                            val res = usersRepository.getProfile(id)
//                            uiMessageManager.emitMessage(UiMessage(message = "${res.nombre} acaba de unirse al grupo"))
//                        } catch (e: Exception) {
//                            uiMessageManager.emitMessage(
//                                UiMessage(
//                                    message = e.localizedMessage ?: ""
//                                )
//                            )
//                        }
//                    }
//                    if (event.type == GrupoEventType.GrupoEventSala) {
//                        try {
//                            uiMessageManager.emitMessage(UiMessage(message = "Se ha creado una nueva sala ${event.sala?.nombre}"))
//                        } catch (e: Exception) {
//                            uiMessageManager.emitMessage(
//                                UiMessage(
//                                    message = e.localizedMessage ?: ""
//                                )
//                            )
//                        }
//                    }


                    }

            }
            cl.start(emptyList())
        }catch (e:Exception){
            cl?.close()
            delay(1000)
            Log.d("DEBUG_ARR",e.localizedMessage?:"")
            startWs()
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
    fun outputMessage(){
    viewModelScope.launch {
        messageChat.filterNotNull().collectLatest {data->
            try{
            if (data.content == "" && data.data == null) return@collectLatest
                state.value.user?.let {
                    Log.d("DEBUG_APP",it.toString())
                    val message = GrupoMessageDto(
                        profile_id = it.profile_id,
                        content = data.content,
                        chat_id = chatId,
                        type_message = data.type_message,
                        reply_to = data.reply_to,
                        data = data.data,
                        created_at = data.created_at,
                        id = data.id,
                        parent_id = data.parent_id,
                    )
                    val event =  MessagePublishRequest(
                        message = message,
                        chat_id = chatId,
                        type_chat = TypeChat.TYPE_CHAT_GRUPO.ordinal
                    )
                    Log.d("DEBUG_APP_DATA",event.toString())
                   chatRepository.publishMessage(event)
                }
            }catch(e:Exception){
                Log.d("DEBUG_APP",e.toString())
            }
        }
    }
    }

    fun getData(){
        viewModelScope.launch {
            try {
                chatRepository.updateUnreadMessages(chatId)
                grupoRepository.getUsersGroup(grupoId)
//                grupoRepository.getMessagesGrupo(grupoId)
            }catch(e:SerializationException){
                Log.d("DEBUG_ERROR",e.message.toString())
                Log.d("DEBUG_ERROR","serialization exeption")
                Log.d("DEBUG_ERROR",e.localizedMessage?:"")
            }catch (e:Exception){
                Log.d("DEBUG_ERROR","fail to fetchUsers")
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
            messageChat.tryEmit(message)
        }
    }
    fun syncMessages(){
        viewModelScope.launch {
//            if(state.value.authState == AppAuthState.LOGGED_IN){
               grupoRepository.syncMessages(chatId)
//            }
        }
    }
    fun clearMessage(id:Long){
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }

    fun getUserGrupo(profileId:Long):UserProfileGrupo?{
        return state.value.usersGrupo.find {
            it.id == profileId
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
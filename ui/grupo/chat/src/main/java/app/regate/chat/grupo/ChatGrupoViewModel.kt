package app.regate.chat.grupo

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import app.regate.api.UiMessage
import app.regate.api.UiMessageManager
import app.regate.compoundmodels.MessageProfile
import app.regate.compoundmodels.UserProfileGrupo
import app.regate.constant.Host
import app.regate.data.common.MessageData
import app.regate.data.daos.MessageProfileDao
import app.regate.data.dto.empresa.grupo.CupoInstalacion
import app.regate.data.dto.empresa.grupo.GrupoEvent
import app.regate.data.dto.empresa.grupo.GrupoMessageData
import app.regate.data.dto.empresa.grupo.GrupoMessageDto
import app.regate.data.grupo.GrupoRepository
import app.regate.data.instalacion.CupoRepository
import app.regate.data.users.UsersRepository
import app.regate.domain.observers.ObserveAuthState
import app.regate.domain.observers.ObserveGrupo
import app.regate.domain.observers.pagination.ObservePagerMessages
import app.regate.domain.observers.ObserveUser
import app.regate.domain.observers.ObserveUsersGrupo
import app.regate.extensions.combine
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
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.async
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
    private val cupoRepository: CupoRepository,
    private val preferences:AppPreferences,
//    private val profileDao: ProfileDao,
    private val messageProfileDao: MessageProfileDao,
    observeUser: ObserveUser,
    observeAuthState: ObserveAuthState,
    observeGrupo: ObserveGrupo,
    pagingInteractor: ObservePagerMessages,
    observeUsersGrupo: ObserveUsersGrupo,
//    private val observeInstalacion: ObserveInstalacion
):ViewModel() {
//    private val grupoId2:Long = savedStateHandle["id"]!!
    private val grupoId:Long = savedStateHandle["id"]!!
    private var data = savedStateHandle.get<String>("data")?:""
    private val loadingState = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val messageChat = MutableStateFlow<Message?>(null)
    private val scrollToBottom = MutableStateFlow<Boolean?>(null)
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
        scrollToBottom
    ){loading,message,messageChat,user,authState,grupo,usersGrupo,scrollToBottom->
        ChatGrupoState(
            loading = loading,
            message = message,
            messageChat = messageChat,
            user = user,
            authState = authState,
            grupo = grupo,
            usersGrupo = usersGrupo,
            scrollToBottom = scrollToBottom

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
        pagingInteractor(ObservePagerMessages.Params(PAGING_CONFIG,grupoId))
        viewModelScope.launch {
            startWs()
        }
        viewModelScope.launch {
            grupoRepository.observeMessages(grupoId).collectLatest {
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
    }
    suspend fun startWs(){
        try {
            val cl = client.webSocketSession(method = HttpMethod.Get, host = Host.host,
//                client.webSocket(method = HttpMethod.Get, host = "172.20.20.76",
                port =Host.port, path = "/v1/ws/chat-grupo?id=${grupoId}")
            Log.d("DEBUG_APP","start ws.......")
            Log.d("DEBUG_APP_WS",cl.isActive.toString())
            cl.apply {
                launch {
                    outputMessage()
                }
            }
            if(cl.isActive){
                syncMessages()
            }

            while (cl.isActive) {
                val othersMessage = cl.incoming.receive() as? Frame.Text
                if (othersMessage != null) {
                    val event = Json.decodeFromString<GrupoEvent>(othersMessage.readText())
                    if (event.type == "message") {
                        grupoRepository.saveMessage(event.message)
                    }
                    if (event.type == "user") {
                        val id = event.message.profile_id
                        try {
                            val res = usersRepository.getProfile(id)
                            uiMessageManager.emitMessage(UiMessage(message = "${res.nombre} acaba de unirse al grupo"))
                        } catch (e: Exception) {
                            uiMessageManager.emitMessage(
                                UiMessage(
                                    message = e.localizedMessage ?: ""
                                )
                            )
                        }
                    }
                    if (event.type == "sala") {
//                               val id = event.message.profile_id
                        try {
//                                   val res = usersRepository.getProfile(id)
                            uiMessageManager.emitMessage(UiMessage(message = "Se ha creado una nueva sala ${event.sala?.nombre}"))
                        } catch (e: Exception) {
                            uiMessageManager.emitMessage(
                                UiMessage(
                                    message = e.localizedMessage ?: ""
                                )
                            )
                        }
                    }
//                           val messageProfile = receiveDeserialized<MessageResponse>()
                    Log.d("DEBUG_APP", othersMessage.readText())
                }
            }
            cl.start(emptyList())
//        client.close()
        }catch (e:Exception){
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
    suspend fun DefaultClientWebSocketSession.outputMessage(){
        messageChat.filterNotNull().collectLatest {data->
            if (data.content != "") {
                state.value.user?.let {
                    Log.d("DEBUG_APP",it.toString())
                    val message = GrupoMessageDto(
                        profile_id = it.profile_id,
                        content = data.content,
                        grupo_id = grupoId,
                        type_message = data.type_message,
                        reply_to = data.reply_to,
                        data = data.data,
                        id = data.id,
                    )
                    val event =  GrupoEvent(
                        type = "message",
                        message = message,
                    )
                    send(
                        Json.encodeToString(event)
                    )
                }
            }
        }
    }

    fun getData(){
        viewModelScope.launch {
            try {

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
                grupo_id = grupoId,
                id = getLongUuid(),
                type_message = messageData.type_message,
                data = messageData.data
            )
            val res = async { grupoRepository.saveMessageLocal(message) }
            res.await()
            animateScroll()
            messageChat.tryEmit(message)
        }
    }
    fun syncMessages(){
        viewModelScope.launch {
//            if(state.value.authState == AppAuthState.LOGGED_IN){
               grupoRepository.syncMessages(grupoId)
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
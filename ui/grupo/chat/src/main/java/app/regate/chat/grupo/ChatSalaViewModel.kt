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
import app.regate.data.auth.AppAuthState
import app.regate.data.daos.MessageProfileDao
import app.regate.data.dto.empresa.grupo.GrupoEvent
import app.regate.data.dto.empresa.grupo.GrupoMessageDto
import app.regate.data.grupo.GrupoRepository
import app.regate.data.users.UsersRepository
import app.regate.domain.observers.ObserveAuthState
import app.regate.domain.observers.ObserveGrupo
import app.regate.domain.observers.ObservePagerMessages
import app.regate.domain.observers.ObserveUser
import app.regate.domain.observers.ObserveUsersGrupo
import app.regate.extensions.combine
import app.regate.models.Message
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
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import java.util.UUID

@Inject
class ChatSalaViewModel(
    @Assisted savedStateHandle: SavedStateHandle,
    private val client:HttpClient,
    private val grupoRepository:GrupoRepository,
    private val usersRepository: UsersRepository,
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
    private val loadingState = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val messageChat = MutableStateFlow<Message?>(null)

    val pagedList: Flow<PagingData<MessageProfile>> =
        pagingInteractor.flow.cachedIn(viewModelScope)
    val state:StateFlow<ChatSalaState> = combine(
        loadingState.observable,
        uiMessageManager.message,
        messageChat,
        observeUser.flow,
        observeAuthState.flow,
        observeGrupo.flow,
        observeUsersGrupo.flow,
    ){loading,message,messageChat,user,authState,grupo,usersGrupo->
        ChatSalaState(
            loading = loading,
            message = message,
            messageChat = messageChat,
            user = user,
            authState = authState,
            grupo = grupo,
            usersGrupo = usersGrupo

        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ChatSalaState.Empty
    )
    init {
        getData()
        observeUser(Unit)
        observeAuthState(Unit)
        observeGrupo(ObserveGrupo.Param(id = grupoId))
        observeUsersGrupo(ObserveUsersGrupo.Params(id = grupoId))
        syncMessages()
        pagingInteractor(ObservePagerMessages.Params(PAGING_CONFIG,grupoId))
        viewModelScope.launch {
            try{
//            runBlocking {
               client.webSocket(method = HttpMethod.Get, host = "172.20.20.76",
                    port = 9090, path = "/v1/ws/chat-grupo?id=${grupoId}"){
                    launch { outputMessage() }
//                    launch { inputMessage() }
                   while (true){
                       val othersMessage = incoming.receive() as? Frame.Text
                       if (othersMessage != null) {
                           val event = Json.decodeFromString<GrupoEvent>(othersMessage.readText())
                           if(event.type == "message"){
                               grupoRepository.saveMessage(event.message)
                           }
                           if(event.type == "user"){
                              val id = event.message.profile_id
                               try{
                                   val res = usersRepository.getProfile(id)
                                   uiMessageManager.emitMessage(UiMessage(message = "${res.nombre} acaba de unirse al grupo"))
                               }catch(e:Exception){
                                   uiMessageManager.emitMessage(UiMessage(message = e.localizedMessage?:""))
                               }
                           }
                           if(event.type == "sala"){
//                               val id = event.message.profile_id
                               try{
//                                   val res = usersRepository.getProfile(id)
                                   uiMessageManager.emitMessage(UiMessage(message = "Se ha creado una nueva sala ${event.sala?.nombre}"))
                               }catch(e:Exception){
                                   uiMessageManager.emitMessage(UiMessage(message = e.localizedMessage?:""))
                               }
                           }
//                           val messageProfile = receiveDeserialized<MessageResponse>()
                           Log.d("DEBUG_APP",othersMessage.readText())
                       }
                   }
            }
            client.close()
            }catch(e:Exception){
                Log.d("DEBUG_ERROR",e.localizedMessage?:"")
            }
        }
    }
    companion object {
        val PAGING_CONFIG = PagingConfig(
            pageSize = 20,
            initialLoadSize = 20,
        )
    }

    suspend fun addUser(id:Long){
        try{
            val res = usersRepository.getProfile(id)
            uiMessageManager.emitMessage(UiMessage(message = "${res.nombre} acaba de unirse al grupo"))
        }catch(e:Exception){
            uiMessageManager.emitMessage(UiMessage(message = e.localizedMessage?:""))
        }
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
                        reply_to = data.reply_to,
                        id = data.id
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
                Log.d("DEBUG_ERROR",e.localizedMessage?:"")
            }
        }
    }

    fun sendMessage(messageData:MessageData){
        viewModelScope.launch {
            val message =  Message(
                content = messageData.content,
                reply_to = messageData.reply_to,
                profile_id = state.value.user?.profile_id?:0,
                created_at = Clock.System.now(),
                grupo_id = grupoId,
                id = UUID.randomUUID().mostSignificantBits and Long.MAX_VALUE
            )
            grupoRepository.saveMessageLocal(message)
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

}
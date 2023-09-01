package app.regate.chat.sala

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import app.regate.api.UiMessage
import app.regate.api.UiMessageManager
import app.regate.compoundmodels.MessageSalaWithProfile
import app.regate.compoundmodels.UserProfileGrupo
import app.regate.data.common.MessageData
import app.regate.data.dto.empresa.salas.MessageSalaDto
import app.regate.data.dto.empresa.salas.SalaEvent
import app.regate.data.sala.SalaRepository
import app.regate.data.users.UsersRepository
import app.regate.domain.observers.ObserveAuthState
import app.regate.domain.observers.ObserveGrupo
import app.regate.domain.observers.pagination.ObservePagerMessagesSala
import app.regate.domain.observers.ObserveUser
import app.regate.domain.observers.ObserveUsersGrupo
import app.regate.extensions.combine
import app.regate.models.MessageSala
import app.regate.util.ObservableLoadingCounter
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.HttpMethod
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
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
class ChatSalaViewModel(
    @Assisted savedStateHandle: SavedStateHandle,
    private val client:HttpClient,
    private val salaRepository:SalaRepository,
    private val usersRepository: UsersRepository,
//    private val profileDao: ProfileDao,
    observeUser: ObserveUser,
    observeAuthState: ObserveAuthState,
    observeGrupo: ObserveGrupo,
    pagingInteractor: ObservePagerMessagesSala,
    observeUsersGrupo: ObserveUsersGrupo,
//    private val observeInstalacion: ObserveInstalacion
):ViewModel() {
//    private val grupoId2:Long = savedStateHandle["id"]!!
    private val salaId:Long = savedStateHandle.get<Long>("id")?:0
    private val titleSala:String = savedStateHandle.get<String>("title")?:""
    private val loadingState = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val messageChat = MutableStateFlow<MessageSala?>(null)

    val pagedList: Flow<PagingData<MessageSalaWithProfile>> =
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
        observeUser(Unit)
        observeAuthState(Unit)
        observeGrupo(ObserveGrupo.Param(id = salaId))
        observeUsersGrupo(ObserveUsersGrupo.Params(id = salaId))
        syncData()
        pagingInteractor(ObservePagerMessagesSala.Params(PAGING_CONFIG,salaId))
        viewModelScope.launch {
            try{
//            runBlocking {
//               client.webSocket(method = HttpMethod.Get, host = "192.168.0.12",
//                val ws = client.webSocketSession {  }
             client.webSocket(method = HttpMethod.Get, host = "172.20.20.76",
//                client.webSocket(method = HttpMethod.Get, host = "192.168.0.12",
                    port = 9090, path = "/v1/ws/chat-sala?id=${salaId}"){
                    launch { outputMessage() }
//                    launch { inputMessage() }
                   while (true){
                       val othersMessage = incoming.receive() as? Frame.Text
                       if (othersMessage != null) {
                           val event = Json.decodeFromString<SalaEvent>(othersMessage.readText())
                           if(event.type == "message"){
                               salaRepository.saveMessage(event.message)
                           }
//                           if(event.type == "user"){
//                              val id = event.message.profile_id
//                               try{
//                                   val res = usersRepository.getProfile(id)
//                                   uiMessageManager.emitMessage(UiMessage(message = "${res.nombre} acaba de unirse al grupo"))
//                               }catch(e:Exception){
//                                   uiMessageManager.emitMessage(UiMessage(message = e.localizedMessage?:""))
//                               }
//                           }
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
//            prefetchDistance = 1
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
                    val message = MessageSalaDto(
                        profile_id = it.profile_id,
                        content = data.content,
                        sala_id = salaId,
                        reply_to = data.reply_to,
                        id = data.id
                    )
                    val event =  SalaEvent(
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

//    fun getData(){
//        viewModelScope.launch {
//            try {
//                salaRepository.getUsersGroup(salaId)
////                salaRepository.getMessagesGrupo(salaId)
//            }catch(e:SerializationException){
//                Log.d("DEBUG_ERROR",e.message.toString())
//                Log.d("DEBUG_ERROR","serialization exeption")
//                Log.d("DEBUG_ERROR",e.localizedMessage?:"")
//
//            }catch (e:Exception){
//                Log.d("DEBUG_ERROR","fail to fetchUsers")
//                Log.d("DEBUG_ERROR",e.localizedMessage?:"")
//            }
//        }
//    }

    fun sendMessage(messageData: MessageData,animateScroll:()->Unit){
        viewModelScope.launch {
            val message =  MessageSala(
                content = messageData.content,
                reply_to = messageData.reply_to,
                profile_id = state.value.user?.profile_id?:0,
                created_at = Clock.System.now(),
                sala_id = salaId,
                id = UUID.randomUUID().mostSignificantBits and Long.MAX_VALUE
            )
            val res = async { salaRepository.saveMessageLocal(message) }
            res.await()
            animateScroll()
            messageChat.tryEmit(message)
        }
    }
    fun syncData(){
        viewModelScope.launch {
//            if(state.value.authState == AppAuthState.LOGGED_IN){
               launch { salaRepository.insertUsersSala(salaId) }
               launch{ salaRepository.syncMessages(salaId) }
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

    fun getTitleSala():String{
        return  titleSala
    }

    fun getIdSala(): Long {
        return salaId
    }

}
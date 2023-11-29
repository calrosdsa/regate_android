package app.regate.sala

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessage
import app.regate.api.UiMessageManager
import app.regate.data.dto.ResponseMessage
import app.regate.data.dto.empresa.salas.SalaDetail
import app.regate.data.sala.SalaRepository
import app.regate.domain.observers.ObserveAuthState
import app.regate.domain.observers.instalacion.ObserveInstalacion
import app.regate.domain.observers.account.ObserveUser
import app.regate.util.ObservableLoadingCounter
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import app.regate.data.dto.empresa.grupo.GrupoMessageData
import app.regate.data.dto.empresa.grupo.GrupoMessageType
import app.regate.data.dto.empresa.grupo.MessageSalaPayload
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import app.regate.common.resources.R
import app.regate.data.chat.ChatParams
import app.regate.data.chat.ChatRepository
import app.regate.data.dto.chat.NotifyNewUserRequest
import app.regate.data.dto.chat.TypeChat
import app.regate.domain.interactors.UpdateChat
import app.regate.domain.observers.ObserveUsersSala
import app.regate.domain.observers.chat.ObserveUsersForChat
import app.regate.extensions.combine
import app.regate.models.TypeEntity

@Inject
class SalaViewModel(
    @Assisted savedStateHandle: SavedStateHandle,
    private val salaRepository: SalaRepository,
    private val observeInstalacion: ObserveInstalacion,
    observeAuthState: ObserveAuthState,
    observeUser: ObserveUser,
    private val updateChat: UpdateChat,
    private val chatRepository: ChatRepository,
    observeUsersForChat: ObserveUsersForChat
    ):ViewModel() {
    private val salaId: Long = savedStateHandle["id"]!!
    private val loadingState = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val data = MutableStateFlow<SalaDetail?>(null)
    val state:StateFlow<SalaState> = combine(
        uiMessageManager.message,
        loadingState.observable,
        observeAuthState.flow,
        data,
        observeUser.flow,
        observeUsersForChat.flow
    ){message,loading,authState,data,user,users->
        SalaState(
            message = message,
            authState = authState,
            loading = loading,
            data = data,
            user = user,
            users = users
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = SalaState.Empty
    )
    init {
        observeUsersForChat(ObserveUsersForChat.Params(id = salaId, typeChat = TypeChat.TYPE_CHAT_SALA.ordinal))
        observeAuthState(Unit)
        observeUser(Unit)
        getSala()
    }
    fun getSala(){
        viewModelScope.launch {
            try{
            salaRepository.getSala(salaId).let {result->
                data.tryEmit(result)
                updateChat.executeSync(UpdateChat.Params(
                    params = ChatParams(
                        parent_id = salaId,
                        typeChat = TypeChat.TYPE_CHAT_SALA.ordinal,
                        photo = result.instalacion.portada,
                        name = result.sala.titulo
                    )
                ))
            }
                chatRepository.getUsers(salaId,TypeChat.TYPE_CHAT_SALA.ordinal)

            } catch (e:ResponseException){
                Log.d("DEBUG_ERROR",e.localizedMessage?:"")
            } catch(e :Exception){
                Log.d("DEBUG_ERROR",e.localizedMessage?:"")
            }
        }
    }
    fun joinSala(){
        viewModelScope.launch {
            try{
                loadingState.addLoader()
                val res = state.value.data?.sala?.let {
                    salaRepository.joinSala(salaId, it.precio,it.cupos,it.grupo_id)
                }
                getSala()
                loadingState.removeLoader()
                if(res != null){
                val notifyNewUserRequest = NotifyNewUserRequest(
                    id = res.id,
                    profileId = state.value.user?.profile_id?:0,
                    typeEntity = TypeEntity.SALA,
                    parentId = state.value.data?.sala?.id?:0
                )
                    chatRepository.notifyNewUser(notifyNewUserRequest)
                }


//                Log.d("DEBUG_APP_ERROR",res.message)
            }catch(e:ResponseException){
                loadingState.removeLoader()
                uiMessageManager.emitMessage(UiMessage(message = e.response.body<ResponseMessage>().message))
                Log.d("DEBUG_APP_ERROR",e.response.body()?:"error")
            }catch (e:Exception){
                //TODO()
            }
        }
    }

    fun navigateSelect(navigate:(String)->Unit){
        try{
            val sala = state.value.data?.sala
            if(sala != null){
            val salaData = MessageSalaPayload(
                id = sala.id.toInt(),
                titulo = sala.titulo,
                cupos = sala.cupos,
                precio = sala.precio,
                precio_cupo=sala.precio_cupo,
                start = sala.horas[0],
                end = sala.horas.last()
            )
            val data = GrupoMessageData(
                type_data = GrupoMessageType.SALA.ordinal,
                data =  Json.encodeToString(salaData)
            )
            Log.d("DEBUG_APP", Json.encodeToString(data))
            navigate(Json.encodeToString(data))
            }
        }catch (e:Exception){
            //TODO()
        }
    }
    fun exitSala(context: Context, navigateUp:()->Unit){
        viewModelScope.launch {
            try{
                loadingState.addLoader()
                state.value.users.find {
                    it.profile_id == state.value.user?.profile_id
                }?.id .also {userSalaId->
                    if(userSalaId != null){
                        salaRepository.exitSala(userSalaId.toInt())
                    }
                }
                loadingState.removeLoader()
                navigateUp()
            }catch (e:Exception){
                loadingState.removeLoader()
                uiMessageManager.emitMessage(UiMessage(message = context.getString(R.string.unexpected_error)))
                Log.d("DEBUG_APP_ERROR",e.localizedMessage?:"error")
            }
        }
    }

    fun navigateToChat(id:Long,navigateToChat: (id: Long,parentId:Long,typeChat:Int) -> Unit){
        viewModelScope.launch {
            try{
                val chat = chatRepository.getChatByType(id,TypeChat.TYPE_CHAT_SALA.ordinal)
                if (chat != null) {
                    navigateToChat(chat.id,chat.parent_id,TypeChat.TYPE_CHAT_SALA.ordinal)
                }
            }catch (e:Exception){

                Log.d("DEBUG_APP_",e.localizedMessage?:"")
            }
        }
    }


    fun refresh(){
        viewModelScope.launch {
            getSala()
        }
    }
    fun clearMessage(id:Long) {
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }

}
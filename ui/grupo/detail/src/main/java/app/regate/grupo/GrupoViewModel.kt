package app.regate.grupo

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessageManager
import app.regate.compoundmodels.UserProfileGrupoAndSalaDto
import app.regate.data.chat.ChatParams
import app.regate.data.chat.ChatRepository
import app.regate.data.dto.chat.NotifyNewUserRequest
import app.regate.data.dto.chat.TypeChat
import app.regate.data.dto.empresa.salas.SalaDto
import app.regate.data.dto.system.ReportData
import app.regate.data.dto.system.ReportType
import app.regate.data.grupo.GrupoRepository
import app.regate.domain.interactors.UpdateChat
import app.regate.domain.observers.ObserveAuthState
import app.regate.domain.observers.grupo.ObserveGrupo
import app.regate.domain.observers.account.ObserveUser
import app.regate.domain.observers.grupo.ObserveUsersGrupo
import app.regate.extensions.combine
import app.regate.models.chat.Chat
import app.regate.util.ObservableLoadingCounter
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class GrupoViewModel(
    @Assisted savedStateHandle: SavedStateHandle,
    private val grupoRepository: GrupoRepository,
    observeAuthState: ObserveAuthState,
    private val observeUsersGrupo: ObserveUsersGrupo,
    observeGrupo: ObserveGrupo,
    private val observeUser: ObserveUser,
    private val chatRepository: ChatRepository,
    private val updateChat: UpdateChat,
    ):ViewModel() {
    private val grupoId: Long = savedStateHandle["id"]!!
    private val loadingState = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val salas = MutableStateFlow<List<SalaDto>>(emptyList())
    private val currentUser = MutableStateFlow<UserProfileGrupoAndSalaDto?>(null)
    private val selectedUser = MutableStateFlow<UserProfileGrupoAndSalaDto?>(null)
    private val chat = MutableStateFlow<Chat?>(null)
    val state:StateFlow<GrupoState> = combine(
        uiMessageManager.message,
        loadingState.observable,
        observeAuthState.flow,
        observeUsersGrupo.flow,
        observeGrupo.flow,
        salas,
        observeUser.flow,
        currentUser,
        selectedUser,
    ){ message, loading, authState,usersGrupo, grupo,salas,user,currentUser,selectedUser->
        GrupoState(
            message = message,
            authState = authState,
            loading = loading,
            usersProfileGrupo = usersGrupo,
            grupo = grupo,
            salas = salas,
            user = user,
            currentUser = currentUser,
            selectedUser = selectedUser
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = GrupoState.Empty
    )
    init {
        observeUser(Unit)
        observeAuthState(Unit)
        observeGrupo(ObserveGrupo.Param(id = grupoId))
        observeUsersGrupo(ObserveUsersGrupo.Params(id=grupoId))
        getGrupo()
        checkIsAdmin()

    }
    fun checkIsAdmin() {
        viewModelScope.launch {
            try {
                observeUsersGrupo.flow.collect { results ->
                    try {
                        Log.d("DEBUG_APP_SS", results.toString())
                        observeUser.flow.collect { result ->
                                val user = results.find { it.profile_id == result.profile_id }
                                currentUser.tryEmit(results.find { it.profile_id == user?.profile_id })
                        }
                    } catch (e: Exception) {
                        //TODO()
                    }
                }
            } catch (e: Exception) {
                //TODO()
            }
        }
    }
    fun getGrupo() {
        viewModelScope.launch {
            try {
                loadingState.addLoader()
                val res = grupoRepository.getGrupoDetail(grupoId)
                checkIsAdmin()
                salas.tryEmit(res.salas)
                loadingState.removeLoader()
                updateChat.executeSync(UpdateChat.Params(
                    params = ChatParams(
                        parent_id = grupoId,
                        typeChat = TypeChat.TYPE_CHAT_GRUPO.ordinal,
                        photo = res.grupo.photo,
                        name = res.grupo.name
                    )
                ))
                chatRepository.getUsers(grupoId,TypeChat.TYPE_CHAT_GRUPO.ordinal)
                Log.d("DEBUG_APP", res.toString())
            } catch (e: ResponseException) {
                loadingState.removeLoader()
                Log.d("DEBUG_ERROR", e.response.body())
            } catch (e: Exception) {
                loadingState.removeLoader()
                Log.d("DEBUG_ERROR", e.localizedMessage ?: "")
            }
        }
    }

//    fun joinGrupo(){
//        viewModelScope.launch {
//            try{
//                loadingState.addLoader()
//                 grupoRepository.joinGrupo(grupoId)
//                getGrupo()
//                loadingState.removeLoader()
////                uiMessageManager.emitMessage(UiMessage(message = res.message))
////                Log.d("DEBUG_APP_ERROR",res.message)
//            }catch(e:ResponseException){
//                loadingState.removeLoader()
//                uiMessageManager.emitMessage(UiMessage(message = e.response.body<ResponseMessage>().message))
//                Log.d("DEBUG_APP_ERROR",e.response.body()?:"error")
//            }
//        }
//    }
    fun selectUser(id:Long){
        viewModelScope.launch {
        try{
            val user = state.value.usersProfileGrupo.first { it.profile_id == id }
            selectedUser.tryEmit(user)
        }catch (e:Exception){
            //TODO()
        }
        }
    }
    fun removeUserFromGroup(){
        viewModelScope.launch {
            try{
                selectedUser.value?.let {
                    grupoRepository.removeUserFromGroup(it.id)
                    notifyUser(it.id,it.profile_id)
                }
            }catch (e:Exception){
                Log.d("DEBUG_APP_WS",e.localizedMessage?:"")
            }
        }
    }
    fun removeUserAdmin(){
        viewModelScope.launch {
            try{
            selectedUser.value?.let {
                grupoRepository.changeStatusUser(it.id,false)
            }
            }catch (e:Exception){
                Log.d("DEBUG_APP_WS",e.localizedMessage?:"")
            }
        }
    }
    fun addUserAdmin(){
        viewModelScope.launch {
            try{

            selectedUser.value?.let {
                grupoRepository.changeStatusUser(it.id,true)
            }
            }catch (e:Exception){
                Log.d("DEBUG_APP_WS",e.localizedMessage?:"")
            }
//            grupoRepository.changeStatusUser(id,true)
        }
    }
    fun leaveGroup(navigateUp:()->Unit){
        viewModelScope.launch {
            try{
                val chat = chatRepository.getChatByType(grupoId,TypeChat.TYPE_CHAT_GRUPO.ordinal)
            val targetUser = state.value.usersProfileGrupo
                .first { it.profile_id == state.value.user?.profile_id }
                if (chat != null) {
                    grupoRepository.leaveGrupo(targetUser.id,chat.id,grupoId)
                    notifyUser(targetUser.id,state.value.user?.profile_id ?: 0)
                }
                grupoRepository.deleteGroupUserLocal(groupId = grupoId)
            navigateUp()
            }catch (e:Exception){
                //TODO()
            }
        }
    }

    suspend fun notifyUser(id:Long,profileId:Long){
        val request = NotifyNewUserRequest(
            id = id,
            profileId = profileId,
            parentId = grupoId,
            type_chat = TypeChat.TYPE_CHAT_GRUPO.ordinal,
            is_out = true
        )
        chatRepository.notifyNewUser(request)
    }
    fun navigateToReport(navigate:(String)->Unit){
        try{
            val report = Json.encodeToString(
                ReportData(
                report_type = ReportType.GRUPO.ordinal,
                entity_id = grupoId.toInt(),
            )
            )
            navigate(report)
        }catch (e:Exception){
            //TODO()
        }
    }
    suspend fun getPendingRequestCount():Int {
        return try{
            val res = grupoRepository.getPendingRequestCount(grupoId)
//            Log.d("DEBUG_APP_COUNT",res.toString())
            return res.count
        }catch(e:Exception){
            Log.d("DEBUG_APP_ERR_COUNT",e.localizedMessage?:"")
            10
        }
    }

    fun navigateToChat(id:Long,navigateToChat: (id: Long,parentId:Long,typeChat:Int) -> Unit){
        viewModelScope.launch {
            try{
                val chat = chatRepository.getChatByType(id,TypeChat.TYPE_CHAT_GRUPO.ordinal)
                if (chat != null) {
                    navigateToChat(chat.id,chat.parent_id,TypeChat.TYPE_CHAT_GRUPO.ordinal)
                }
            }catch (e:Exception){
                Log.d("DEBUG_APP_",e.localizedMessage?:"")
            }
        }
    }

    fun refresh(){
        viewModelScope.launch {
            try{
            getGrupo()
            }catch (e:Exception){
                //TODO()
            }
        }
    }
    fun clearMessage(id:Long) {
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }
}
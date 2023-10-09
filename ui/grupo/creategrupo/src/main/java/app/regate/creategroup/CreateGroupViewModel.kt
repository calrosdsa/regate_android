package app.regate.creategroup

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessageManager
import app.regate.data.dto.FileData
import app.regate.data.dto.empresa.grupo.GroupRequest
import app.regate.data.grupo.GrupoRepository
import app.regate.domain.observers.ObserveAuthState
import app.regate.domain.observers.grupo.ObserveGrupo
import app.regate.util.ObservableLoadingCounter
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class CreateGroupViewModel(
    @Assisted savedStateHandle: SavedStateHandle,
    observeAuthState: ObserveAuthState,
    observeGrupo: ObserveGrupo,
    private val grupoRepository: GrupoRepository
    ):ViewModel() {
        private val groupId: Long = savedStateHandle.get<Long>("id")?:0
    private val loadingState = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val file = MutableStateFlow<FileData?>(null)
    val state: StateFlow<CreateGroupState> = combine(
        uiMessageManager.message,
        loadingState.observable,
        observeAuthState.flow,
        observeGrupo.flow,
    ) { message, loading, authState,group->
        CreateGroupState(
            message = message,
            authState = authState,
            loading = loading,
            group = group
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = CreateGroupState.Empty
    )

    init {
        observeAuthState(Unit)
        Log.d("DEBUG_APP_ARG",groupId.toString())
        if(groupId != 0L){
            observeGrupo(ObserveGrupo.Param(id = groupId))
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun createGroup(name: String, description: String, visibility: Int,
    openBottonAuth:()->Unit,removeLoader:()->Unit,navigateUp:()->Unit) {
        viewModelScope.launch {
            try{
                 grupoRepository.createGrupo(GroupRequest(
                   name = name,
                   description = description,
                   visibility = visibility,
                   fileData = file.value,
                    id = groupId,
                    photo_url = state.value.group?.photo
                ))
                navigateUp()
//                uiMessageManager.emitMessage(UiMessage(message = "El grupo se ha creado exitosamente."))
                removeLoader()
            }catch (e:ResponseException){
                if(e.response.status == HttpStatusCode.Unauthorized){
//                    uiMessageManager.emitMessage(UiMessage(message= ))
                    openBottonAuth()
                }
                removeLoader()
                Log.d("DEBUG_APP",e.response.status.value.toString())
                Log.d("DEBUG_APP",e.response.body()?:"")
            } catch (e:Exception){
                removeLoader()
                Log.d("DEBUG_APP",e.localizedMessage?:"")
            }
        }
    }

    fun uploadImage(type:String,name:String,byteArray: ByteArray){
        viewModelScope.launch {
            val result = FileData( name = name,type = type, byteArray = byteArray)
            file.tryEmit(result)
        }
    }

    fun clearMessage(id:Long){
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }
}
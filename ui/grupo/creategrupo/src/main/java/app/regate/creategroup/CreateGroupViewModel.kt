package app.regate.creategroup

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessageManager
import app.regate.data.dto.FileData
import app.regate.data.dto.empresa.grupo.GroupRequest
import app.regate.data.dto.empresa.grupo.GrupoDto
import app.regate.data.grupo.GrupoRepository
import app.regate.domain.observers.ObserveAuthState
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

@Inject
class CreateGroupViewModel(
    @Assisted savedStateHandle: SavedStateHandle,
    observeAuthState: ObserveAuthState,
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
    ) { message, loading, authState->
        CreateGroupState(
            message = message,
            authState = authState,
            loading = loading,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = CreateGroupState.Empty
    )

    init {
        observeAuthState(Unit)
        Log.d("DEBUG_APP_ARG",groupId.toString())
    }

    @SuppressLint("SuspiciousIndentation")
    fun createGroup(name: String, description: String, visibility: String, navigateToGroup:(Long)->Unit) {
        viewModelScope.launch {
            try{
                loadingState.addLoader()
                val res =  grupoRepository.createGrupo(GroupRequest(
                   name = name,
                   description = description,
                   visibility = visibility.toInt(),
                   fileData = file.value
                ))
                navigateToGroup(res.id)
                loadingState.removeLoader()
            }catch (e:ResponseException){
                loadingState.removeLoader()
                Log.d("DEBUG_APP",e.response.body()?:"")
            } catch (e:Exception){
                loadingState.removeLoader()
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
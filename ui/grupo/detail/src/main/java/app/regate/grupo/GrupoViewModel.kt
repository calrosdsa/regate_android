package app.regate.grupo

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessageManager
import app.regate.data.dto.empresa.salas.SalaDto
import app.regate.data.grupo.GrupoRepository
import app.regate.domain.observers.ObserveAuthState
import app.regate.domain.observers.ObserveGrupo
import app.regate.domain.observers.ObserveUsersGrupo
import app.regate.extensions.combine
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
class GrupoViewModel(
    @Assisted savedStateHandle: SavedStateHandle,
    private val grupoRepository: GrupoRepository,
    observeAuthState: ObserveAuthState,
    observeUsersGrupo: ObserveUsersGrupo,
    observeGrupo: ObserveGrupo
    ):ViewModel() {
    private val grupoId: Long = savedStateHandle["id"]!!
    private val loadingState = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val salas = MutableStateFlow<List<SalaDto>>(emptyList())
    val state:StateFlow<GrupoState> = combine(
        uiMessageManager.message,
        loadingState.observable,
        observeAuthState.flow,
        observeUsersGrupo.flow,
        observeGrupo.flow,
        salas
    ){ message, loading, authState,usersGrupo, grupo,salas->
        GrupoState(
            message = message,
            authState = authState,
            loading = loading,
            usersProfileGrupo = usersGrupo,
            grupo = grupo,
            salas = salas
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = GrupoState.Empty
    )
    init {
        observeAuthState(Unit)
        observeGrupo(ObserveGrupo.Param(id = grupoId))
        observeUsersGrupo(ObserveUsersGrupo.Params(id=grupoId))
        getGrupo()
    }
    fun getGrupo(){
        viewModelScope.launch {
            try{
                val res = grupoRepository.getGrupo(grupoId)
                salas.tryEmit(res)
                Log.d("DEBUG_APP",res.toString())
            } catch (e:ResponseException){
                Log.d("DEBUG_ERROR",e.response.body())
            }catch(e:Exception){
                Log.d("DEBUG_ERROR",e.localizedMessage?:"")
            }
        }
    }
    fun joinSala(){
        viewModelScope.launch {
//            try{
//                loadingState.addLoader()
//                val res = salaRepository.joinSala(grupoId,200)
//                getSala()
//                loadingState.removeLoader()
//                uiMessageManager.emitMessage(UiMessage(message = res.message))
////                Log.d("DEBUG_APP_ERROR",res.message)
//            }catch(e:ResponseException){
//                loadingState.removeLoader()
//                uiMessageManager.emitMessage(UiMessage(message = e.response.body<ResponseMessage>().message))
//                Log.d("DEBUG_APP_ERROR",e.response.body()?:"error")
//            }
        }
    }

    fun refresh(){
        viewModelScope.launch {
            getGrupo()
        }
    }
    fun clearMessage(id:Long) {
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }
}
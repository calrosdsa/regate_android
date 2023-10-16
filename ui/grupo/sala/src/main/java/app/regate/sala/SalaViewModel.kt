package app.regate.sala

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
import app.regate.domain.observers.ObserveUser
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

@Inject
class SalaViewModel(
    @Assisted savedStateHandle: SavedStateHandle,
    private val salaRepository: SalaRepository,
    private val observeInstalacion: ObserveInstalacion,
    observeAuthState: ObserveAuthState,
    observeUser:ObserveUser,
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
    ){message,loading,authState,data,user->
        SalaState(
            message = message,
            authState = authState,
            loading = loading,
            data = data,
            user = user
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = SalaState.Empty
    )
    init {
        observeAuthState(Unit)
        observeUser(Unit)
        getSala()
    }
    fun getSala(){
        viewModelScope.launch {
            try{
            salaRepository.getSala(salaId).let {result->
                data.tryEmit(result)
            }
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
                val res = state.value.data?.sala?.let { salaRepository.joinSala(salaId, it.precio,it.cupos,it.grupo_id) }
                getSala()
                loadingState.removeLoader()
                if (res != null) {
                    uiMessageManager.emitMessage(UiMessage(message = res.message))
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
//    fun exitSala(context:Context, navigateUp:()->Unit){
//        viewModelScope.launch {
//            try{
//                state.value.data?.profiles?.find {
//                    it.profile_id == state.value.user?.profile_id
//                }?.user_id.also {userSalaId->
//                    if(userSalaId != null){
//                        salaRepository.exitSala(userSalaId.toInt())
//                    }
//                }
//                navigateUp()
//            }catch (e:Exception){
//                uiMessageManager.emitMessage(UiMessage(message = context.getString(R.string.unexpected_error)))
//                Log.d("DEBUG_APP_ERROR",e.localizedMessage?:"error")
//            }
//        }
//    }

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
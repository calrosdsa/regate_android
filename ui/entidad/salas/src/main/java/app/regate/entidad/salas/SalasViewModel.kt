package app.regate.entidad.salas

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessage
import app.regate.api.UiMessageManager
import app.regate.data.dto.empresa.salas.SalaDto
import app.regate.data.sala.SalaRepository
import app.regate.util.ObservableLoadingCounter
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class SalasViewModel(
    @Assisted savedStateHandle: SavedStateHandle,
    private val salaRepository: SalaRepository,
):ViewModel() {
//    val  d= savedStateHandle.set("")
    private val establecimientoId: Long = savedStateHandle["id"]!!
//    private val establecimientoId: Long = 1
    private val loadingState = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val salas = MutableStateFlow<List<SalaDto>>(emptyList())

    val state: StateFlow<SalasState> = combine(
        loadingState.observable,
        uiMessageManager.message,
        salas
    ){loading,message,salas->
        SalasState(
            loading = loading,
            message = message,
            salas = salas,
//            selectedTime = selectedTime
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = SalasState.Empty
    )
    init {
        getSalas()
    }

    fun getSalas(){
        viewModelScope.launch(context = Dispatchers.IO) {
            try{
                loadingState.addLoader()
                val res = salaRepository.getEstablecimientoSalas(establecimientoId)
//                delay(2000)
                salas.tryEmit(res)
                loadingState.removeLoader()
            }catch(e: ResponseException){
                Log.d("DEBUG_APP_",e.localizedMessage?:"")
                loadingState.removeLoader()
                uiMessageManager.emitMessage(UiMessage(message = e.response.body()))
            }catch(e:Exception){
                Log.d("DEBUG_APP_",e.localizedMessage?:"")
                loadingState.removeLoader()
            }
        }
    }

    fun getEstablecimientoId():Long{
        return establecimientoId
    }
}

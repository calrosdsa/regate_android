package app.regate.account.reservas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessage
import app.regate.api.UiMessageManager
import app.regate.data.dto.account.reserva.ReservaDto
import app.regate.data.reserva.ReservaRepository
import app.regate.discover.ReservasState
import app.regate.util.ObservableLoadingCounter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@Inject
class ReservasViewModel(
    private val reservaRepository: ReservaRepository,

): ViewModel() {
    private val loadingState = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val reservas = MutableStateFlow<List<ReservaDto>>(emptyList())
    val state:StateFlow<ReservasState> = combine(
        loadingState.observable,
        uiMessageManager.message,
        reservas
    ){loading,message,reservas->
        ReservasState(
            loading = loading,
            message= message,
            reservas = reservas
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ReservasState.Empty
    )

    init{
        getReservas()
    }


    fun getReservas(){
        viewModelScope.launch {
            try {
                loadingState.addLoader()
                val res = reservaRepository.getReservas()
                reservas.tryEmit(res)
                loadingState.removeLoader()
            }catch(e:Exception){
                loadingState.removeLoader()
                uiMessageManager.emitMessage(UiMessage(message = e.localizedMessage?:""))
            }
        }
    }

    fun clearMessage(id:Long){
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }
}
package app.regate.account.reservas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessage
import app.regate.api.UiMessageManager
import app.regate.data.reserva.ReservaRepository
import app.regate.domain.observers.ObserveReservas
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
    observeReservas: ObserveReservas,
): ViewModel() {
    private val loadingState = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val selectedReservas = MutableStateFlow<List<Long>>(emptyList())

    val state:StateFlow<ReservasState> = combine(
        loadingState.observable,
        uiMessageManager.message,
        observeReservas.flow,
        selectedReservas,
    ){loading,message,reservas,selectedReservas->
        ReservasState(
            loading = loading,
            message= message,
            reservas = reservas,
            selectedReservas = selectedReservas
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ReservasState.Empty
    )

    init{
        observeReservas(Unit)
        getReservas()
    }


    fun getReservas(){
        viewModelScope.launch {
            try {
                loadingState.addLoader()
                reservaRepository.updateReservas()
                loadingState.removeLoader()
            }catch(e:Exception){
                loadingState.removeLoader()
                uiMessageManager.emitMessage(UiMessage(message = e.localizedMessage?:""))
            }
        }
    }

    fun selectReserva(id:Long){
        viewModelScope.launch {
            if(selectedReservas.value.contains(id)){
                val updateValues = selectedReservas.value.filter { it != id }
                selectedReservas.emit(updateValues)
            }else{
                selectedReservas.emit(selectedReservas.value+id)
            }
        }
    }

    fun cancelSelectedReservas(){
        viewModelScope.launch {
            selectedReservas.emit(emptyList())
        }
    }
    fun deleteReservas(){
        viewModelScope.launch {
            try{
                reservaRepository.deleteReservas(selectedReservas.value)
                selectedReservas.emit(emptyList())
            }catch(e:Exception){
                //TODO()
            }
        }
    }

    fun clearMessage(id:Long){
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }
}
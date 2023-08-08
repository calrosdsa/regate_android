package app.regate.account.reserva

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessageManager
import app.regate.data.dto.account.reserva.ReservaDetail
import app.regate.data.reserva.ReservaRepository
import app.regate.util.ObservableLoadingCounter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class ReservaViewModel(
    @Assisted savedStateHandle: SavedStateHandle,
    private val reservaRepository: ReservaRepository
):ViewModel() {
    private val reservaId = savedStateHandle.get<Long>("id") ?: 0
    private val loadingCounter =ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val data = MutableStateFlow<ReservaDetail?>(null)
    val state:StateFlow<ReservaState> = combine(
        loadingCounter.observable,
        uiMessageManager.message,
        data,
    ){loading,message,data->
        ReservaState(
            loading = loading,
            message = message,
            data = data
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ReservaState.Empty
    )

    init{
        Log.d("DEBUG_APP_","RESERVA ID $reservaId")
        getData()
    }

    fun getData(){
        viewModelScope.launch {
            try{
                val res = reservaRepository.getReserva(reservaId)
                data.emit(res)
            }catch (e:Exception){
                Log.d("DEBUG_APP",e.localizedMessage?:"")
            }
        }
    }
}
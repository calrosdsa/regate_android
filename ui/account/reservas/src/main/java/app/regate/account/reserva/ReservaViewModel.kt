package app.regate.account.reserva

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessageManager
import app.regate.domain.interactors.UpdateEstablecimiento
import app.regate.domain.interactors.UpdateInstalacion
import app.regate.domain.observers.ObserveReservaDetail
import app.regate.util.ObservableLoadingCounter
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
    observeReservaDetail: ObserveReservaDetail,
    private val updateEstablecimiento: UpdateEstablecimiento,
    private val updateInstalacion: UpdateInstalacion,
):ViewModel() {
    private val reservaId = savedStateHandle.get<Long>("id") ?: 0
    private val instalacionId = savedStateHandle.get<Long>("instalacion_id") ?: 0
    private val establecimientoId = savedStateHandle.get<Long>("establecimiento_id") ?: 0

    private val loadingCounter =ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()

    val state:StateFlow<ReservaState> = combine(
        loadingCounter.observable,
        uiMessageManager.message,
        observeReservaDetail.flow,
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
        observeReservaDetail(ObserveReservaDetail.Params(id = reservaId))
        getData()
    }

    fun getData(){
        viewModelScope.launch {
            try{
                updateEstablecimiento.executeSync(UpdateEstablecimiento.Params(id = establecimientoId))
                updateInstalacion.executeSync(UpdateInstalacion.Params(id = instalacionId))
            }catch (e:Exception){
                Log.d("DEBUG_APP",e.localizedMessage?:"")
            }
        }
    }
}
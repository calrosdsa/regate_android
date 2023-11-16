package app.regate.account.reserva

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessageManager
import app.regate.data.chat.ChatRepository
import app.regate.data.reserva.ReservaRepository
import app.regate.domain.interactors.UpdateEstablecimiento
import app.regate.domain.interactors.UpdateInstalacion
import app.regate.domain.observers.ObserveAuthState
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
    observeAuthState:ObserveAuthState,
    private val updateEstablecimiento: UpdateEstablecimiento,
    private val updateInstalacion: UpdateInstalacion,
    private val reservaRepository: ReservaRepository,
    private val chatRepository: ChatRepository
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
        observeAuthState.flow,
    ){loading,message,data,authState->
        ReservaState(
            loading = loading,
            message = message,
            data = data,
            authState = authState
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ReservaState.Empty
    )

    init{
        Log.d("DEBUG_APP_","RESERVA ID $reservaId")
        observeReservaDetail(ObserveReservaDetail.Params(id = reservaId))
        observeAuthState(Unit)
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

    fun navigateToConversationE(navigate:(Long,Long)->Unit){
        viewModelScope.launch {
            try{
                val res = chatRepository.getConversationId(establecimientoId)
                navigate(res.id,establecimientoId)
            }catch (e:Exception){
                Log.d("DEBUG_APP_ERROR",e.localizedMessage?:"")
                //TODO()
            }
        }
    }

    fun updateDescription(description:String){
        viewModelScope.launch {
            reservaRepository.updateDescriptionReserva(description,reservaId)
        }
    }
    fun deleteReserva(){
        viewModelScope.launch {
            try{
                reservaRepository.deleteReservas(listOf(reservaId))
            }catch(e:Exception){
                //todo()
            }
        }
    }

}
package app.regate.bottom.reserva

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessage
import app.regate.api.UiMessageManager
import app.regate.data.dto.ResponseMessage
import app.regate.data.reserva.ReservaRepository
import app.regate.domain.interactors.UpdateEstablecimiento
import app.regate.domain.observers.ObserveAuthState
import app.regate.domain.observers.ObserveCupos
import app.regate.domain.observers.ObserveEstablecimientoDetail
import app.regate.domain.observers.ObserveInstalacion
import app.regate.domain.observers.ObserveSettingEstablecimiento
import app.regate.extensions.combine
import app.regate.util.ObservableLoadingCounter
import app.regate.util.collectStatus
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.datetime.toLocalDateTime
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class BottomReservaViewModel(
    @Assisted savedStateHandle: SavedStateHandle,
    observeCupo: ObserveCupos,
    observeAuthState: ObserveAuthState,
    observeSettingEstablecimiento: ObserveSettingEstablecimiento,
    observeEstablecimientoDetail: ObserveEstablecimientoDetail,
    observeInstalacion: ObserveInstalacion,
    private val reservaRepository: ReservaRepository,
    private val updateEstablecimiento: UpdateEstablecimiento
):ViewModel(){
    private val instalacionId: Long = savedStateHandle["id"]!!
    private val establecimientoId: Long = savedStateHandle["establecimientoId"]!!
    private val loadingState = ObservableLoadingCounter()
    private val loadingPlaceholderState = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val totalPrice = MutableStateFlow<Int?>(null)
    val state: StateFlow<BottomReservaState> = combine(
        loadingState.observable,
        uiMessageManager.message,
        observeAuthState.flow,
        observeCupo.flow,
        observeSettingEstablecimiento.flow,
        observeEstablecimientoDetail.flow,
        observeInstalacion.flow,
        totalPrice,
    ){loading,message,authState,cupos,setting,establecimiento,instalacion,totalPrice->
        BottomReservaState(
            loading = loading,
            message = message,
            cupos = cupos,
            authState = authState,
            setting = setting,
            totalPrice = totalPrice,
            establecimiento = establecimiento,
            instalacion = instalacion
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = BottomReservaState.Empty
    )

    init {
        observeEstablecimientoDetail(ObserveEstablecimientoDetail.Params(establecimientoId))
        observeSettingEstablecimiento(ObserveSettingEstablecimiento.Params(establecimientoId))
        observeInstalacion(ObserveInstalacion.Params(instalacionId))
        observeCupo(ObserveCupos.Params(instalacionId))
        observeAuthState(Unit)
        viewModelScope.launch {
            observeCupo.flow.collect{ it ->
                if(it.isNotEmpty()){
                val price = it.map{it.price}.reduce { acc, i ->
                    acc + i
                }
                totalPrice.tryEmit(price.toInt())
                }
            }
        }
        viewModelScope.launch {
            updateEstablecimiento(
                UpdateEstablecimiento.Params(establecimientoId)
            ).collectStatus(loadingPlaceholderState,uiMessageManager)
        }
    }

    fun confirmarReservas(){
        viewModelScope.launch {
            try{
                loadingState.addLoader()
                val endTime = state.value.cupos.last().time.toLocalDateTime(TimeZone.UTC).toJavaLocalDateTime().plusMinutes(30)
//                Log.d("DEBUG_APP_DATE",endTime.toString())
                val res = reservaRepository.confirmarReservas(state.value.cupos,
                    totalPrice.value!!,totalPrice.value!!,endTime.toString(),establecimientoId)
                Log.d("DEBUG_APP","ESTABLECIIENTO_ID $establecimientoId")
                delay(1000)
//                uiMessageManager.emitMessage(UiMessage(message = "Se ha completado el proceso"))
                uiMessageManager.emitMessage(UiMessage(message = res.message))
                loadingState.removeLoader()
            }catch (e:ResponseException){
                loadingState.removeLoader()
                uiMessageManager.emitMessage(UiMessage(message = e.response.body<ResponseMessage>().message))
            }catch(e:Exception){
                loadingState.removeLoader()
                uiMessageManager.emitMessage(UiMessage(message = e.localizedMessage?:""))
            }
        }
    }

    fun clearMessage(id: Long) {
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }
}
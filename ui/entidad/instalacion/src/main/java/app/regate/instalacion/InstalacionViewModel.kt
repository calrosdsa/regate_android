package app.regate.instalacion

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessage
import app.regate.api.UiMessageManager
import app.regate.data.dto.empresa.establecimiento.CuposRequest
import app.regate.data.instalacion.InstalacionRepository
import app.regate.domain.interactors.UpdateInstalacion
import app.regate.domain.observers.instalacion.ObserveInstalacion
import app.regate.util.ObservableLoadingCounter
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class InstalacionViewModel(
    @Assisted savedStateHandle: SavedStateHandle,
    observeInstalacion: ObserveInstalacion,
    private val updateInstalacion: UpdateInstalacion,
    private val instalacionRepository: InstalacionRepository,

//    private val appDateFormatter: AppDateFormatter,
):ViewModel(){
    private val instalacionId: Long = savedStateHandle["id"]!!
    private val loadingState = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()


    val state: StateFlow<InstalacionState> = combine(
        observeInstalacion.flow,
        loadingState.observable,
        uiMessageManager.message,

    ){instalacion,loading,message ->
        InstalacionState(
            loading = loading,
            message = message,
            instalacion = instalacion,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = InstalacionState.Empty
    )

    init {
        getInstalacion()
        observeInstalacion(ObserveInstalacion.Params(instalacionId))
        Log.d("API_",instalacionId.toString())
    }
    fun getCupos(startDate:String) {
        viewModelScope.launch {
            try {
                loadingState.addLoader()
                val res = instalacionRepository.getCupos(
                    d = CuposRequest(
                        start_date = startDate,
                        instalacion_id = instalacionId.toInt()
                    )
                )
                Log.d("API_",res.toString())
                loadingState.removeLoader()
            }catch(e:Exception){
                loadingState.removeLoader()
                uiMessageManager.emitMessage(UiMessage(message = e.localizedMessage?:"None"))
            }
        }
    }
    fun getInstalacion(){
        viewModelScope.launch {
            updateInstalacion(UpdateInstalacion.Params(instalacionId,true))
        }
    }



    fun clearMessage(id: Long) {
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }
}
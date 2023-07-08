package app.regate.instalacion

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessage
import app.regate.api.UiMessageManager
import app.regate.data.dto.empresa.establecimiento.CupoInstaDto
import app.regate.data.dto.empresa.establecimiento.CuposRequest
import app.regate.data.instalacion.CupoRepository
import app.regate.data.instalacion.InstalacionRepository
import app.regate.data.mappers.DtoToCupo
import app.regate.domain.interactors.UpdateInstalacion
import app.regate.domain.observers.ObserveInstalacion
import app.regate.util.ObservableLoadingCounter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class InstalacionViewModel(
    @Assisted savedStateHandle: SavedStateHandle,
    observeInstalacion: ObserveInstalacion,
    private val updateInstalacion: UpdateInstalacion,
    private val instalacionRepository: InstalacionRepository,
    private val cupoRepository: CupoRepository,
    private val mapperCupo:DtoToCupo,
//    private val appDateFormatter: AppDateFormatter,
):ViewModel(){
    private val instalacionId: Long = savedStateHandle["id"]!!
    private val loadingState = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val cupos = MutableStateFlow<List<CupoInstaDto>>(emptyList())
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    private val selectedCupos = MutableStateFlow<List<CupoInstaDto>>(emptyList())

    val state: StateFlow<InstalacionState> = combine(
        observeInstalacion.flow,
        loadingState.observable,
        uiMessageManager.message,
        cupos,
        selectedCupos,
    ){instalacion,loading,message,cupos,selectedCupos ->
        InstalacionState(
            loading = loading,
            message = message,
            instalacion = instalacion,
            cupos = cupos,
            selectedCupos = selectedCupos
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = InstalacionState.Empty
    )

    init {
        getInstalacion()
        getCupos(now.toString())
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
                cupos.tryEmit(res)
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

    fun addCupoToSelectedList(cupo:CupoInstaDto) {
        Log.d("API_",cupo.toString())
        viewModelScope.launch {
            if(selectedCupos.value.contains(cupo)){
                selectedCupos.tryEmit(selectedCupos.value.filter {
                    it != cupo
                })
            }else{
            selectedCupos.tryEmit(selectedCupos.value.plus(cupo))
            }
        }
    }
    fun openBottomSheet(open:(id:Long)->Unit){
        viewModelScope.launch {
            cupoRepository.insertCupos(selectedCupos.value.map {
                mapperCupo.map(it)
            })
            open(instalacionId)
        }
    }

    fun clearMessage(id: Long) {
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }
}
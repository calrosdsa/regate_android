package app.regate.createsala

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessage
import app.regate.api.UiMessageManager
import app.regate.compoundmodels.InstalacionCupos
import app.regate.data.daos.CupoDao
import app.regate.data.dto.empresa.salas.SalaRequestDto
import app.regate.data.sala.SalaRepository
import app.regate.domain.observers.ObserveAuthState
import app.regate.extensions.combine
import app.regate.util.ObservableLoadingCounter
import io.ktor.client.plugins.ResponseException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class CreateSalaViewModel(
    @Assisted savedStateHandle: SavedStateHandle,
    observeAuthState: ObserveAuthState,
    private val salaRepository: SalaRepository,
    private val cupoDao: CupoDao
    ):ViewModel() {
//        private val salaId: Long = savedStateHandle["id"]!!
    private val grupoId: Long = savedStateHandle["grupo_id"]!!
    private val loadingState = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val instalacionCupos = MutableStateFlow<InstalacionCupos?>(null)
    private val salaData = MutableStateFlow(SalaRequestDto())
    private val enableToContinue = MutableStateFlow(false)
    val state: StateFlow<CreateSalaState> = combine(
        uiMessageManager.message,
        loadingState.observable,
        observeAuthState.flow,
        instalacionCupos,
        salaData,
        enableToContinue,
    ) { message, loading, authState, instalacionCupos, salaData,enableToContinue ->
        CreateSalaState(
            message = message,
            authState = authState,
            loading = loading,
            instalacionCupos = instalacionCupos,
            salaData = salaData,
            enableToContinue = enableToContinue
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = CreateSalaState.Empty
    )

    init {
        observeAuthState(Unit)
        viewModelScope.launch {
            cupoDao.deleteCupos()
            cupoDao.observeLastCupo().filterNotNull().collectLatest { cupo ->
                    val instalacionC = cupoDao.getInstalacionCupos(cupo.instalacion_id)
                    instalacionCupos.tryEmit(instalacionC)
                    instalacionC.let { instalacionCupos1 ->
                        enableToContinue.tryEmit(true)
                        salaData.tryEmit(
                            salaData.value.copy(
                                category_id = instalacionCupos1.instalacion.category_id ?: 0,
                                instalacion_id = instalacionCupos1.instalacion.id,
                                establecimiento_id = instalacionCupos1.instalacion.establecimiento_id,
                                start_time = instalacionCupos1.cupos.first().time.toLocalDateTime(TimeZone.UTC).time.toString(),
                                end_time = instalacionCupos1.cupos.last().time.toLocalDateTime(TimeZone.UTC).time.toString(),
                                fecha = instalacionCupos1.cupos.first().time.toLocalDateTime(
                                    TimeZone.UTC
                                ).date.toString(),
                                precio = instalacionCupos1.cupos.sumOf { it.price }.toInt()
                            )
                        )
                    }
            }
        }
    }


    fun createSala(asunto: String, description: String, cupos: String) {
        Log.d("DEBUG_APP_CUPOS", "SALA DATA CREATE ${salaData.value}")
        viewModelScope.launch {
            try {
                loadingState.addLoader()
                salaData.value.let {
                    if(it.instalacion_id == 0L ) {
                        uiMessageManager.emitMessage(UiMessage(
                            message = "No fue posible crear la sala debido a que no se estableció el lugar y la hora"))
                        loadingState.removeLoader()
                        return@launch
                    }
                    if((cupos.toInt()) -1 >= (state.value.instalacionCupos?.instalacion?.cantidad_personas?: 30)) {
                        uiMessageManager.emitMessage(UiMessage(
                            message = "No fue posible crear la sala debido a que los cupos superan la cantidad maxima"))
                        loadingState.removeLoader()
                        return@launch
                    }
                    val data = it.copy(
                        titulo = asunto,
                        descripcion = description,
//                        establecimiento_id = establecimientoId,
                        cupos = cupos.toInt(),
                        grupo_id = grupoId
                    )
                    Log.d("DEBUG_APP_CUPOS", "SALA DATA $data")
                    val res = salaRepository.createSala(data)
                    Log.d("DEBUG_APP_CUPOS", "SALA DATA $res")
                }
                loadingState.removeLoader()
                uiMessageManager.emitMessage(UiMessage(message = "Grupo creado exitosamente"))
            } catch (e: ResponseException) {
                loadingState.removeLoader()
                when(e.response.status){
                    HttpStatusCode.Unauthorized ->{
                        uiMessageManager.emitMessage(UiMessage(message = "Usuario no authorizado"))
                    }
                    else ->{
                        uiMessageManager.emitMessage(UiMessage(message = "No se pudo crear el grupo"))
                    }
                }
                Log.d("DEBUG_APP_ERROR_1", e.localizedMessage ?: "")
            } catch (e: Exception) {
                loadingState.removeLoader()
                uiMessageManager.emitMessage(UiMessage(message = e.localizedMessage?:""))
                Log.d("DEBUG_APP_ERROR_1", e.localizedMessage ?: "")

            }
        }
    }

    fun clearMessage(id:Long){
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }
}
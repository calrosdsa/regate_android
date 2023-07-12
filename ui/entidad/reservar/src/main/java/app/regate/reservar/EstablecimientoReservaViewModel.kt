package app.regate.reservar

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.ApiError
import app.regate.api.ApiSuccess
import app.regate.api.UiMessage
import app.regate.api.UiMessageManager
import app.regate.compoundmodels.InstalacionCategoryCount
import app.regate.data.dto.empresa.establecimiento.CupoEstablecimiento
import app.regate.data.dto.empresa.establecimiento.CuposEstablecimientoRequest
import app.regate.data.dto.empresa.instalacion.InstalacionAvailable
import app.regate.data.dto.empresa.instalacion.InstalacionRequest
import app.regate.data.dto.empresa.instalacion.InstalacionesAvailables
import app.regate.data.establecimiento.EstablecimientoRepository
import app.regate.data.instalacion.CupoRepository
import app.regate.data.instalacion.InstalacionRepository
import app.regate.domain.interactors.UpdateInstalaciones
import app.regate.domain.observers.ObserveInstalacionCategoryCount
import app.regate.domain.observers.ObserveSettingEstablecimiento
import app.regate.extensions.combine
import app.regate.models.Cupo
import app.regate.models.Instalacion
import app.regate.models.LabelType
import app.regate.util.ObservableLoadingCounter
import app.regate.util.collectStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinTimeZone
import kotlinx.datetime.toLocalDateTime
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import java.time.ZoneId

@Inject
class EstablecimientoReservaViewModel(
    @Assisted savedStateHandle: SavedStateHandle,
    private val updateInstalaciones: UpdateInstalaciones,
    private val establecimientoRepository: EstablecimientoRepository,
    private val instalacionRepository: InstalacionRepository,
    private val cupoRepository: CupoRepository,
    observeSettingEstablecimiento: ObserveSettingEstablecimiento,
    observeEstablecimientoCategory: ObserveInstalacionCategoryCount,
    ):ViewModel() {
    private val establecimientoId: Long = savedStateHandle["id"]!!
    private val now =  Clock.System.now().toLocalDateTime( ZoneId.systemDefault().toKotlinTimeZone() )
    private val loadingState = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val instalacionesAvailables = MutableStateFlow<List<InstalacionAvailable>>(emptyList())
    private val establecimientoCupos = MutableStateFlow<List<CupoEstablecimiento>>(emptyList())
    private val filter = MutableStateFlow(Filter.Default)
    private val selectedTime = MutableStateFlow("0000-01-01T${getHour(now.hour)}:00:00Z".toInstant())
    private val selectedCategory = MutableStateFlow<InstalacionCategoryCount?>(null)

    val state: StateFlow<EstablecimientoReservaState> = combine(
        observeSettingEstablecimiento.flow,
        observeEstablecimientoCategory.flow,
        loadingState.observable,
        instalacionesAvailables,
        establecimientoCupos,
        filter,
        selectedTime,
        selectedCategory
    ){setting,categories,loading,instalacionesA,establecimientoCupos,filter,selectedTime,selectedCategory->
        EstablecimientoReservaState(
            loading = loading,
            instalacionesAvailables = instalacionesA,
            establecimientoCupos = establecimientoCupos,
            filter = filter,
            selectedTime = selectedTime,
            setting=setting,
            categories = categories,
            selectedCategory = selectedCategory
//            selectedTime = selectedTime
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = EstablecimientoReservaState.Empty
    )
    init {
        observeEstablecimientoCategory(ObserveInstalacionCategoryCount.Params(establecimientoId, LabelType.CATEGORIES))
        observeSettingEstablecimiento(ObserveSettingEstablecimiento.Params(establecimientoId))
        viewModelScope.launch {
          filter.collectLatest{
              Log.d("DEBUG_APP","COLLECTING")
              if(it.currentDate != null){
              getEstablecimientoCupos()
              }
            }
        }
        try{
        viewModelScope.launch {
            updateInstalaciones(
                UpdateInstalaciones.Params(establecimientoId)
            ).collectStatus(loadingState,uiMessageManager)
        }
        }catch(e:Exception){
            Log.d("DEBUG_APP_CUPO", e.localizedMessage ?: "")
        }


    }
    private fun getHour(hour:Int):String{
        return if(hour < 10){
            "0$hour"
        }else {
            hour.toString()
        }
    }

    fun getInstalacionesAvailables(start:Instant,cupos: Int){
        val listTime = getArrayofTime(start,cupos -1)
        val listDate = listTime.map { "${filter.value.currentDate?.date} $it" }
        viewModelScope.launch {
        selectedTime.tryEmit(start)
            val request = InstalacionRequest(
                day = filter.value.currentDate?.dayOfWeek?.value,
                date_time = listDate,
                time = listTime,
                establecimiento_id = establecimientoId,
                category_id = filter.value.category_id
            )
            try{
                Log.d("DEBUG_APP_REQUEST",request.toString())
                val res = instalacionRepository.getInstalacionesAvailables(request)
                Log.d("DEBUG_APP",res.toString())
                instalacionesAvailables.tryEmit(res.instalaciones)
            }catch(e:Exception){
                Log.d("DEBUG_APP",e.localizedMessage?:"")
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun getEstablecimientoCupos(){
        viewModelScope.launch {
            try{
            val d = CuposEstablecimientoRequest(
                cupos = (filter.value.minutes / 30).toInt(),
                minutes = filter.value.minutes,
                establecimiento_id = establecimientoId,
                day = filter.value.currentDate?.dayOfWeek?.value,
                date = filter.value.currentDate?.date
            )
            val res = establecimientoRepository.getEstablecimientoCupos(d)
                Log.d("DEBUG_APP",res.toString())
                establecimientoCupos.tryEmit(res)
                val first = res.first { it.enabled }


            getInstalacionesAvailables(first.start_time,first.cupos)

            }catch(e:Exception){
                Log.d("DEBUG_APP",e.localizedMessage?:"")
            }

        }
    }

    //Return array of times ej(["20:00","20:30"])
    fun getArrayofTime(instant: Instant, cupos:Int):List<String>{
        val startTime = instant.toLocalDateTime(TimeZone.UTC)
        val listTime = mutableListOf(startTime.time.toString())
        for (i in 1..cupos){
            val t = startTime.toJavaLocalDateTime().plusMinutes(30L*i).toLocalTime().toString()
            listTime.add(t)
        }
        return listTime.toList()
    }
    fun setTime(date:Long){
        viewModelScope.launch {
            val localDate = Instant.fromEpochMilliseconds(date).toLocalDateTime(TimeZone.UTC    )
            Log.d("DEBUG_APP","lOCAL DATE UPDATED $localDate")
            filter.tryEmit(filter.value.copy(currentDate = localDate))
        }
    }
    fun setIntervalo(minutes:Long){
        viewModelScope.launch {
            filter.tryEmit(filter.value.copy(minutes = minutes))
        }
    }

    fun openBottomSheet(open:(id:Long,establecimientoId:Long)->Unit,price:Int,id:Long){
        try{
        val cupo = establecimientoCupos.value.find { it.start_time == selectedTime.value } ?: return
//            Log.d("DEBUG_APP_CUPO",cupo.toString())
        val listTime = getArrayofTime(cupo.start_time,cupo.cupos -1)
        val listDate = listTime.map { "${filter.value.currentDate?.date}T$it:00Z".toInstant() }
        viewModelScope.launch {
         cupoRepository.insertCuposReserva(listDate,id,(price/listDate.size).toDouble())
            open(id,establecimientoId)
        }
        }catch(e:Exception){
            Log.d("DEBUG_APP",e.localizedMessage?:"mmmm")
        }
    }

    fun setCategory(id:Long){
        viewModelScope.launch {
            filter.tryEmit(filter.value.copy(category_id = id))
            selectedCategory.tryEmit(state.value.categories.find {
                it.category_id == filter.value.category_id.toInt()
            })
        }
    }


    fun clearMessage(id:Long){
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }
}

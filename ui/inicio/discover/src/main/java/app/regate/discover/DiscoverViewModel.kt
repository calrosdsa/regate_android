package app.regate.discover

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessage
import app.regate.api.UiMessageManager
import app.regate.data.common.AddressDevice
import app.regate.data.common.getDataEntityFromJson
import app.regate.data.dto.account.reserva.ReservaDto
import app.regate.data.dto.empresa.instalacion.FilterInstalacionData
import app.regate.data.dto.empresa.instalacion.InstalacionDto
import app.regate.data.instalacion.CupoRepository
import app.regate.data.instalacion.InstalacionRepository
import app.regate.data.reserva.ReservaRepository
import app.regate.domain.observers.ObserveLabelType
import app.regate.extensions.combine
import app.regate.models.Cupo
import app.regate.models.LabelType
import app.regate.models.Labels
import app.regate.settings.AppPreferences
import app.regate.util.ObservableLoadingCounter
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toJavaLocalTime
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject

@Inject
class DiscoverViewModel(
    private val reservaRepository: ReservaRepository,
    private val appPreferences: AppPreferences,
    private val instalacionRepository: InstalacionRepository,
    observeLabelType: ObserveLabelType,
    private val cupoRepository: CupoRepository
//    private val instalacionRepository: InstalacionRepository,
): ViewModel() {
    private val loadingState = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val addressDevice = MutableStateFlow<AddressDevice?>(null)
    private val filterData = MutableStateFlow(FilterInstalacionData())
    private val instalacionResult = MutableStateFlow<List<Pair<InstalacionDto, List<Labels>>>>(
        emptyList()
    )

    val state:StateFlow<DiscoverState> = combine(
        observeLabelType.flow,
        loadingState.observable,
        uiMessageManager.message,
        addressDevice,
        filterData,
        instalacionResult
        ){categories,loading,message,addressDevice,filter,results ->
        DiscoverState(
            loading = loading,
            message= message,
            filter = filter,
            addressDevice = addressDevice,
            categories = categories,
            results = results
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = DiscoverState.Empty
    )

    init{
        observeLabelType(ObserveLabelType.Params(LabelType.CATEGORIES))
        viewModelScope.launch {
//            appPreferences.filter = Json.encodeToString(FilterInstalacionData())
            appPreferences.observeAddress().flowOn(Dispatchers.IO).collectLatest{
                addressDevice.emit(getDataEntityFromJson<AddressDevice>(it))
            }
        }
        viewModelScope.launch {
            appPreferences.observeFilter().flowOn(Dispatchers.IO).collectLatest{
                Log.d("DEBUG_APP",it)
                getDataEntityFromJson<FilterInstalacionData>(it)?.let { it1 -> filterData.emit(it1) }
                getInstalaciones()
            }
        }
    }
    fun openReservaBottomSheet(instalacionId:Long,totalPrice:Int,navigate:()->Unit){
        viewModelScope.launch {
            try{
            val currentDate = Instant.fromEpochMilliseconds(filterData.value.currentDate).toLocalDateTime(TimeZone.UTC).date
            val listTime = getArrayofTime(
                filterData.value.currentTime.toJavaLocalTime(),
                (filterData.value.interval / 30) - 1
            )
                val listDate = listTime.map { "${currentDate}T$it:00Z".toInstant() }
                cupoRepository.insertCuposReserva(
                    dates =listDate,
                    id = instalacionId,
                    price = (totalPrice/listDate.size).toDouble()
                )
                navigate()
            }catch (e:Exception){
                Log.d("DEBUG_APP",e.localizedMessage?:"mmmm")
            }
        }
    }

    fun getInstalaciones() {
        viewModelScope.launch {
            val listTime = getArrayofTime(
                filterData.value.currentTime.toJavaLocalTime(),
                (filterData.value.interval / 30) - 1
            )
            val listDate = listTime.map {
                "${
                    Instant.fromEpochMilliseconds(filterData.value.currentDate).toLocalDateTime(
                        TimeZone.UTC
                    ).date
                } $it"
            }
            try {
                loadingState.addLoader()
                val res = instalacionRepository.filterInstacion(
                    filterData.value.copy(
                        time = listTime, date = listDate,
                        day = Instant.fromEpochMilliseconds(filterData.value.currentDate)
                            .toLocalDateTime(
                                TimeZone.UTC
                            ).dayOfWeek.value,
                        longitud = addressDevice.value?.longitud,
                        latitud = addressDevice.value?.latitud
                    ), 1)
                instalacionResult.tryEmit(res)
                loadingState.removeLoader()
            } catch (e: ResponseException) {
                uiMessageManager.emitMessage(UiMessage(message = e.response.body()))
            } catch (e: Exception) {
                loadingState.removeLoader()
                uiMessageManager.emitMessage(UiMessage(message = e.localizedMessage ?: ""))
            }
        }
    }
    private fun getArrayofTime(time: java.time.LocalTime, interval:Long):List<String>{
        val listTime = mutableListOf(time.toString())
        for (i in 1..interval){
            val t = time.plusMinutes(30L*i).toString()
            Log.d("TIME_",t)
            listTime.add(t)
        }
        Log.d("TIME_",listTime.toString())
        return listTime.toList()
    }

    fun setTime(time:LocalTime){
        appPreferences.filter =  Json.encodeToString(filterData.value.copy(currentTime = time))
    }
    fun setCurrentDate(dateMillis:Long){
        appPreferences.filter =  Json.encodeToString(filterData.value.copy(currentDate = dateMillis))
    }
    fun setCategory(id:Long){
        appPreferences.filter = Json.encodeToString(filterData.value.copy(category_id = id))
    }
    fun setInterval(minutes:Long){
        appPreferences.filter = Json.encodeToString(filterData.value.copy(interval = minutes))
    }
    fun clearMessage(id:Long){
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }
}
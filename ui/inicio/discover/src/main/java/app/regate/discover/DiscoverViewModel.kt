package app.regate.discover

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import app.regate.api.UiMessage
import app.regate.api.UiMessageManager
import app.regate.data.common.AddressDevice
import app.regate.data.common.getDataEntityFromJson
import app.regate.data.dto.account.reserva.ReservaDto
import app.regate.data.dto.empresa.grupo.GrupoDto
import app.regate.data.dto.empresa.instalacion.FilterInstalacionData
import app.regate.data.dto.empresa.instalacion.InstalacionDto
import app.regate.data.instalacion.CupoRepository
import app.regate.data.instalacion.InstalacionRepository
import app.regate.data.reserva.ReservaRepository
import app.regate.domain.observers.ObserveLabelType
import app.regate.domain.pagination.PaginationGroups
import app.regate.domain.pagination.PaginationInstalacionFilter
import app.regate.extensions.combine
import app.regate.models.Cupo
import app.regate.models.LabelType
import app.regate.models.Labels
import app.regate.settings.AppPreferences
import app.regate.util.ObservableLoadingCounter
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.drop
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
import kotlinx.serialization.decodeFromString
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
    private val filterData = MutableStateFlow(FILTER_DATA)
    private val loadingState = ObservableLoadingCounter()
    val pagedList: Flow<PagingData<InstalacionDto>> = Pager(PAGING_CONFIG){
         PaginationInstalacionFilter(isInit = filterData.value.isInit,loadingState = loadingState){page->
                 instalacionRepository.filterInstacion(filterData.value,page)
        }
    }.flow.cachedIn(viewModelScope)
    private val uiMessageManager = UiMessageManager()
    private val addressDevice = MutableStateFlow<AddressDevice?>(null)
    private val selectedCategory = MutableStateFlow<Labels?>(null)

    val state:StateFlow<DiscoverState> = combine(
        observeLabelType.flow,
        loadingState.observable,
        uiMessageManager.message,
        addressDevice,
        filterData,
//        instalacionResult,
        selectedCategory
        ){categories,loading,message,addressDevice,filter,selectedCategory ->
        DiscoverState(
            loading = loading,
            message= message,
            filter = filter,
            addressDevice = addressDevice,
            categories = categories,
//            results = results,
            selectedCategory = selectedCategory
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
                try{
                    val addressUser = getDataEntityFromJson<AddressDevice>(it)
                    Log.d("DEBUG_APP_ADDRESS",it)
                    addressDevice.emit(getDataEntityFromJson<AddressDevice>(it))
//                    if(it.isNotBlank()){
                    val filter = Json.decodeFromString<FilterInstalacionData>(appPreferences.filter)
                    val update = filter.copy(
                        longitud = addressUser?.longitud,
                        latitud = addressUser?.latitud
                    )
                    appPreferences.filter = Json.encodeToString(update)
//                    }
                }catch(e:Exception){
                    Log.d("DEBUG_APP_ERROR",e.localizedMessage?:"")
                }
            }
        }
        viewModelScope.launch {
            appPreferences.observeFilter().flowOn(Dispatchers.IO).collectLatest{
                try{
                Log.d("DEBUG_APP",it)
                getDataEntityFromJson<FilterInstalacionData>(it)?.let {filter->
//                    filterData.emit(filter)
                    getInstalaciones(filter)
                }
                }catch(e:Exception){
                    Log.d("DEBUG_APP_ERROR_ADD",e.localizedMessage?:"")
                }
            }
//            }
        }
//        viewModelScope.launch {
//            filterData.drop(1).collectLatest{
//            getInstalaciones()
//            }
//        }
    }

    companion object {
        val PAGING_CONFIG = PagingConfig(
            pageSize = 20,
            initialLoadSize = 20,
        )
        val FILTER_DATA = FilterInstalacionData()
    }

    fun openReservaBottomSheet(instalacion:InstalacionDto,navigate:()->Unit){
        viewModelScope.launch {
            try{
//                instalacionRepository.saveInstalacion(instalacion)
            val currentDate = Instant.fromEpochMilliseconds(filterData.value.currentDate).toLocalDateTime(TimeZone.UTC).date
            val listTime = getArrayofTime(
                filterData.value.currentTime.toJavaLocalTime(),
                (filterData.value.interval / 30) - 1
            )
                val listDate = listTime.map { "${currentDate}T$it:00Z".toInstant() }
                instalacion.precio_hora?.div(listDate.size)?.let {
                    cupoRepository.insertCuposReserva(
                        dates =listDate,
                        id = instalacion.id,
                        price = it.toDouble(),
                    )
                }
                navigate()
            }catch (e:Exception){
                Log.d("DEBUG_APP",e.localizedMessage?:"mmmm")
            }
        }
    }

    fun getInstalaciones(filter:FilterInstalacionData) {
        viewModelScope.launch {
            val listTime = getArrayofTime(
               filter.currentTime.toJavaLocalTime(),
                (filter.interval / 30) - 1
            )
            Log.d("DEBUG_","list time $listTime")
            val listDate = listTime.map {
                "${Instant.fromEpochMilliseconds(filter.currentDate).toLocalDateTime(TimeZone.UTC).date} $it"
            }
            try {
//                loadingState.addLoader()
//                val address = getAddress()
                val data = filter.copy(
                    time = listTime,
                    date = listDate,
                    isInit = true
                    )
                filterData.emit(data)
//                val res = instalacionRepository.filterInstacion(
//                   data, 1)
//                Log.d("DEBUG_",filter.toString())
////                Log.d("DEBUG_",res.toString())
//                instalacionResult.tryEmit(res)
//                loadingState.removeLoader()
            } catch (e: ResponseException) {
                Log.d("DEBUG_ERROR",e.localizedMessage?:"c")
                uiMessageManager.emitMessage(UiMessage(message = e.response.body()))
            } catch (e: Exception) {
                Log.d("DEBUG_ERROR",e.localizedMessage?:"c")
//                loadingState.removeLoader()
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

    fun setTime(time:LocalTime) {
        appPreferences.filter = Json.encodeToString(
            filterData.value.copy(
                currentTime = time,
            )
        )
    }
    fun setCurrentDate(dateMillis:Long){
//        appPreferences.filter =  Json.encodeToString(filterData.value.copy(currentDate = dateMillis,
//            day_week = (Instant.fromEpochMilliseconds(dateMillis)
//                .toLocalDateTime(
//                    TimeZone.UTC
//                ).dayOfWeek.ordinal)+1))
        appPreferences.filter =  Json.encodeToString(filterData.value.copy(currentDate = dateMillis,
            day_week = Instant.fromEpochMilliseconds(dateMillis)
                .toLocalDateTime(
                    TimeZone.UTC
                ).dayOfWeek.value))
    }
    fun setCategory(id:Long){
        appPreferences.filter = Json.encodeToString(filterData.value.copy(category_id = id))
        viewModelScope.launch {
            val selected = state.value.categories.find{ it.id == id}
            selectedCategory.tryEmit(selected)
        }
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
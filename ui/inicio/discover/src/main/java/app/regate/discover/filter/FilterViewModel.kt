package app.regate.discover.filter

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessage
import app.regate.api.UiMessageManager
import app.regate.data.common.getDataEntityFromJson
import app.regate.data.dto.empresa.instalacion.FilterInstalacionData
import app.regate.domain.observers.ObserveLabelType
import app.regate.models.LabelType
import app.regate.settings.AppPreferences
import app.regate.util.ObservableLoadingCounter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject


@Inject
class FilterViewModel (
    observeLabelType: ObserveLabelType,
    private val appPreferences: AppPreferences,
):ViewModel() {
    private val loadingCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val filterData = MutableStateFlow(FilterInstalacionData())


    val state:StateFlow<FilterState> = combine(
        loadingCounter.observable,
        uiMessageManager.message,
        observeLabelType.flow,
        filterData
    ){loading,message,amenities,filterData->
        FilterState(
            loading = loading,
            message = message,
            amenities = amenities,
            filterData = filterData
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = FilterState()
    )

    init{
        observeLabelType(ObserveLabelType.Params(LabelType.AMENITIES))
        viewModelScope.launch {
            appPreferences.observeFilter().flowOn(Dispatchers.IO).collect{
                getDataEntityFromJson<FilterInstalacionData>(it)?.let { it1 -> filterData.emit(it1) }
            }
        }
    }
    fun setMaxPrice(price:Int){
        appPreferences.filter =  Json.encodeToString(filterData.value.copy(max_price = price))
    }
    fun setAmenities(amenityId:Long){
        val amenities = filterData.value.amenities.toMutableList()
        if(amenities.contains(amenityId)){
            amenities.remove(amenityId)
        }else{
            amenities.add(amenityId)
        }
        appPreferences.filter =  Json.encodeToString(filterData.value.copy(amenities =amenities))
    }
    fun checkPermission(context:Context,permission:String):Boolean{
        val pm: PackageManager = context.packageManager
        val hasPerm = pm.checkPermission(
            permission,
            context.packageName
        )
        return hasPerm == PackageManager.PERMISSION_GRANTED
    }
    fun setLocation(bool:Boolean){
        viewModelScope.launch {
            uiMessageManager.emitMessage(UiMessage(message = "Permission granted"))
        }
        Log.d("GRANTED",bool.toString())
    }

    fun clearMessage(id:Long){
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }
}
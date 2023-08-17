//@file:Suppress("DEPRECATION")

package app.regate.discover.filter

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.IntentSenderRequest
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessageManager
import app.regate.data.common.getDataEntityFromJson
import app.regate.data.dto.empresa.instalacion.FilterInstalacionData
import app.regate.domain.Converter
import app.regate.domain.observers.ObserveLabelType
import app.regate.models.LabelType
import app.regate.settings.AppPreferences
import app.regate.util.AppLocation
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
    private val preferences: AppPreferences,
    private val appLocation: AppLocation,
    private val converter:Converter
):ViewModel() {
    private val loadingCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val filterData = MutableStateFlow(FilterInstalacionData())
    val visiblePermissionDialogQueue = mutableStateListOf<String>()


    val state:StateFlow<FilterState> = combine(
        loadingCounter.observable,
        uiMessageManager.message,
        observeLabelType.flow,
        filterData,
        converter.observeAddress()
    ){loading,message,amenities,filterData,addressDevice->
        FilterState(
            loading = loading,
            message = message,
            amenities = amenities,
            filterData = filterData,
            addressDevice = addressDevice
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = FilterState()
    )

    init{
        observeLabelType(ObserveLabelType.Params(LabelType.CATEGORIES))
        viewModelScope.launch {
            preferences.observeFilter().flowOn(Dispatchers.IO).collect{
                getDataEntityFromJson<FilterInstalacionData>(it)?.let { it1 -> filterData.emit(it1) }
            }
        }

    }
    fun dismissDialog() {
        visiblePermissionDialogQueue.removeFirst()
    }

    fun onPermissionResult(
        permission: String,
        isGranted: Boolean,
        context:Context,
        launcher:(IntentSenderRequest)->Unit,
    ) {
        if(!isGranted && !visiblePermissionDialogQueue.contains(permission)) {
            visiblePermissionDialogQueue.add(permission)
        }
        if(isGranted){
            appLocation.enableLocation(context,launcher)
            appLocation.saveAddress(context as Activity)
        }
    }
    fun setMaxPrice(price:Int){
        preferences.filter =  Json.encodeToString(filterData.value.copy(max_price = price))
    }
    fun setCategories(amenityId:Long){
        val categories = filterData.value.amenities.toMutableList()
        if(categories.contains(amenityId)){
            categories.remove(amenityId)
        }else{
            categories.add(amenityId)
        }
        preferences.filter =  Json.encodeToString(filterData.value.copy(amenities =categories))
    }
    fun clearAlL(){
        preferences.filter = Json.encodeToString(FilterInstalacionData())
    }
    fun setNearMe(bool:Boolean){
        preferences.filter =  Json.encodeToString(filterData.value.copy(
            near_me = bool
        ))
    }
    fun checkPermission(context:Context,permission:String):Boolean{
        val pm: PackageManager = context.packageManager
        val hasPerm = pm.checkPermission(
            permission,
            context.packageName
        )
        return hasPerm == PackageManager.PERMISSION_GRANTED
    }


    fun clearMessage(id:Long){
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }
}

//@file:Suppress("DEPRECATION")

package app.regate.discover.filter

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.regate.api.UiMessage
import app.regate.api.UiMessageManager
import app.regate.data.common.AddressDevice
import app.regate.data.common.getDataEntityFromJson
import app.regate.data.dto.empresa.instalacion.FilterInstalacionData
import app.regate.domain.observers.ObserveLabelType
import app.regate.models.LabelType
import app.regate.settings.AppPreferences
import app.regate.util.AppLocation
import app.regate.util.ObservableLoadingCounter
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task
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
import java.util.Locale


@Inject
class FilterViewModel (
    observeLabelType: ObserveLabelType,
    private val preferences: AppPreferences,
    private val appLocation: AppLocation
):ViewModel() {
    private val loadingCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val filterData = MutableStateFlow(FilterInstalacionData())
    val visiblePermissionDialogQueue = mutableStateListOf<String>()


//    la

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
    fun setAmenities(amenityId:Long){
        val amenities = filterData.value.amenities.toMutableList()
        if(amenities.contains(amenityId)){
            amenities.remove(amenityId)
        }else{
            amenities.add(amenityId)
        }
        preferences.filter =  Json.encodeToString(filterData.value.copy(amenities =amenities))
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

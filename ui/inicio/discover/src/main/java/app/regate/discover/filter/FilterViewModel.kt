//@file:Suppress("DEPRECATION")

package app.regate.discover.filter

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
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
    fun setLocation(bool:Boolean,context:Context,launcher:(IntentSenderRequest)->Unit){
        enableLocation(context,launcher)
        viewModelScope.launch {
            uiMessageManager.emitMessage(UiMessage(message = "Permission granted"))
        }
        Log.d("GRANTED",bool.toString())
    }


//    private var launcher=  registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()){ result->
//        if (result.resultCode == Activity.RESULT_OK) {
//            Log.d("DEBUG_APP", "OK")
//
//        } else {
//            Log.d("DEBUG_APP", "CANCEL")
//        }
//    }
    private fun enableLocation(context:Context,launcher:(IntentSenderRequest)->Unit) {
    try{
        Log.d("DEBUG_APP", "INNITT__")
//        val locationRequest = LocationRequest.create()
//        locationRequest.apply {
//            priority =LocationRequest.PRIORITY_HIGH_ACCURACY
//            interval = 30 * 1000.toLong()
//            fastestInterval = 5 * 1000.toLong()
//        }
        val locationRequest = LocationRequest.Builder(100)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(1000)
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .setMaxUpdateDelayMillis(2000)
                .build()
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)
        val result=
            LocationServices.getSettingsClient(context).checkLocationSettings(builder.build())
        result.addOnCompleteListener {
            try {
                val response: LocationSettingsResponse = it.getResult(ApiException::class.java)
                if(response.locationSettingsStates?.isGpsPresent!!){
                    Log.d("DEBUG_APP_SUCC", response.locationSettingsStates!!.isGpsPresent.toString())
                    //TODO()
                }
                //do something
            }catch (e: ApiException){
                Log.d("DEBUG_APP_LOC",e.localizedMessage?:"")
                when (e.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        val intentSenderRequest =
                            e.status.resolution?.let { it1 -> IntentSenderRequest.Builder(it1).build() }
                        if (intentSenderRequest != null) {
                            launcher(intentSenderRequest)
                        }
                    } catch (e: IntentSender.SendIntentException) {
                        //TODO()
                    }
                }
            }
        }
    }catch(e:Exception){
        Log.d("DEBUG_APP_LO",e.localizedMessage?:"")

    }
    }

//    private fun turnGPSOn(context:Context) {
//        try {
//
//            val locationRequest = LocationRequest.Builder(100)
//                .setWaitForAccurateLocation(false)
//                .setMinUpdateIntervalMillis(1000)
//                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
//                .setMaxUpdateDelayMillis(2000)
//                .build()
//
//
//            val builder = LocationSettingsRequest.Builder()
//                .addLocationRequest(locationRequest)
//            val client: SettingsClient = LocationServices.getSettingsClient(context)
//            val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
//            task.addOnSuccessListener { locationSettingsResponse ->
//                Log.d("DEBUG_APP", locationSettingsResponse.locationSettingsStates.toString())
//                // All location settings are satisfied. The client can initialize
//                // location requests here.
//                // ...
//            }
//
//            task.addOnFailureListener { exception ->
//                if (exception is ResolvableApiException) {
//                    // Location settings are not satisfied, but this can be fixed
//                    // by showing the user a dialog.
//                    try {
//                        // Show the dialog by calling startResolutionForResult(),
//                        // and check the result in onActivityResult().
//                    } catch (sendEx: IntentSender.SendIntentException) {
//                        Log.d("DEBUG_APP_LOC_ERR", sendEx.localizedMessage ?: "")
//                        // Ignore the error.
//                    }
//                }
//            }
//        } catch (err: Exception) {
//            Log.d("DEBUG_APP_LOC_ERR", err.localizedMessage ?: "")
//        }
//
////
//    }

    fun clearMessage(id:Long){
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }
}

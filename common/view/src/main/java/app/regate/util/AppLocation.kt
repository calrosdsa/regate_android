package app.regate.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
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
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import app.regate.data.common.AddressDevice
import app.regate.data.dto.empresa.instalacion.FilterInstalacionData
import app.regate.inject.ApplicationScope
import app.regate.settings.AppPreferences
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.location.Priority
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject
import java.util.Locale

@ApplicationScope
@Inject
class AppLocation(
//    private val activity: Activity,
    private val preferences: AppPreferences
) {
    lateinit var locationManager: LocationManager
    lateinit var locationListener: LocationListener

    fun enableLocation(context: Context, launcher:(IntentSenderRequest)->Unit) {
        try{
            Log.d("DEBUG_APP", "INNITT__")
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

    @SuppressLint("MissingPermission")
    fun saveAddress(activity:Activity) {
        try {
//            activityContext = activity
            locationManager = activity.getSystemService(ComponentActivity.LOCATION_SERVICE) as LocationManager
            if (ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                Log.d("DEBUG_APP","No permiso")
                return
            }
            Log.d("DEBUG_APP","INITTT2")
            locationListener = createLocationListerner(activity)
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f,locationListener)
        }catch (e:Exception){
            Log.d("DEBUG_APP","ERROR MAIN ${e.localizedMessage}")
        }
    }

    private fun createLocationListerner(activity: Activity):LocationListener{
        return object : LocationListener {
            override fun onLocationChanged(location: Location) {
                Log.d("DEBUG_APP","INITTT3")
//            val text = ("" + location.longitude + ":" + location.latitude)
//            Log.d("DEBUG_APP",text)
                if(Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU){
                    getCityNameApi33(location.latitude,location.longitude,activity)
                }else{
                    getCityName(location.latitude,location.longitude,activity)
                }
//            Log.d("DEBUG_APP",city.toString())
            }
            override fun onProviderEnabled(provider: String) {
                Log.d("DEBUG_APP","ENABLED")

            }
            override fun onProviderDisabled(provider: String) {
                Log.d("DEBUG_APP","DISABLED")

            }
        }
    }

//    private val locationListener: LocationListener = object : LocationListener {
//        override fun onLocationChanged(location: Location) {
//            Log.d("DEBUG_APP","INITTT3")
////            val text = ("" + location.longitude + ":" + location.latitude)
////            Log.d("DEBUG_APP",text)
//            if(Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU){
//                getCityNameApi33(location.latitude,location.longitude)
//            }else{
//                getCityName(location.latitude,location.longitude)
//            }
////            Log.d("DEBUG_APP",city.toString())
//        }
//        override fun onProviderEnabled(provider: String) {
//            Log.d("DEBUG_APP","ENABLED")
//
//        }
//        override fun onProviderDisabled(provider: String) {
//            Log.d("DEBUG_APP","DISABLED")
//
//        }
//    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun getCityNameApi33(lat: Double, long: Double,activity: Activity){
        Log.d("DEBUG_APP","INITTT4")
        val geoCoder = Geocoder(activity, Locale.getDefault())
        geoCoder.getFromLocation(lat,long,1,
            object: Geocoder.GeocodeListener{
                override fun onGeocode(addresses: MutableList<Address>) {
                    Log.d("DEBUG_APP","INITTT $addresses")
                    addresses[0].let{
                        Log.d("DEBUG_APP",it.adminArea)
                        Log.d("DEBUG_APP",it.subAdminArea)
                        Log.d("DEBUG_APP",it.subLocality)
                        Log.d("DEBUG_APP",it.getAddressLine(0))
                        val  address = AddressDevice(
                            city = it.locality,
                            locale = it.locale.language,
                            country = it.countryName,
                            country_code = it.countryCode,
                            admin_area = it.adminArea,
                            sub_admin_area = it.subAdminArea,
                            longitud = it.longitude,
                            latitud = it.latitude,
                        )
                        val addressString = Json.encodeToString(address)
                        preferences.address = addressString
//                        val filter = Json.decodeFromString<FilterInstalacionData>(preferences.filter)
//                        val update = filter.copy(
//                            longitud = address.longitud,
//                            latitud = address.latitud
//                        )
//                        preferences.filter = Json.encodeToString(update)
                    }
                    locationManager.removeUpdates(locationListener)
                    // code
                }
                override fun onError(errorMessage: String?) {
                    super.onError(errorMessage)
                    Log.d("DEBUG_APP_ERROR_LOC",errorMessage.toString())
                }
            })
    }
    @Suppress("DEPRECATION")
    private fun getCityName(lat: Double, long: Double,activity: Activity){
        Log.d("DEBUG_APP","INITTT6")
        val geocoder = Geocoder(activity, Locale.getDefault())
        val addresses = geocoder.getFromLocation(lat, long, 1)
        addresses?.get(0)?.let{
            val  address = AddressDevice(
                city = it.locality,
                locale = it.locale.language,
                country = it.countryName,
                country_code = it.countryCode,
                admin_area = it.adminArea,
                sub_admin_area = it.subAdminArea,
                longitud = it.longitude,
                latitud = it.latitude,
            )
            val addressString = Json.encodeToString(address)
            preferences.address = addressString
        }

        locationManager.removeUpdates(locationListener)
    }

}
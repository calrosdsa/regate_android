package app.regate.home

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import app.regate.settings.AppPreferences
import java.util.Locale

abstract class RegateActivity: ComponentActivity() {
    lateinit var locationManager: LocationManager
    lateinit var appPreferences: AppPreferences
    fun saveAddress(preference: AppPreferences) {
        Log.d("DEBUG_APP","INITTT")
        appPreferences = preference
        try {
            locationManager = applicationContext.getSystemService(LOCATION_SERVICE) as LocationManager
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                Log.d("DEBUG_APP","No permiso")
                return
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
        }catch (e:Exception){
            Log.d("DEBUG_APP",e.localizedMessage?:"")
        }
    }


    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
//            val text = ("" + location.longitude + ":" + location.latitude)
//            Log.d("DEBUG_APP",text)
            if(Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU){
                getCityNameApi33(location.latitude,location.longitude)
            }else{
                getCityName(location.latitude,location.longitude)
            }
//            Log.d("DEBUG_APP",city.toString())
        }
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun getCityNameApi33(lat: Double, long: Double){
        val geoCoder = Geocoder(applicationContext, Locale.getDefault())
        geoCoder.getFromLocation(lat,long,1,
            object: Geocoder.GeocodeListener{
                override fun onGeocode(addresses: MutableList<Address>) {
//                   addresses[0].let{
//                    val  address = AddressDevice(
//                        city = it.locality,
//                        locale = it.locale.language,
//                        country = it.countryName,
//                        country_code = it.countryCode
//                    )
//                    val addressString = Json.encodeToString(address)
//                       appPreferences.address = addressString
//                   }
                    locationManager.removeUpdates(locationListener)
                    // code
                }
                override fun onError(errorMessage: String?) {
                    Log.d("DEBUG_APP",errorMessage.toString())
                    super.onError(errorMessage)

                }
            })
    }
    @Suppress("DEPRECATION")
    private fun getCityName(lat: Double, long: Double){
        val geocoder = Geocoder(applicationContext, Locale.getDefault())
        val addresses = geocoder.getFromLocation(lat, long, 1)
        addresses?.get(0)?.let{
//                val  address = AddressDevice(
//                    city = it.locality,
//                    locale = it.locale.language,
//                    country = it.countryName,
//                    country_code = it.countryCode
//                )
//                val addressString = Json.encodeToString(address)
//                appPreferences.address = addressString
        }

        locationManager.removeUpdates(locationListener)
    }
}
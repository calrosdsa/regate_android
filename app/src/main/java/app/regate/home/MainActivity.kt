

package app.regate.home

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.viewModelFactory
import app.regate.ComposeScreens
import app.regate.common.composes.LocalAppDateFormatter
import app.regate.common.composes.theme.RegateTheme
import app.regate.data.common.AddressDevice
import app.regate.extensions.unsafeLazy
import app.regate.inject.ActivityComponent
import app.regate.inject.ActivityScope
import app.regate.inject.ApplicationComponent
import app.regate.map.MapActivity
import app.regate.settings.AppPreferences
import app.regate.util.AppDateFormatter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import java.util.Locale


class MainActivity : ComponentActivity() {
    lateinit var locationManager: LocationManager
    private lateinit var component: MainActivityComponent

    private val preferences: AppPreferences by unsafeLazy { component.preferences }
    private val viewModel: MainActivityViewModel by viewModels {
        viewModelFactory {
            addInitializer(MainActivityViewModel::class) { component.viewModel() }
        }
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermisos()
        saveAddress()
//        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?
        component = MainActivityComponent::class.create(this)
        viewModel
        val establecimientoId = intent.getStringExtra("establecimientoId")
        val mapIntent = Intent(this, MapActivity::class.java)
//        intent.flags = FLAG_ACTIVITY_SINGLE_TOP

        setContent {
            CompositionLocalProvider(
                LocalAppDateFormatter provides component.appDateFormatter
            ) {
                RegateTheme() {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background,
                    ) {
                            HomeEntry(
                                composeScreens = component.screens,
                                establecimientoId = establecimientoId,
                                navigateToMap = { startActivity(mapIntent)}
                            )
//                        }
                    }
                }
            }
        }
    }
    private fun requestPermisos(){
        val permissions = arrayOf<String>(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION // Add other permissions here
        )
        val PERMISSION_REQUEST_CODE = 1
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE)
    }

    fun saveAddress() {
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
        Log.d("DEBUG_APP","INITTT2")
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
        }catch (e:Exception){
            Log.d("DEBUG_APP","ERROR MAIN ${e.localizedMessage}")
        }
    }


    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
             Log.d("DEBUG_APP","INITTT3")
//            val text = ("" + location.longitude + ":" + location.latitude)
//            Log.d("DEBUG_APP",text)
            if(Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU){
                getCityNameApi33(location.latitude,location.longitude)
            }else{
                getCityName(location.latitude,location.longitude)
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

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun getCityNameApi33(lat: Double, long: Double){
        Log.d("DEBUG_APP","INITTT4")
        val geoCoder = Geocoder(applicationContext, Locale.getDefault())
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
                    }
                    locationManager.removeUpdates(locationListener)
                    // code
                }
                override fun onError(errorMessage: String?) {
                    super.onError(errorMessage)

                }
            })
    }
    @Suppress("DEPRECATION")
    private fun getCityName(lat: Double, long: Double){
        Log.d("DEBUG_APP","INITTT6")
        val geocoder = Geocoder(applicationContext, Locale.getDefault())
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


//
@ActivityScope
@Component
abstract class MainActivityComponent(
    @get:Provides val activity: Activity,
    @Component val applicationComponent: ApplicationComponent = ApplicationComponent.from(activity),
) : ActivityComponent {
    abstract val appDateFormatter: AppDateFormatter
    abstract val screens: ComposeScreens
//    abstract val textCreator: TiviTextCreator
    abstract val preferences: AppPreferences
//    abstract val analytics: Analytics
//    abstract val contentViewSetter: ContentViewSetter
//    abstract val login: LoginToTraktInteractor
    abstract val viewModel: () -> MainActivityViewModel
}



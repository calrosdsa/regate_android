package app.regate.util

import android.app.Activity
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import app.regate.data.common.AddressDevice
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject
import java.util.Locale

@Inject
class AppUtil(
    private val activity:Activity
) {
    @Suppress("DEPRECATION")
     fun getAddress(lat: Double, long: Double):Address?{
        Log.d("DEBUG_APP","INITTT6")
        val geocoder = Geocoder(activity.applicationContext, Locale.getDefault())
        val addresses = geocoder.getFromLocation(lat, long, 1)
        return addresses?.get(0)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun getAddressApi33(lat: Double, long: Double):Address?{
        var address:Address? = null
        val geoCoder = Geocoder(activity.applicationContext, Locale.getDefault())
        geoCoder.getFromLocation(lat,long,1,
            object: Geocoder.GeocodeListener{
                override fun onGeocode(addresses: MutableList<Address>) {
                    address = addresses.get(0)
                }
                override fun onError(errorMessage: String?) {
                    super.onError(errorMessage)
                    address = null
                }
            })
        return address
    }

}
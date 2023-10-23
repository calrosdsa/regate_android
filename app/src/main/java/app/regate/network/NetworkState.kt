//@file:Suppress("DEPRECATION")

package app.regate.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi


const val TAG = "DEBUG_APP"
val networkRequest = NetworkRequest.Builder()
    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
    .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
    .build()

private val networkCallback = object : ConnectivityManager.NetworkCallback() {
    // network is available for use
    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        Log.d("DEBUG_APP", "AVAILABLE")

    }

    // Network capabilities have changed for the network
    override fun onCapabilitiesChanged(
        network: Network,
        networkCapabilities: NetworkCapabilities
    ) {
        super.onCapabilitiesChanged(network, networkCapabilities)
        Log.d("DEBUG_APP", "LOST")
//        when(networkCapabilities){
//            NetworkCapabilities.NET_
//        }
//        val unmetered = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
    }

    // lost network connection
    override fun onLost(network: Network) {
        super.onLost(network)
        Log.d("DEBUG_APP", "LOST")
    }
}

@RequiresApi(Build.VERSION_CODES.N)
fun initListener(context: Context){
    val connectivityManager = context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager
    connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network : Network) {
            Log.e(TAG, "The default network is now: " + network)
        }

        override fun onLost(network : Network) {
            Log.e(TAG, "The application no longer has a default network. The last default network was " + network)
        }

        override fun onCapabilitiesChanged(network : Network, networkCapabilities : NetworkCapabilities) {
            Log.e(TAG, "The default network changed capabilities: " + networkCapabilities)
        }

        override fun onLinkPropertiesChanged(network : Network, linkProperties : LinkProperties) {
            Log.e(TAG, "The default network changed link properties: " + linkProperties)
        }
    })

}


fun isOnline(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities =
        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    if (capabilities != null) {
        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            Log.d("DEBUG_APP", "NetworkCapabilities.TRANSPORT_CELLULAR")
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            Log.d("DEBUG_APP", "NetworkCapabilities.TRANSPORT_WIFI")
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
            Log.d("DEBUG_APP", "NetworkCapabilities.TRANSPORT_ETHERNET")
            return true
        }
    }
    return false
}
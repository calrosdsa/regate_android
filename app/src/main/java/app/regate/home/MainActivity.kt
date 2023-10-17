@file:Suppress("DEPRECATION")

package app.regate.home

//import app.regate.R
//import app.regate.map.MapActivity
import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.content.Context
import android.content.IntentSender
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.viewModelFactory
import app.regate.ComposeScreens
import app.regate.common.composes.LocalAppDateFormatter
import app.regate.common.composes.LocalAppUtil
import app.regate.common.composes.theme.RegateTheme
import app.regate.common.composes.util.shouldUseDarkColors
import app.regate.common.composes.util.shouldUseDynamicColors
import app.regate.common.resources.R
import app.regate.constant.Route
import app.regate.extensions.unsafeLazy
import app.regate.inject.ActivityComponent
import app.regate.inject.ActivityScope
import app.regate.inject.ApplicationComponent
import app.regate.settings.AppPreferences
import app.regate.util.AppDateFormatter
import app.regate.util.AppLocation
import app.regate.util.AppMedia
import app.regate.util.AppUtil
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.LocationSettingsStatusCodes
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import java.util.Locale


class MainActivity : ComponentActivity() {
//    lateinit var locationManager: LocationManager
    private lateinit var component: MainActivityComponent

    private val preferences: AppPreferences by unsafeLazy { component.preferences }
    private val viewModel: MainActivityViewModel by viewModels {
        viewModelFactory {
            addInitializer(MainActivityViewModel::class) { component.viewModel() }
        }
    }


//    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val action :String? = intent.action
        val dataUri:Uri? = intent.data
        val grupoId = dataUri?.getQueryParameter("grupoId")
        Log.d("DEBUG_APP_QUERY",grupoId.toString())
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationGroup()
            createNotificationGroupChatChannel()
        }
    
        component = MainActivityComponent::class.create(this)
        viewModel

        val establecimientoId = intent.getStringExtra("establecimientoId")
//        val mapIntent = Intent(this, MapActivity::class.java)
        val startScreen = if(preferences.categories.isBlank()) Route.WELCOME_PAGE else Route.MAIN
//    if(preferences.categories.isBlank()) Route.WELCOME_PAGE else
//        intent.flags = FLAG_ACTIVITY_SINGLE_TOP
        setContent {
            CompositionLocalProvider(
                LocalAppDateFormatter provides component.appDateFormatter,
                LocalAppUtil provides component.appUtil
            ) {
                RegateTheme(
                    useDarkColors = preferences.shouldUseDarkColors(),
                    useDynamicColors = preferences.shouldUseDynamicColors()
                ) {
                val systemUiController = rememberSystemUiController()
                val background = MaterialTheme.colorScheme.background
                val useDark =  !preferences.shouldUseDarkColors()
                LaunchedEffect(key1 = useDark) {
                    systemUiController.setStatusBarColor(
                        color = background,
                        darkIcons = useDark
                    )
                }
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background,
                    ) {
                            HomeEntry(
                                composeScreens = component.screens,
                                establecimientoId = establecimientoId,
//                                navigateToMap = { startActivity(mapIntent)},
                                startScreen = startScreen
                            )
//                        }
                    }
                }
            }
        }
    }



    private var launcher=  registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()){ result->
        if (result.resultCode == Activity.RESULT_OK) {
            Log.d("DEBUG_APP", "OK")
        } else {
            Log.d("DEBUG_APP", "CANCEL")
        }
    }
    private fun enableLocation() {
        val locationRequest = LocationRequest.create()
        locationRequest.apply {
            priority =LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 30 * 1000.toLong()
            fastestInterval = 5 * 1000.toLong()
        }
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)
        val result=
            LocationServices.getSettingsClient(this).checkLocationSettings(builder.build())
        result.addOnCompleteListener {
            try {
                val response: LocationSettingsResponse = it.getResult(ApiException::class.java)
//                println("location>>>>>>> ${response.locationSettingsStates.isGpsPresent}")
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
                        launcher.launch(intentSenderRequest)
                    } catch (e: IntentSender.SendIntentException) {
                        //TODO()
                    }
                }
            }
        }
    }
//    override fun attachBaseContext(newBase: Context) {
//        super.attachBaseContext(ContextWrapper(newBase.setAppLocale("es")))
//    }

    fun Context.setAppLocale(language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        return createConfigurationContext(config)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationGroup(){
        val groupId = getString(R.string.chat_notification_id)
        val groupName = getString(R.string.chat_notification_name)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannelGroup(NotificationChannelGroup(groupId, groupName))

    }
    private fun createNotificationGroupChatChannel() {
        val groupId = getString(R.string.chat_notification_id)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val messageGroupChannel = NotificationChannel(
                getString(R.string.chat_group_channel_id),
                getString(R.string.chat_group_channel_name),
                NotificationManager.IMPORTANCE_HIGH
            )
            messageGroupChannel.group = groupId
            val messageChannel = NotificationChannel(
                getString(R.string.chat_message_channel_id),
                getString(R.string.chat_message_channel_name),
                NotificationManager.IMPORTANCE_HIGH
            )
            messageChannel.group = groupId
            val salaChannel = NotificationChannel(
                getString(R.string.notification_sala_channel),
                getString(R.string.notification_sala),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val billingChannel = NotificationChannel(
                getString(R.string.notification_billing_channel),
                getString(R.string.notification_billing),
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannels(
                mutableListOf(
                    messageChannel, messageGroupChannel,
                    salaChannel, billingChannel
                )
            )
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

}


//
@ActivityScope
@Component
abstract class MainActivityComponent(
    @get:Provides val activity: Activity,
    @Component val applicationComponent: ApplicationComponent = ApplicationComponent.from(activity),
) : ActivityComponent {
    abstract val appDateFormatter: AppDateFormatter
    abstract val appUtil:AppUtil
    abstract val appLocation:AppLocation
    abstract val appMedia:AppMedia
    abstract val screens: ComposeScreens
//    abstract val textCreator: TiviTextCreator
    abstract val preferences: AppPreferences
//    abstract val analytics: Analytics
//    abstract val contentViewSetter: ContentViewSetter
//    abstract val login: LoginToTraktInteractor
    abstract val viewModel: () -> MainActivityViewModel
}



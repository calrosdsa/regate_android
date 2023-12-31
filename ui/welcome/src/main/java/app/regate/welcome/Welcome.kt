package app.regate.welcome

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.platform.LocalContext
import app.regate.common.composes.LocalAppUtil
import app.regate.common.composes.component.dialog.LocationPermissionTextProvider
import app.regate.common.composes.component.dialog.NotificationPermissionTextProvider
import app.regate.common.composes.component.dialog.PermissionDialog
import app.regate.common.composes.viewModel
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import app.regate.welcome.categories.Categories
import app.regate.welcome.location.EnabledLocation
import app.regate.welcome.notifications.EnabledNotification
import app.regate.welcome.started.Started
import kotlinx.coroutines.launch

typealias Welcome= @Composable (
    navigateToHome:() -> Unit,
) -> Unit
@Inject
@Composable
fun Welcome(
    viewModelFactory : () ->WelcomeViewmModel,
    @Assisted navigateToHome: () -> Unit,
) {
    Welcome(
        viewModel = viewModel(factory = viewModelFactory),
        navigateToHome = navigateToHome
    )
}

@Composable
fun Welcome(
    viewModel: WelcomeViewmModel,
    navigateToHome: () -> Unit,
){
    val state by viewModel.state.collectAsState()
    val appUtil = LocalAppUtil.current

    Welcome(
        viewState = state,
        navigateToHome = {
            navigateToHome()
            viewModel.saveCategories()
        },
        addCategory = viewModel::addCategory,
        isRequiredNotificationPermission = appUtil.isRequiredAskForNotificationPermission(),

        dialogQueue = viewModel.visiblePermissionDialogQueue,
        dismissDialog = viewModel::dismissDialog,
        onPermissionResult = viewModel::onPermissionResult,
        onPermissionNotification = viewModel::onPermissionNoticiation
    )
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun Welcome(
    viewState:WelcomeState,
    dialogQueue: SnapshotStateList<String>,
    isRequiredNotificationPermission:Boolean,
    addCategory:(Long)->Unit,
    navigateToHome: () -> Unit,
    dismissDialog:()->Unit,
    onPermissionResult:(String,Boolean,Context,(IntentSenderRequest)->Unit,()->Unit)->Unit,
    onPermissionNotification:(String,Boolean)->Boolean
){
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    val activity = LocalContext.current as Activity
    val locationResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Log.d("DEBUG_APP", "OK")

            } else {
                Log.d("DEBUG_APP", "CANCEL")
            }
        }
    )
    val locationPermission = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { perms ->
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION).forEach { permission ->
               onPermissionResult(
                     permission,
                    perms[permission] == true,
                    activity,
                {intentSenderRequest ->
                    locationResultLauncher.launch(intentSenderRequest)
                },{
                       if(!isRequiredNotificationPermission){
                           navigateToHome()
                       }else{
                           coroutineScope.launch { pagerState.animateScrollToPage(3) }
                       }
                   }
               )
            }
        }
    )

    val notificationPermission = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { perms ->
            arrayOf(Manifest.permission.POST_NOTIFICATIONS).forEach { permission ->
                val isGranted = onPermissionNotification(
                    permission,
                    perms[permission] == true,
                )
                coroutineScope.launch {
                    if(isGranted){
                        navigateToHome()
                    }
                }
            }
        }
    )

    dialogQueue
        .reversed()
        .forEach { permission ->
            PermissionDialog(
                permissionTextProvider = when (permission) {
                    Manifest.permission.ACCESS_COARSE_LOCATION -> {
                        LocationPermissionTextProvider()
                    }
                    Manifest.permission.POST_NOTIFICATIONS ->{
                        NotificationPermissionTextProvider()
                    }
                    else -> return@forEach
                },
                isPermanentlyDeclined = !activity.shouldShowRequestPermissionRationale(
                    permission
                ),
                onDismiss = dismissDialog,
                onOkClick = {
                    dismissDialog()
                    locationPermission.launch(arrayOf(permission))
                },
                onGoToAppSettingsClick = { activity.openAppSettings() }
            )
        }
    BackHandler(enabled = pagerState.currentPage > 0) {
        when(pagerState.currentPage){

            1 -> coroutineScope.launch { pagerState.animateScrollToPage(0) }
            2 -> coroutineScope.launch { pagerState.animateScrollToPage(1) }
            3 -> coroutineScope.launch { pagerState.animateScrollToPage(2) }
        }
    }
    HorizontalPager(
        state = pagerState,
        userScrollEnabled = false,
        pageCount = 4) {page->
        when(page){
            0 -> Started(navigateToPage = { coroutineScope.launch { pagerState.animateScrollToPage(it) } })
            1 -> Categories(
                navigateToPage = {coroutineScope.launch { pagerState.animateScrollToPage(it) }},
                categories = viewState.categories,
                selectedCategories = viewState.selectedCategories,
                addCategory = addCategory
            )
            2 -> EnabledLocation(
                navigateToHome = {
                    if(!isRequiredNotificationPermission){
                    navigateToHome()
                }else{
                    coroutineScope.launch { pagerState.animateScrollToPage(3) }
                    }
                },
                requestLocationPermission = {locationPermission.launch(permissionsToRequest)}
            )
            3 -> EnabledNotification(
//                navigateToHome =  navigateToHome,
                requestPermission = { notificationPermission.launch(arrayOf(Manifest.permission.POST_NOTIFICATIONS)) }
            )
        }
    }
}


fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}

private val permissionsToRequest = arrayOf(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION,
//    Manifest.permission.CALL_PHONE,
)
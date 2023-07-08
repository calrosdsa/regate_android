package app.regate.discover.filter

import android.Manifest
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DismissValue
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.regate.common.composes.components.CustomButton
import app.regate.common.composes.components.input.RowCheckSelectWithImage
import app.regate.common.composes.util.Layout
import app.regate.common.composes.viewModel
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import app.regate.common.resources.R as R

typealias Filter = @Composable (
    navigateUp:() ->Unit
)-> Unit

@Inject
@Composable
fun Filter(
    @Assisted navigateUp: () -> Unit,
    viewModelFactory:()-> FilterViewModel
){
    Filter(viewModel = viewModel(factory =  viewModelFactory),navigateUp)
}

@Composable
internal fun Filter(
    viewModel:FilterViewModel,
    navigateUp: () -> Unit
){
    val viewState by viewModel.state.collectAsState()
    Filter(
        viewState = viewState,
        navigateUp = navigateUp,
        setAmenity = viewModel::setAmenities,
        setMaxPrice = viewModel::setMaxPrice,
        onRequestPermission = viewModel::setLocation,
        checkPermission = viewModel::checkPermission,
        clearMessage = viewModel::clearMessage

    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Filter(
    viewState:FilterState,
    navigateUp: () -> Unit,
    setAmenity:(Long)->Unit,
    setMaxPrice:(Int)->Unit,
    onRequestPermission:(Boolean)->Unit,
    checkPermission:(Context,String)->Boolean,
    clearMessage:(Long)->Unit
) {
    val context = LocalContext.current
    var sliderValue by remember(viewState.filterData.max_price) {
        mutableStateOf(viewState.filterData.max_price?.toFloat() ?: 0f) // pass the initial values
    }
    val locationPermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            onRequestPermission(isGranted)
        }
    )
    var isEnabledLocation by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val dismissSnackbarState = rememberDismissState(
        confirmValueChange = { value ->
            if (value != DismissValue.Default) {
                snackbarHostState.currentSnackbarData?.dismiss()
                true
            } else {
                false
            }
        },
    )
    LaunchedEffect(key1 = true, block = {
        isEnabledLocation = checkPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
    })
    viewState.message?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message.message)
            // Notify the view model that the message has been dismissed
            clearMessage(message.id)
        }
    }
    Scaffold(
        topBar = {
            Column {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(4.dp)
                ) {
                    IconButton(onClick = { navigateUp() }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "closefilter")
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = stringResource(id = R.string.filters),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Divider()
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                SwipeToDismiss(
                    state = dismissSnackbarState,
                    background = { Snackbar(snackbarData = data) },
                    dismissContent = {
                        Snackbar(snackbarData = data)
                    },
//                        Snackbar(snackbarData = data) },
                    modifier = Modifier
                        .padding(horizontal = Layout.bodyMargin)
                        .fillMaxWidth(),
                )
            }
        },
        bottomBar = {
            Column {
                Divider()
                BottomAppBar() {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextButton(onClick = {}) {
                            Text(text = stringResource(id = R.string.clear_all))
                        }
                        CustomButton(onClick = { /*TODO*/ }) {
                            Text(text = "Mostrar $12 canchas")
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(id = R.string.price_range),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "max: ${sliderValue.toInt()}", modifier = Modifier.padding(10.dp),
                style = MaterialTheme.typography.labelLarge
            )
            Slider(
                value = sliderValue,
                onValueChange = { sliderValue_ ->
                    sliderValue = sliderValue_
                },
                onValueChangeFinished = {
                    setMaxPrice(sliderValue.toInt())
                },
                valueRange = 0f..1000f,
                modifier = Modifier.padding(10.dp)
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {
                Text(text = "0", style = MaterialTheme.typography.labelMedium)
                Text(text = "1000", style = MaterialTheme.typography.labelMedium)
            }
            Divider(modifier = Modifier.padding(vertical = 10.dp))

            Text(
                text = stringResource(id = R.string.location),
                style = MaterialTheme.typography.titleMedium
            )
            Surface(onClick = {}, modifier = Modifier.padding(10.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(5.dp)
                ) {
                    RadioButton(selected = isEnabledLocation, onClick = {
                        locationPermissionResultLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
                    })
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = stringResource(id = R.string.near_me))
                }
            }

            Divider(modifier = Modifier.padding(vertical = 10.dp))

            Text(
                text = stringResource(id = R.string.services),
                style = MaterialTheme.typography.titleMedium
            )

            viewState.amenities.map { item ->
                RowCheckSelectWithImage(
                    selected = viewState.filterData.amenities.contains(item.id),
                    onCheck = { setAmenity(item.id) },
                    label = item.name,
                    src = item.thumbnail
                )
            }
        }
    }
}

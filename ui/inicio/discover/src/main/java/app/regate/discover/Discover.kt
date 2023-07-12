package app.regate.discover

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import app.regate.common.composes.LocalAppDateFormatter
import app.regate.common.composes.components.dialog.DatePickerDialogComponent
import app.regate.common.composes.components.dialog.DialogHour2
import app.regate.common.composes.components.input.AmenityItem
import app.regate.common.composes.ui.BottomBar
import app.regate.common.composes.ui.PosterCardImageDark
import app.regate.common.composes.util.Layout
import app.regate.common.composes.viewModel
import app.regate.constant.Route
import app.regate.data.dto.empresa.instalacion.InstalacionDto
import app.regate.models.Labels
import com.commandiron.wheel_picker_compose.WheelTimePicker
import com.commandiron.wheel_picker_compose.core.TimeFormat
import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaLocalTime
import kotlinx.datetime.toKotlinLocalTime
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import java.time.Duration
import java.time.LocalTime
import kotlin.time.Duration.Companion.days
import app.regate.common.resources.R
import app.regate.constant.id

typealias DiscoverScreen = @Composable (
    navController:NavController,
    navigateToMap:()->Unit
//    navigateToSignUpScreen:() -> Unit,
) -> Unit

@Inject
@Composable
fun DiscoverScreen (
    @Assisted navController: NavController,
    @Assisted navigateToMap: () -> Unit,
    viewModelFactory:()->DiscoverViewModel
){
        Discover(
            viewModel = viewModel(factory = viewModelFactory),
            navController= navController,
            navigateToMap = navigateToMap
        )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Discover(
    viewModel:DiscoverViewModel,
    navController:NavController,
    navigateToMap: () -> Unit
){
    val viewState by viewModel.state.collectAsState()
    val formatter = LocalAppDateFormatter.current
    val treshhold = 7.days
    val endDate = (Clock.System.now() + treshhold).toEpochMilliseconds()
    val startDate = (Clock.System.now() - (1.days)).toEpochMilliseconds()
    val showDialog = remember { mutableStateOf(false) }
    val showTimeDialog = remember { mutableStateOf(false) }
    val showDialogIntervalo = remember {
        mutableStateOf(false)
    }
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
    val dateState = rememberDatePickerState(
        initialSelectedDateMillis = Clock.System.now().toEpochMilliseconds()
    )
    LaunchedEffect(key1 = dateState.selectedDateMillis, block = {
        dateState.selectedDateMillis?.let { viewModel.setCurrentDate(it) }
    })
    viewState.message?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message.message)
            // Notify the view model that the message has been dismissed
            viewModel.clearMessage(message.id)
        }
    }
    DialogHour2(
        showDialog = showDialogIntervalo.value,
        dismiss = { showDialogIntervalo.value = false },
        intervalos = viewState.horaIntervalo,
        setIntervalo = viewModel::setInterval,
        minutes = viewState.filter.interval
    )
    DatePickerDialogComponent(
        state = dateState,
        show = showDialog.value,
        dismissDialog = { showDialog.value = false },
        dateValidator = {
            (it in startDate..endDate)
        }
    )
    if(showTimeDialog.value){
    Dialog(onDismissRequest = {showTimeDialog.value = false }) {
        WheelTimePicker(
            timeFormat = TimeFormat.HOUR_24,
            startTime = viewState.filter.currentTime.toJavaLocalTime(),
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surface)
                .padding(10.dp),
            onSnappedTime = { viewModel.setTime(it.toKotlinLocalTime()) }
        )
    }
    }

    Scaffold(
        topBar = {
               HeaderFilter(
                   place = viewState.addressDevice,
                   showDateDialog = {showDialog.value = true},
                   showTimeDialog = { showTimeDialog.value = true },
                   showDialogInterval = {showDialogIntervalo.value = true},
                   date = formatter.formatWithSkeleton(dateState.selectedDateMillis!!, formatter.yearAbbrMonthDaySkeleton),
                   navigateToFilter = {navController.navigate(Route.FILTER)},
                   categories = viewState.categories,
                   currentCategoryId = viewState.filter.category_id,
                   currentTime = viewState.filter.currentTime.toJavaLocalTime(),
                   currentInterval = viewState.filter.interval,
                   setCategory = viewModel::setCategory
                   )
        },
        bottomBar = {
            BottomBar(navController = navController)
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navigateToMap() }) {
                Row(verticalAlignment = Alignment.CenterVertically,modifier = Modifier.padding(horizontal = 10.dp)) {
                Icon(imageVector = Icons.Default.Map, contentDescription = "map_floating",modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = stringResource(id = R.string.map),style = MaterialTheme.typography.labelLarge)
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                SwipeToDismiss(
                    state = dismissSnackbarState,
                    background = {},
                    dismissContent = {
                        Snackbar(snackbarData = data)
                    },
//                        Snackbar(snackbarData = data) },
                    modifier = Modifier
                        .padding(horizontal = Layout.bodyMargin)
                        .fillMaxWidth(),
                )
            }
        }
    ) { paddingValue ->
        Discover(
            viewState = viewState,
            modifier = Modifier.padding(paddingValue)
        ) { instalacionId, establecimientoId, totalPrice ->
            viewModel.openReservaBottomSheet(instalacionId, totalPrice
            ) { navController.navigate(Route.RESERVAR id instalacionId id establecimientoId) }
        }
//        formatterDateReserva = { formatter.formatShortDateTime(it.toInstant())},
//        setIntervalo = viewModel::setIntervalo,

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Discover(
    viewState:DiscoverState,
//    formatterDateReserva:(date:String)->String,
    modifier:Modifier = Modifier,
    navigateToReservaInstalacion:(instalacionId:Long,establecimientoId:Long,totalPrice:Int)->Unit
//    updateCurrentDate:(date:Long)->Unit,
//    setIntervalo:(minutes:Long)->Unit,
    ) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(
            items = viewState.results,
        ) { result ->
            InstalacionResult(instalacion = result.first, amenities = result.second,
                onClick = { navigateToReservaInstalacion(result.first.id,result.first.establecimiento_id
                    ,result.first.precio_hora?:10000) },
            minutes = viewState.filter.interval)
        }
    }
}


@Composable
fun InstalacionResult(
    modifier:Modifier=Modifier,
    instalacion:InstalacionDto,
    onClick:()->Unit,
    amenities:List<Labels>,
    minutes:Long,
) {
    Surface(
        onClick = { onClick() }, modifier = modifier.padding(10.dp),
        shadowElevation = 10.dp, shape = MaterialTheme.shapes.medium
    ) {
        Column() {
            Box() {
                Surface(modifier = Modifier.align(Alignment.BottomEnd)) {
                    Text(text = instalacion.precio_hora.toString())
                }
                PosterCardImageDark(
                    model = instalacion.portada ?: "",
                    modifier = Modifier
                        .height(130.dp)
                        .fillMaxWidth() // 20% of width
                )
                Crossfade(
                    targetState = false, modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                ) {
                    if (it) {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Default.Bookmark,
                                contentDescription = instalacion.name,
                                tint = Color.White
                            )
                        }
                    } else {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Outlined.Bookmark,
                                contentDescription = instalacion.name,
                                tint = Color.White
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.BottomStart)
                ) {

                    Text(
                        text = instalacion.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White

                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = instalacion.distance.toString(),
                            modifier = Modifier.size(16.dp),
                            tint = Color.White
                        )
                        Text(
                            text = "A ${instalacion.distance?.div(1000)} Km de distancia",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White
                        )
                    }
                }
            }
            Column {
                Text(
                    text = "${instalacion.precio_hora} por ${
                        LocalTime.MIN.plus(
                            Duration.ofMinutes(
                                minutes
                            )
                        )
                    } h",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                )
                Text(
                    text = "Comodidades",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(horizontal =10.dp)
                )
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(10.dp),
                ) {
                    items(items = amenities, key = { it.id }) {
                        AmenityItem(amenity = it)
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }
        }
    }
}


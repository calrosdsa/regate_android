package app.regate.discover

import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import app.regate.common.composes.LocalAppDateFormatter
import app.regate.common.composes.component.dialog.DatePickerDialogComponent
import app.regate.common.composes.component.dialog.DialogHour2
import app.regate.common.composes.ui.BottomBar
import app.regate.common.composes.ui.PosterCardImageDark
import app.regate.common.composes.util.Layout
import app.regate.common.composes.util.itemsCustom
import app.regate.common.composes.viewModel
import app.regate.constant.Route
import app.regate.data.dto.empresa.instalacion.InstalacionDto
//import com.commandiron.wheel_picker_compose.WheelTimePicker
//import com.commandiron.wheel_picker_compose.core.TimeFormat
import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaLocalTime
import kotlinx.datetime.toKotlinLocalTime
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import kotlin.time.Duration.Companion.days
import app.regate.common.resources.R
import app.regate.constant.id
import app.regate.discover.timepicker.ShowTimeDialog
import app.regate.discover.timepicker.TimeFormat
import app.regate.discover.timepicker.WheelTimePicker
import app.regate.util.roundOffDecimal
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

typealias DiscoverScreen = @Composable (
    navController:NavController,
//    navigateToSignUpScreen:() -> Unit,
) -> Unit

@Inject
@Composable
fun DiscoverScreen (
    @Assisted navController: NavController,
    viewModelFactory:(SavedStateHandle)->DiscoverViewModel
){
        Discover(
            viewModel = viewModel(factory = viewModelFactory),
            navController= navController,
        )
}
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
internal fun Discover(
    viewModel:DiscoverViewModel,
    navController:NavController,
){
    val lazyPagingItems = viewModel.pagedList.collectAsLazyPagingItems()
    val viewState by viewModel.state.collectAsState()
    val formatter = LocalAppDateFormatter.current
    val isEmpty by remember(key1 = lazyPagingItems.itemCount) {
        derivedStateOf {  lazyPagingItems.itemCount == 0 }
    }
    val treshhold = 7.days
    val now =Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).toInstant(
        TimeZone.UTC)
    val endDate = (now + treshhold).toEpochMilliseconds()
    val startDate = (now - (1.days)).toEpochMilliseconds()
    val showDialog = remember { mutableStateOf(false) }
    val showTimeDialog = remember { mutableStateOf(false) }
//    val showDialogIntervalo = remember {
//        mutableStateOf(false)
//    }
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
    val refreshState = rememberPullRefreshState(refreshing = viewState.loading, onRefresh = {
        lazyPagingItems.refresh()
//        viewModel.getDataFilter()
    })
    val dateState = rememberDatePickerState(
        initialSelectedDateMillis = viewState.filter.currentDate
    )
    LaunchedEffect(key1 = dateState.selectedDateMillis, block = {
        if(viewModel.isDateAvailable()){
            val dateEpoch = viewModel.getDataFilter()
            dateEpoch?.let {date->
            dateState.setSelection(date)
            }
        }else{
            dateState.selectedDateMillis?.let { viewModel.setCurrentDate(it) }
        }
    })
    viewState.message?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message.message)
            // Notify the view model that the message has been dismissed
            viewModel.clearMessage(message.id)
        }
    }
//    DialogHour2(
//        showDialog = showDialogIntervalo.value,
//        dismiss = { showDialogIntervalo.value = false },
//        intervalos = viewState.horaIntervalo,
//        setIntervalo = viewModel::setInterval,
//        minutes = viewState.filter.interval
//    )
    DatePickerDialogComponent(
        state = dateState,
        show = showDialog.value,
        dismissDialog = { showDialog.value = false },
        dateValidator = {
            (it in startDate..endDate)
        }
    )

    ShowTimeDialog(
        show = showTimeDialog.value,
        onDismiss = { showTimeDialog.value = false },
        startTime =  viewState.filter.currentTime,
        setTime = viewModel::setTime,
        endTime = viewState.filter.endTime
    )

    Scaffold(
        topBar = {
               HeaderFilter(
                   place = viewState.addressDevice,
                   showDateDialog = {showDialog.value = true},
                   showTimeDialog = { showTimeDialog.value = true },
//                   showDialogInterval = {showDialogIntervalo.value = true},
                   date = formatter.formatWithSkeleton(dateState.selectedDateMillis!!, formatter.yearAbbrMonthDaySkeleton),
                   navigateToFilter = {navController.navigate(Route.FILTER)},
                   categories = viewState.categories,
                   currentTime = viewState.filter.currentTime.toJavaLocalTime(),
                   endTime = viewState.filter.endTime.toJavaLocalTime(),
//                   currentInterval = viewState.filter.interval,
                   setCategory = viewModel::setCategory,
                   selectedCategory = viewState.selectedCategory
                   )
        },
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
                        .fillMaxSize(),
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .pullRefresh(state = refreshState)
            .fillMaxSize()) {
            Discover(
                viewState = viewState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                lazyPagingItems = lazyPagingItems,
            ) { instalacion ->
                viewModel.openReservaBottomSheet(
                    instalacion
                ) { navController.navigate(Route.RESERVAR id instalacion.id id instalacion.establecimiento_id) }
            }

            if(isEmpty){
                Text(text = stringResource(id = R.string.no_result_found),
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .align(Alignment.Center), style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center)
            }
            PullRefreshIndicator(
                refreshing = viewState.loading,
                state = refreshState,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(paddingValues)
                    .padding(top = 20.dp),
                scale = true,
            )
        }
//        formatterDateReserva = { formatter.formatShortDateTime(it.toInstant())},
//        setIntervalo = viewModel::setIntervalo,

    }
}

@Composable
internal fun Discover(
    viewState:DiscoverState,
//    formatterDateReserva:(date:String)->String,
    modifier:Modifier = Modifier,
    lazyPagingItems: LazyPagingItems<InstalacionDto>,
    navigateToReservaInstalacion:(InstalacionDto)->Unit
//    updateCurrentDate:(date:Long)->Unit,
//    setIntervalo:(minutes:Long)->Unit,
    ) {
    val isAddressDevice by remember(viewState.addressDevice) {
        derivedStateOf {
            viewState.addressDevice != null
        }
    }

    LaunchedEffect(key1 = viewState.filter, block = {
        Log.d("DEBUG_APP_FILTER",viewState.filter.toString())
        if(viewState.filter.isInit){
           lazyPagingItems.refresh()
        }
    })
    LazyColumn(modifier = modifier.fillMaxSize()) {
        itemsCustom(
            items = lazyPagingItems,
        ) { result ->
            if (result != null) {
                InstalacionResult(instalacion = result,
                    onClick = { navigateToReservaInstalacion(result) },
        //                onClick = { navigateToReservaInstalacion(result.first.id,result.first.establecimiento_id
        //                    ,result.first.precio_hora?:10000) },
                    isAddressDevice = isAddressDevice
        //            minutes = viewState.filter.interval
                )
            }
        }

    }
}


@Composable
fun InstalacionResult(
    modifier:Modifier=Modifier,
    instalacion:InstalacionDto,
    onClick:()->Unit,
    isAddressDevice:Boolean,
//    amenities:List<Labels>,
//    minutes:Long,
) {
    Surface(
        onClick = { onClick() }, modifier = modifier.padding(10.dp),
        shadowElevation = 10.dp, shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.inverseOnSurface
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
                PriceLabel(precio = instalacion.precio_hora.toString(),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(y = 4.dp))

                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(0.7f)
                        .align(Alignment.BottomStart)
                ) {

                    Text(
                        text = instalacion.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    if(isAddressDevice){
                    instalacion.distance?.div(1000)?.let {
                        roundOffDecimal(it)?.let { distance ->
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
                                    text = "A $distance Km de distancia",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White
                                )
                            }
                        }
                    }
                    }
                }
            }
//            Column {
//                Text(
//                    text = "${instalacion.precio_hora} por ${
//                        LocalTime.MIN.plus(
//                            Duration.ofMinutes(
//                                minutes
//                            )
//                        )
//                    } h",
//                    style = MaterialTheme.typography.labelLarge,
//                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
//                )
//                Text(
//                    text = "Comodidades",
//                    style = MaterialTheme.typography.labelMedium,
//                    modifier = Modifier.padding(horizontal =10.dp)
//                )
//                LazyRow(
//                    modifier = Modifier
//                        .fillMaxWidth(),
//                    contentPadding = PaddingValues(10.dp),
//                ) {
//                    items(items = amenities, key = { it.id }) {
//                        AmenityItem(amenity = it)
//                        Spacer(modifier = Modifier.width(8.dp))
//                    }
//                }
//            }
        }
    }
}



@Composable
fun PriceLabel(
    precio:String,
    modifier:Modifier=Modifier,
){
    Surface(
        shape = RoundedCornerShape(topStart = 10.dp),
        border = BorderStroke(1.dp, Color.White),
        modifier = modifier,
        color = MaterialTheme.colorScheme.primary
    ) {
        Box(modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)) {
            Text(
                text = precio,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}



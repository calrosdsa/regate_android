package app.regate.reservar

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.lifecycle.SavedStateHandle
import app.regate.common.composes.LocalAppDateFormatter
import app.regate.common.composes.components.card.InstalacionCard
import app.regate.common.composes.components.dialog.CategoryDialog
import app.regate.common.composes.components.dialog.DatePickerDialogComponent
import app.regate.common.composes.components.dialog.DialogHour
import app.regate.common.composes.components.images.AsyncImage
import app.regate.common.composes.viewModel
import app.regate.common.resources.R
import app.regate.data.dto.empresa.instalacion.InstalacionAvailable
import app.regate.models.Instalacion
import app.regate.models.Labels
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import kotlin.time.Duration.Companion.days


typealias EstablecimientoReserva = @Composable (
    navigateToReservaDetail:(id:Long,establecimientoId:Long) -> Unit,
    category:Long
) -> Unit
//
//@Inject

@Inject
@Composable
fun EstablecimientoReserva (
    viewModelFactory:(SavedStateHandle)->EstablecimientoReservaViewModel,
    @Assisted navigateToReservaDetail: (id: Long,establecimientoId:Long) -> Unit,
    @Assisted category: Long
) {
    EstablecimientoReserva(
        viewModel = viewModel(factory = viewModelFactory),
        navigateToReservaDetail = navigateToReservaDetail,
        category = category
    )
}

@Composable
internal fun EstablecimientoReserva(
    viewModel:EstablecimientoReservaViewModel,
    navigateToReservaDetail: (id: Long,establecimientoId:Long) -> Unit,
    category:Long
){
    val viewState by viewModel.state.collectAsState()
    val formatter = LocalAppDateFormatter.current
    LaunchedEffect(key1 = Unit, block = {
        viewModel.setCategory(category)
    })
    EstablecimientoReserva(
//        navigateToReservaDetail = { navigateToReservaDetail(it)},
        state = viewState,
        formatShortTime = {
            formatter.formatShortTime(it)},
        formatDate = {formatter.formatWithSkeleton(it,formatter.yearAbbrMonthDaySkeleton)},
        getInstalacionesAvailables = viewModel::getInstalacionesAvailables,
        updateCurrentDate = viewModel::setTime,
        setIntervalo = viewModel::setIntervalo,
        openBottomSheet = {price,id->
            viewModel.openBottomSheet(navigateToReservaDetail,price,id)
        },
        setCategory = viewModel::setCategory
    )


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EstablecimientoReserva(
//    navigateToReservaDetail:(id:Long)->Unit,
    state:EstablecimientoReservaState,
    formatShortTime:(time: Instant) ->String,
    formatDate:(date:Long)->String,
    getInstalacionesAvailables:(start:Instant,cupos:Int)->Unit,
    updateCurrentDate:(date:Long)->Unit,
    setIntervalo:(minutes:Long)->Unit,
    openBottomSheet:(price:Int,id:Long)->Unit,
    setCategory:(Long)->Unit

) {
    val treshhold = 7.days
    val endDate = (Clock.System.now() + treshhold).toEpochMilliseconds()
    val startDate = (Clock.System.now() - (1.days)).toEpochMilliseconds()
    val showDialog = remember { mutableStateOf(false) }
    val dateState = rememberDatePickerState(
        initialSelectedDateMillis = Clock.System.now().toEpochMilliseconds()
    )
    val showDialogIntervalo = remember {
        mutableStateOf(false)
    }
    val horarioScrollState = rememberLazyListState()
    val showCategoryDialog = remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = state.establecimientoCupos, block = {
        if (state.establecimientoCupos.isNotEmpty()) {
            val firstIndex = state.establecimientoCupos.indexOfFirst { it.enabled }
            if (firstIndex >= 0) horarioScrollState.animateScrollToItem(firstIndex)

        }
    })
    LaunchedEffect(key1 = dateState.selectedDateMillis, block = {
        dateState.selectedDateMillis?.let { updateCurrentDate(it) }
    })
    CategoryDialog(
        showDialog = showCategoryDialog.value,
        selectedCategory = state.selectedCategory.let{
            Labels(id = it?.category_id?.toLong()?:0,name= it?.category_name.toString(),thumbnail = it?.thumbnail)
                                                     },
        categories = state.categories.map {
            Labels(id = it.category_id?.toLong()?:0L,name=it.category_name,thumbnail = it.thumbnail)
                                          },
        closeDialog = { showCategoryDialog.value = false },
        setCategory ={  setCategory(it) }
    )


    DialogHour(
        showDialog = showDialogIntervalo.value,
        dismiss = { showDialogIntervalo.value = false },
        intervalos = state.setting?.horario_interval,
        setIntervalo = setIntervalo,
        minutes = state.filter.minutes
    )
    DatePickerDialogComponent(
        state = dateState,
        show = showDialog.value,
        dismissDialog = { showDialog.value = false },
        dateValidator = {
            (it in startDate..endDate)
        }
    )


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp, vertical = 10.dp)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp, vertical = 10.dp),
            shape = MaterialTheme.shapes.medium,
            shadowElevation = 10.dp,
//                    tonalElevation = 5.dp
        ) {
            Column() {

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    item {
                        Surface(
                            onClick = { showCategoryDialog.value = true },
                            modifier = Modifier
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(5.dp)
                                    .size(40.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "add-sport",
                                    modifier = Modifier
                                        .zIndex(1f)
                                        .size(8.dp)
                                        .align(Alignment.TopEnd)
                                )
                                AsyncImage(
                                    model = state.selectedCategory?.thumbnail ?: "",
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(30.dp)
                                        .align(Alignment.Center),
                                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                                )
                            }
                        }
                    }
                    item {
                        Divider(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(1.dp)
                        )
                    }
                    item {
                        Surface(onClick = { showDialog.value = true }, modifier = Modifier) {
                            Column(
                                modifier = Modifier.padding(10.dp)
                            ) {
                                Text(
                                    text = "Date",
                                    style = MaterialTheme.typography.labelSmall
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = dateState.selectedDateMillis?.let { formatDate(it) }
                                        .toString(),
                                    style = MaterialTheme.typography.labelMedium,
                                )
                            }
                        }
                    }
                    item {
                        Divider(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(1.dp)
                        )
                    }
                    item {

                        Surface(
                            onClick = { showDialogIntervalo.value = true },
                            modifier = Modifier
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.time_interval),
                                    style = MaterialTheme.typography.labelSmall
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = "${state.filter.minutes}mn",
                                    style = MaterialTheme.typography.labelMedium,
                                )
                            }
                        }
                    }
                }


            }
        }
        if (state.establecimientoCupos.isNotEmpty()) {
            Text(
                text = stringResource(id = R.string.choose_a_schedule),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            LazyRow(
                contentPadding = PaddingValues(5.dp),
                state = horarioScrollState
            ) {
                items(items = state.establecimientoCupos) {
                    OutlinedButton(
                        modifier = Modifier.padding(5.dp),
                        onClick = { getInstalacionesAvailables(it.start_time, it.cupos) },
                        enabled = it.enabled,
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (state.selectedTime == it.start_time) MaterialTheme.colorScheme.primary else Color.Transparent,
                            contentColor = if (state.selectedTime == it.start_time) Color.White else MaterialTheme.colorScheme.primary

                        )
                    ) {
                        Text(
                            text = "${formatShortTime(it.start_time)} a ${formatShortTime(it.end_time)}",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                }
            }

            Text(
                text = "Canchas disponibles", style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            LazyRow(modifier = Modifier.fillMaxSize()) {
                items(
                    items = state.instalacionesAvailables,
//                key = { it.id }
                ) { instalacion ->
                    InstalacionAvailable(
                        instalacion = instalacion,
                        navigate = {
                            openBottomSheet(
                                instalacion.precio,
                                instalacion.instalacion_id
                            )
                        },
                        modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .height(155.dp)
                            .clip(MaterialTheme.shapes.large)
                            .width(220.dp)
                    )
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = stringResource(id = R.string.there_are_not_available_schudules),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun InstalacionAvailable(
    instalacion: InstalacionAvailable,
    navigate: (id: Long) -> Unit,
    modifier: Modifier = Modifier,
    imageHeight: Dp = 110.dp,
    content: @Composable() (ColumnScope.() -> Unit) = {}
) {
    ElevatedCard(
            modifier = modifier,
            onClick = { navigate(instalacion.instalacion_id) }
    ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight)
            ) {
                AsyncImage(
                    model = instalacion.portada,
                    requestBuilder = { crossfade(true) },
                    contentDescription = instalacion.portada,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
                Surface(
                    shape = RoundedCornerShape(topEnd = 10.dp),
                    border = BorderStroke(1.dp, Color.White),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .offset(y = 4.dp),
                    color = MaterialTheme.colorScheme.primary
                ) {
                    Box(modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)) {
                        Text(
                            text = instalacion.precio.toString(),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
            Column(modifier = Modifier.padding(5.dp)) {
                Text(
                    text = instalacion.name, style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                )
                Spacer(modifier = Modifier.height(5.dp))
                content()
            }

        }
    }



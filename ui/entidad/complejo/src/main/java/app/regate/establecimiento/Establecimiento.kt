package app.regate.establecimiento

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.SavedStateHandle
import app.regate.common.composes.components.images.AsyncImage
import app.regate.common.composes.ui.PosterCardImage
import app.regate.common.composes.util.Layout
import app.regate.common.composes.viewModel
import app.regate.common.resources.R
import app.regate.models.Establecimiento
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import moe.tlaster.nestedscrollview.VerticalNestedScrollView
import moe.tlaster.nestedscrollview.rememberNestedScrollViewState


typealias Establecimiento = @Composable (
    navigateUp:()->Unit,
//    navigateToInstalacion:(id:Long) -> Unit,
    actividades:@Composable () -> Unit,
    reservar:@Composable (category:Long) -> Unit,
    salas:@Composable () -> Unit,
    currentPage:Int,
    ) -> Unit

@Inject
@Composable
fun Establecimiento(
    viewModelFactory:(SavedStateHandle)-> EstablecimientoViewModel,
    @Assisted navigateUp:()->Unit,
//    @Assisted navigateToInstalacion: (id: Long) -> Unit,
    @Assisted actividades:@Composable () -> Unit,
    @Assisted reservar:@Composable (category:Long) -> Unit,
    @Assisted salas:@Composable () -> Unit,
    @Assisted currentPage:Int,
    ){
    Establecimiento(
        viewModel = viewModel(factory = viewModelFactory),
        navigateUp=navigateUp ,
//        navigateToInstalacion =  navigateToInstalacion,
        actividades = actividades,
        reservar = reservar,
        salas = salas,
        currentPage = currentPage,
    )
}


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun Establecimiento(
    viewModel: EstablecimientoViewModel,
    navigateUp: () -> Unit,
//    navigateToInstalacion: (id: Long) -> Unit,
    actividades:@Composable () -> Unit,
    reservar:@Composable (category:Long) -> Unit,
    salas:@Composable () -> Unit,
    currentPage: Int,
    ) {
    val sheetState = rememberStandardBottomSheetState(initialValue = SheetValue.Hidden)
    val category = remember{ mutableStateOf(0L) }
    val state by viewModel.state.collectAsState()
    val pagerState = rememberPagerState(initialPage = currentPage)
    val coroutineScope = rememberCoroutineScope()
    val nestedScrollViewState = rememberNestedScrollViewState()
    val snackbarHostState = remember { SnackbarHostState() }

//    var headerO by remember{ mutableStateOf(1f) }
    val maxOffset = remember{
        derivedStateOf{ nestedScrollViewState.maxOffset }
    }
    val color = remember { Animatable(Color.Transparent) }
    val topBarColor = MaterialTheme.colorScheme.inverseOnSurface
    BackHandler(true) {
        navigateUp()
    }
    LaunchedEffect(key1 = nestedScrollViewState.offset, block = {
        if (-maxOffset.value == nestedScrollViewState.offset) {
            color.animateTo(topBarColor, animationSpec = tween(500))
        } else {
            color.animateTo(Color.Transparent, animationSpec = tween(500))
        }
    })
    state.message?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message.message)
            // Notify the view model that the message has been dismissed
            viewModel.clearMessage(message.id)
        }
    }

    if(sheetState.isVisible){

    ModalBottomSheet(onDismissRequest = {coroutineScope.launch { sheetState.hide() } }) {
        Column(modifier = Modifier.fillMaxSize().padding(10.dp)){
            PosterCardImage(model = stringResource(id = R.string.location_static_url),
                modifier = Modifier
                    .clickable {

                    }
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(10.dp))
            Text(text = state.establecimiento?.address ?: "",
                style = MaterialTheme.typography.titleSmall)
        }
    }
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color.value),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { navigateUp() }, modifier = Modifier
                        .zIndex(1f)
                        .padding(5.dp)
                        .clip(CircleShape)
                        .background(topBarColor)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "mdakd",
                        modifier = Modifier.size(20.dp)
                    )
                }
                    Spacer(modifier = Modifier.width(5.dp))
                    AnimatedVisibility(visible = -maxOffset.value == nestedScrollViewState.offset) {
                    Text(text = state.establecimiento?.name?:"",style = MaterialTheme.typography.labelLarge)
                    }
                }
                Row() {
                IconButton(
                    onClick = { coroutineScope.launch { sheetState.show() }}, modifier = Modifier
                        .zIndex(1f)
                        .padding(5.dp)
                        .clip(CircleShape)
                        .background(topBarColor)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ThumbUp,
                        contentDescription = "thumpup",
                        modifier = Modifier.size(20.dp)
                    )
                }
                    IconButton(
                        onClick = { navigateUp() }, modifier = Modifier
                            .zIndex(1f)
                            .padding(5.dp)
                            .clip(CircleShape)
                            .background(topBarColor)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "share",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    )
    { paddingValues ->
        VerticalNestedScrollView(
            modifier = Modifier.padding(paddingValues),
//                .nestedScroll(nested),
            state = nestedScrollViewState,
            header = {

                Layout(modifier =  Modifier.height(100.dp),content ={
                state.establecimiento?.let {
                    HeaderEstablecimiento(
                        establecimiento = it,
//                        modifier = Modifier.graphicsLayer {
//                            alpha = headerO.coerceIn(0f,1f)
//                        }
                    )
                }
                }){
                        measurables, constraints ->
                    val placeables = measurables.map { measurable ->
                        measurable.measure(
                            Constraints.fixed(width = constraints.maxWidth
                            ,height = constraints.maxHeight+200))
                    }
                    layout(width= constraints.maxWidth,height = constraints.maxHeight -200) {
                        placeables.forEach { placeable ->
                            placeable.placeRelativeWithLayer(x =0, y = -275)
                        }
                    }
                }

            }
        ) {
            Column() {

                Indicators(navToTab = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(it)
                    }
                }, currentTab = pagerState.currentPage)
                HorizontalPager(
                    pageCount = 4, state = pagerState,
                    userScrollEnabled = false
                ) { page ->
                    when (page) {
                        0 -> {
                            EstablecimientoPage(
                                state = state,
                                openLocationSheet = {coroutineScope.launch { sheetState.show() }},
                                navigateToReserva = {
                                    category.value = it
                                    coroutineScope.launch { pagerState.animateScrollToPage(1) } }
                            )
                        }
                        1 -> reservar(category.value)
                        2 -> salas()
                        3 -> actividades()

                    }
                }
                state.establecimiento?.let { establecimiento ->
                    Text(text = establecimiento.name)
                }
            }
        }
    }
}


@Composable
fun HeaderEstablecimiento(
    establecimiento: Establecimiento,
    modifier:Modifier = Modifier,
){
//    val isLike = remember {
//        mutableStateOf(false)
//    }
    Column(modifier = modifier) {
            AsyncImage(
                model = establecimiento.photo,
                requestBuilder = { crossfade(true) },
                contentDescription = establecimiento.photo,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp),
                contentScale = ContentScale.Crop,
            )

//        Row(
//            verticalAlignment = Alignment.CenterVertically, modifier = Modifier
//                .fillMaxWidth()
//                .height(100.dp)
//                .padding(10.dp),
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Row(
//                verticalAlignment = Alignment.CenterVertically, modifier = Modifier
//            ) {
//                AsyncImage(
//                    model = establecimiento.portada,
//                    requestBuilder = { crossfade(true) },
//                    contentDescription = establecimiento.portada,
//                    modifier = Modifier
//                        .size(80.dp)
//                        .clip(CircleShape),
//                    contentScale = ContentScale.Crop,
//                )
//                Spacer(modifier = Modifier.width(15.dp))
//                Text(
//                    text = establecimiento.name,
//                    style = MaterialTheme.typography.titleMedium
//                )
//            }
//            Crossfade(targetState = isLike.value) {
//            IconButton(onClick = { isLike.value = !it }) {
//                Icon(imageVector = if(it)Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp, contentDescription = "thum_up")
//            }
//            }
//        }

    }
}


@Composable
fun Indicators(
    navToTab:(tab:Int) ->Unit,
    currentTab:Int,
    modifier:Modifier = Modifier
){
//    val widthTab = LocalConfiguration.current.screenWidthDp.dp /4
    ScrollableTabRow(selectedTabIndex = currentTab, edgePadding = 1.dp,
        modifier = modifier) {
//    Row(modifier = Modifier,
//    horizontalArrangement = Arrangement.SpaceBetween) {
        Tab(
            text = { Text(text = "Info", style = MaterialTheme.typography.labelMedium) },
            selected = currentTab == 0,
            onClick = {
                navToTab(0)
                // Animate to the selected page when clicked
            },
//            modifier = Modifier.width(widthTab)
            )
        Tab(
            text = { Text(text = "Reservar",style = MaterialTheme.typography.labelMedium) },
            selected = currentTab ==1,
            onClick = {
                // Animate to the selected page when clicked
                navToTab(1)
            },
//            modifier = Modifier.width(widthTab)
//            modifier = Modifier.size(20.dp)
        )
        Tab(
            text = { Text(text = "Salas", style = MaterialTheme.typography.labelMedium) },
            selected = currentTab ==2,
            onClick = {
                // Animate to the selected page when clicked
                navToTab(2)
            },
//            modifier = Modifier.width(widthTab)
        )
        Tab(
            text = { Text(text = "Actividades", style = MaterialTheme.typography.labelMedium) },
            selected = currentTab ==3,
            onClick = {
                // Animate to the selected page when clicked
                navToTab(3)
            },
//            modifier = Modifier.width(widthTab)
        )

//
//    }
    }
}

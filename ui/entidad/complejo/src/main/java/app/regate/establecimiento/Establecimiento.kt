package app.regate.establecimiento

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
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
import androidx.navigation.NavController
import app.regate.common.composes.LocalAppUtil
import app.regate.common.composes.component.images.AsyncImage
import app.regate.common.composes.ui.PosterCardImage
import app.regate.common.composes.viewModel
import app.regate.common.resources.R
import app.regate.constant.Route
import app.regate.constant.id
import app.regate.data.auth.AppAuthState
import app.regate.data.common.encodeMediaData
import app.regate.models.establecimiento.Establecimiento
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import moe.tlaster.nestedscrollview.VerticalNestedScrollView
import moe.tlaster.nestedscrollview.rememberNestedScrollViewState


typealias Establecimiento = @Composable (
    actividades:@Composable () -> Unit,
    reservar:@Composable (category:Long) -> Unit,
    salas:@Composable () -> Unit,
    establecimientoPhotos:@Composable () -> Unit,
    currentPage:Int,
    navController:NavController,
//    openAuthBottomSheet:()->Unit,
    ) -> Unit

@Inject
@Composable
fun Establecimiento(
    viewModelFactory: (SavedStateHandle) -> EstablecimientoViewModel,
    @Assisted actividades: @Composable () -> Unit,
    @Assisted reservar: @Composable (category: Long) -> Unit,
    @Assisted salas: @Composable () -> Unit,
    @Assisted establecimientoPhotos: @Composable () -> Unit,
    @Assisted currentPage: Int,
    @Assisted navController:NavController,
//    @Assisted openAuthBottomSheet: () -> Unit,
) {


    Establecimiento(
        viewModel = viewModel(factory = viewModelFactory),
//        navigateToInstalacion =  navigateToInstalacion,
        actividades = actividades,
        reservar = reservar,
        salas = salas,
        establecimientoPhotos =establecimientoPhotos,
        currentPage = currentPage,
        navigateToPhoto = {
            navController.navigate(Route.PHOTO id it)
        },
        navigateUp = { navController.navigateUp() },
        navigateToProfile = { navController.navigate(Route.PROFILE id it)},
        navigateToReviews = { navController.navigate(Route.REVIEWS id it)},
        navigateToCreateReview = { navController.navigate(Route.CREATE_REVIEW id it)},
        openAuthBottomSheet = {navController.navigate(Route.AUTH_DIALOG)},
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
    establecimientoPhotos:@Composable () -> Unit,
    currentPage: Int,
    navigateToPhoto: (String) -> Unit,
    navigateToProfile: (Long) -> Unit,
    navigateToReviews: (Long) -> Unit,
    navigateToCreateReview: (Long) -> Unit,
    openAuthBottomSheet:()-> Unit,
    ) {
    val appUtil = LocalAppUtil.current
    val sheetState = rememberStandardBottomSheetState(initialValue = SheetValue.Hidden, skipHiddenState = false)
    val sheetStateAttentionSchedule = rememberStandardBottomSheetState(initialValue = SheetValue.Hidden, skipHiddenState = false)
    val category = remember { mutableStateOf(0L) }
    val state by viewModel.state.collectAsState()
    val secondState by viewModel.secondState.collectAsState()
    val pagerState = rememberPagerState(initialPage = currentPage)
    val coroutineScope = rememberCoroutineScope()
    val nestedScrollViewState = rememberNestedScrollViewState()
    val snackbarHostState = remember { SnackbarHostState() }
    val maxOffset = remember {
        derivedStateOf { nestedScrollViewState.maxOffset }
    }
    val color = remember { Animatable(Color.Transparent) }
    val topBarColor = MaterialTheme.colorScheme.inverseOnSurface
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

    BackHandler(true) {

        navigateUp()
    }

    if (sheetStateAttentionSchedule.isVisible) {
        ModalBottomSheet(onDismissRequest = { coroutineScope.launch { sheetStateAttentionSchedule.hide() } }) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
            ) {
                Text(text = "Horarios de atención",style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 10.dp))
                secondState.attentionScheduleWeek.map {schedule->
                AttentionScheduleItem(item = schedule)
                }
            }
        }
    }
    if (sheetState.isVisible) {
        ModalBottomSheet(onDismissRequest = { coroutineScope.launch { sheetState.hide() } }) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
            ) {
                PosterCardImage(model = stringResource(id = R.string.location_static_url),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(10.dp),
                    onClick = {
                        val lat = state.establecimiento?.latidud
                        val lng = state.establecimiento?.longitud
                        appUtil.openMap(lng, lat, state.establecimiento?.name)
                    }
                )
                Text(
                    text = state.establecimiento?.address ?: "",
                    style = MaterialTheme.typography.titleSmall
                )
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
                        Text(
                            text = state.establecimiento?.name ?: "",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
                Row() {
                    Crossfade(targetState = state.isFavorite) {
                        if (it) {
                            IconButton(
                                onClick = {
                                    if(state.authState == AppAuthState.LOGGED_IN){
                                    coroutineScope.launch { viewModel.removeLike() }
                                    }else{
                                        openAuthBottomSheet()
                                    }
                                          },
                                modifier = Modifier
                                    .zIndex(1f)
                                    .padding(5.dp)
                                    .clip(CircleShape)
                                    .background(topBarColor)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Favorite,
                                    contentDescription = "thumpup",
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        } else {
                            IconButton(
                                onClick = { coroutineScope.launch { viewModel.like() } },
                                modifier = Modifier
                                    .zIndex(1f)
                                    .padding(5.dp)
                                    .clip(CircleShape)
                                    .background(topBarColor)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.FavoriteBorder,
                                    contentDescription = "thumpup",
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
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
        modifier = Modifier.zIndex(0f)
    )
    { paddingValues ->
        VerticalNestedScrollView(
            modifier = Modifier.padding(paddingValues),
//                .nestedScroll(nested),
            state = nestedScrollViewState,
            header = {
                Layout(modifier = Modifier.height(100.dp), content = {
                    state.establecimiento?.let { establecimiento ->
                        HeaderEstablecimiento(
                            establecimiento = establecimiento,
                            navigateToPhoto = navigateToPhoto
//                        modifier = Modifier.graphicsLayer {
//                            alpha = headerO.coerceIn(0f,1f)
//                        }
                        )
                    }
                }) { measurables, constraints ->
                    val placeables = measurables.map { measurable ->
                        measurable.measure(
                            Constraints.fixed(
                                width = constraints.maxWidth,
                                height = constraints.maxHeight + 200
                            )
                        )
                    }
                    layout(
                        width = constraints.maxWidth,
                        height = constraints.maxHeight - 200
                    ) {
                        placeables.forEach { placeable ->
                            placeable.placeRelativeWithLayer(x = 0, y = -275)
                        }
                    }
                }

            }

        ) {

            Column() {

                Indicators(navToTab = {
                    coroutineScope.launch {
                        pagerState.scrollToPage(it)
                    }
                }, currentTab = pagerState.currentPage)
                HorizontalPager(
                    pageCount = 4, state = pagerState,
                    userScrollEnabled = false
                ) { page ->
                    when (page) {
                        0 -> {
                            EstablecimientoInfo(
                                state = state,
                                openLocationSheet = { coroutineScope.launch { sheetState.show() } },
                                navigateToReserva = {
                                    coroutineScope.launch {
                                        category.value = it
                                        delay(100)
                                        pagerState.animateScrollToPage(
                                            1
                                        )
                                    }
                                },
                                openMap = appUtil::openMap,
                                navigateToProfile = navigateToProfile,
                                navigateToReviews = navigateToReviews,
                                createReview = navigateToCreateReview,
                                openAttentionScheduleWeek = { coroutineScope.launch {
                                    viewModel.updateScheduleWeek()
                                    sheetStateAttentionSchedule.show()
                                } }
                            )
                        }

                        1 -> reservar(category.value)
                        2 -> establecimientoPhotos()
                        3 -> salas()
                        4 -> actividades()

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
    navigateToPhoto: (String) -> Unit
){
//    val isLike = remember {
//        mutableStateOf(false)
//    }

    Column(modifier = modifier.zIndex(1f)) {
            AsyncImage(
                model = establecimiento.photo,
                requestBuilder = { crossfade(true) },
                contentDescription = establecimiento.photo,
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
//                        val url = URLEncoder.encode(establecimiento.photo.toString(), StandardCharsets.UTF_8.toString())
//                        val data = MediaData(
//                            images = listOf(url)
//                        )
                        val payload =
                            Json.encodeToString(encodeMediaData(listOf(establecimiento.photo.toString())))
//                        Log.d("DEBUG_APP_PAYLOAD",payload)
                        navigateToPhoto(payload)
                    }
                    .fillMaxWidth()
                    .height(170.dp),
                contentScale = ContentScale.Crop,
            )
    }
}


@Composable
fun Indicators(
    navToTab:(tab:Int) ->Unit,
    currentTab:Int,
    modifier:Modifier = Modifier
){
//    ScrollableTabRow(selectedTabIndex = currentTab, edgePadding = 1.dp,
//        modifier = modifier) {
    TabRow(selectedTabIndex = currentTab,modifier = modifier) {
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
        )
        Tab(
            text = { Text(text = "Fotos",style = MaterialTheme.typography.labelMedium) },
            selected = currentTab ==2,
            onClick = {
                // Animate to the selected page when clicked
                navToTab(2)
            },
        )
        Tab(
            text = { Text(text = "Salas", style = MaterialTheme.typography.labelMedium) },
            selected = currentTab ==3,
            onClick = {
                // Animate to the selected page when clicked
                navToTab(3)
            },
//            modifier = Modifier.width(widthTab)
        )
//        Tab(
//            text = { Text(text = "Actividades", style = MaterialTheme.typography.labelMedium) },
//            selected = currentTab ==3,
//            onClick = {
//                // Animate to the selected page when clicked
//                navToTab(3)
//            },
////            modifier = Modifier.width(widthTab)
//        )

//
//    }
    }
}

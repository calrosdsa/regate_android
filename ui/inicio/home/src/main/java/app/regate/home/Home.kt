package app.regate.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import app.regate.common.composes.LocalAppDateFormatter
import app.regate.common.composes.components.CustomButton
import app.regate.common.composes.components.item.SalaItem
import app.regate.common.composes.ui.BottomBar
import app.regate.common.composes.ui.PosterCardImageDark
import app.regate.common.composes.ui.Skeleton
import app.regate.common.composes.ui.TopBar
import app.regate.common.composes.viewModel
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import app.regate.common.resources.R
import app.regate.constant.Route
import app.regate.constant.id
import app.regate.data.dto.empresa.establecimiento.EstablecimientoDto
import app.regate.home.carousel.HomeMediaCarousel
import kotlinx.datetime.Instant


typealias Home = @Composable (
     navigateToComplejo:(id:Long) -> Unit,
     navController:NavController,
     navigateToMap:()->Unit
) -> Unit

@Inject
@Composable
fun Home(
    viewModelFactory:()-> HomeViewModel,
    @Assisted navigateToComplejo: (id:Long) -> Unit,
    @Assisted navController:NavController,
    @Assisted navigateToMap: () -> Unit
){
    Home(
        viewModel = viewModel(factory = viewModelFactory),
        navigateToComplejo = navigateToComplejo,
        navController = navController,
        navigateToMap = navigateToMap
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Home(
    viewModel: HomeViewModel,
    navController: NavController,
    navigateToComplejo: (id:Long) -> Unit,
    navigateToMap: () -> Unit,
) {

    val viewState by viewModel.state.collectAsState()
    val formatter = LocalAppDateFormatter.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
//        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                title = { Text(text = "REGATE") },
                navigationIcon = {
                    Box() {
                        Image(
                            painter = painterResource(id = R.drawable.logo_app),
                            contentDescription = "logo_home", modifier = Modifier
                                .size(25.dp)
                                .align(
                                    Alignment.Center
                                )
                        )
                    }
                }
            )

//            TopBar(openDrawer = openDrawer)
        },
        bottomBar = {
            BottomBar(navController = navController)
        },
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {paddingValues->
        Home(
            viewState = viewState,
            navigateToComplejo = navigateToComplejo,
            modifier = Modifier.padding(paddingValues),
            navigateToMap = navigateToMap,
            goToGroup = {navController.navigate(Route.GRUPOS)},
            goToReserva = {navController.navigate(Route.DISCOVER)},
            formatShortTime = {formatter.formatShortTime(it)},
            formatDate = {formatter.formatWithSkeleton(it.toEpochMilliseconds(),formatter.monthDaySkeleton)},
            goToSala = {navController.navigate(Route.SALA id it)},
            viewMoreSalas = { navController.navigate(Route.FILTER_SALAS)}
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun Home(
    viewState:HomeState,
    navigateToComplejo: (id:Long) -> Unit,
    navigateToMap: () -> Unit,
    modifier: Modifier = Modifier,
    goToReserva:()->Unit,
    goToGroup:()->Unit,
    formatShortTime:(time: Instant)->String,
    formatDate:(date: Instant)->String,
    goToSala:(Long)->Unit,
    viewMoreSalas:()->Unit,
    ) {

    val listImage = listOf("https://img.freepik.com/free-photo/abstract-grunge-decorative-relief-navy-blue-stucco-wall-texture-wide-angle-rough-colored-background_1258-28311.jpg",
    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR-Ob3Iu4pkCSLtguCfIJiKrDZ9RTG01Vk9tQ",
    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRCJo8cI1DBvDVUd3-EJh175ef2AnBOr38KiQ&usqp=CAU")
    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        HomeMediaCarousel(list = listImage, onItemClicked = {})
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = stringResource(id = R.string.recommended_places),
            modifier = Modifier.padding(horizontal = 10.dp),style =MaterialTheme.typography.titleLarge)
        LazyRow(contentPadding = PaddingValues(10.dp), modifier = Modifier.height(175.dp)){
            if(viewState.loading){
                items(4){
                    Skeleton(  modifier = Modifier
                        .height(175.dp)
                        .width(270.dp)
                        .padding(5.dp))
                }
            }else {
                viewState.data?.recommended?.let { establecimientos ->
                    items(items = establecimientos) { item ->
                        EstablecimientoCard(
                            item = item, navigateToComplejo = navigateToComplejo,
                            modifier = Modifier
                                .height(175.dp)
                                .width(270.dp)
                                .padding(5.dp)
                        )
                    }
                }
            }
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween) {
            CardOption(icon = Icons.Default.CalendarToday, title =  stringResource(id = R.string.book_a_court),
            modifier = Modifier.fillMaxWidth(0.5f), onClick = goToReserva)
//            CardOption(icon = Icons.Default.CalendarToday, title =  stringResource(id = R.string.book_a_court),
//                modifier = Modifier.width(widthCard), onClick = {})
            CardOption(icon = Icons.Default.Group, title =  stringResource(id = R.string.join_a_group),
                modifier = Modifier.fillMaxWidth(), onClick = goToGroup)
        }

        viewState.addressDevice?.let {
            Text(text = stringResource(id = R.string.near_me),
                modifier = Modifier.padding(horizontal = 10.dp),style =MaterialTheme.typography.titleLarge)
//            ViewMore(label = stringResource(id = R.string.near_me),
//                onClick = {})
            LazyRow(contentPadding =PaddingValues(horizontal = 10.dp), modifier = containerHeight) {
                if (viewState.loading) {
                    items(4) {
                        Skeleton(
                            modifier = establecimientoSize
                        )
                    }
                } else {
                    viewState.data?.near?.let { establecimientos ->
                        items(items = establecimientos) { item ->
                            EstablecimientoCard(
                                item = item, navigateToComplejo = navigateToComplejo,
                                modifier = establecimientoSize
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
        }



        Text(text = stringResource(id = R.string.explore_exceptional_destinations),
        modifier = Modifier.padding(horizontal = 10.dp),style =MaterialTheme.typography.titleLarge)
//        ViewMore(label = stringResource(id = R.string.explore_exceptional_destinations),
//            onClick = {})
        LazyRow(contentPadding = PaddingValues(horizontal = 10.dp), modifier = containerHeight){
            if(viewState.loading){
                items(4){
                    Skeleton(modifier = establecimientoSize)
                }
            }else {
                viewState.data?.establecimientos?.let {establecimientos->
                items(items = establecimientos) { item ->
                    EstablecimientoCard(item = item, navigateToComplejo = navigateToComplejo,
                    modifier = establecimientoSize)
                }
                }
            }
        }

        ViewMore(label = stringResource(id = R.string.rooms),onClick={ viewMoreSalas()},
            styleText = MaterialTheme.typography.titleLarge)
        Column(modifier = Modifier.fillMaxWidth()) {
            viewState.data?.salas?.map {
                SalaItem(
                    sala = it,
                    formatDate = formatDate,
                    formatShortTime = formatShortTime,
                    navigateToSala = goToSala)
                Divider()
            }
        }


            MapImage { navigateToMap() }
    }
}
private val containerHeight = Modifier.height(130.dp)
private val establecimientoSize = Modifier
    .height(125.dp)
    .width(185.dp)
    .padding(5.dp)
@Composable
fun CardOption(
    modifier:Modifier= Modifier,
    icon:ImageVector,
    title:String,
    onClick:()->Unit,
){
    Card(modifier = modifier
        .padding(5.dp)
        .clickable {
            onClick()
        }
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(imageVector = icon, contentDescription = null,modifier = Modifier.size(22.dp))
            Text(text =title,style = MaterialTheme.typography.labelMedium, textAlign = TextAlign.Center,
                lineHeight = 15.sp
            )
        }
    }
}
@Composable
fun ViewMore(
    label:String,
    styleText: TextStyle = MaterialTheme.typography.titleMedium,
    onClick:() -> Unit
){
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween){
        Text(text = label,style = styleText,modifier = Modifier.fillMaxWidth(0.7f),

        )

        TextButton(onClick = { onClick() }) {
        Text(text = stringResource(id = R.string.show_more),color = MaterialTheme.colorScheme.primary, textDecoration = TextDecoration.Underline,
            style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
fun EstablecimientoCard(
    item:EstablecimientoDto,
    modifier:Modifier = Modifier,
    navigateToComplejo:(Long)->Unit
){
        Box(modifier = modifier){
        PosterCardImageDark(
            model = item.photo,
            modifier = Modifier
                .fillMaxSize()
                .clickable { navigateToComplejo(item.id.toLong()) }
        )
        Text(text = item.name, style = MaterialTheme.typography.labelMedium,
        modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(5.dp), maxLines = 1,color = Color.White)
        }
}


@Composable
fun MapImage(
    navigateToMap: () -> Unit
){
    Text(
        text = stringResource(id = R.string.looking_for_a_place),
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(10.dp)
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .padding(10.dp)
            .height(145.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.map_background),
            contentDescription = "map_background",
            modifier = Modifier
                .clip(MaterialTheme.shapes.large)
                .fillMaxSize(),
            contentScale = ContentScale.Crop,
            colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply{setToScale(0.8f,0.8f,0.8f,1f)}),
            )
        CustomButton(
            onClick = { navigateToMap() },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(10.dp),

        ) {
            Text(text = stringResource(id = R.string.search_nearby_places))
        }
    }
}

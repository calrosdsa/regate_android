package app.regate.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import app.regate.common.composes.components.CustomButton
import app.regate.common.composes.ui.BottomBar
import app.regate.common.composes.ui.PosterCardImageDark
import app.regate.common.composes.ui.Skeleton
import app.regate.common.composes.ui.TopBar
import app.regate.common.composes.viewModel
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import app.regate.common.resources.R
import app.regate.data.dto.empresa.establecimiento.EstablecimientoDto
import app.regate.home.carousel.HomeMediaCarousel


typealias Home = @Composable (
     navigateToComplejo:(id:Long) -> Unit,
     navController:NavController,
     openDrawer:()->Unit,
     navigateToMap:()->Unit
) -> Unit

@Inject
@Composable
fun Home(
    viewModelFactory:()-> HomeViewModel,
    @Assisted navigateToComplejo: (id:Long) -> Unit,
    @Assisted navController:NavController,
    @Assisted openDrawer: () -> Unit,
    @Assisted navigateToMap: () -> Unit
){
    Home(
        viewModel = viewModel(factory = viewModelFactory),
        navigateToComplejo = navigateToComplejo,
        navController = navController,
        openDrawer = openDrawer,
        navigateToMap = navigateToMap
    )
}
@Composable
internal fun Home(
    viewModel: HomeViewModel,
    navController: NavController,
    openDrawer: () -> Unit,
    navigateToComplejo: (id:Long) -> Unit,
    navigateToMap: () -> Unit,
) {

    val viewState by viewModel.state.collectAsState()

    Scaffold(
//        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(openDrawer = openDrawer)
        },
        bottomBar = {
            BottomBar(navController = navController)
        }
    ) {paddingValues->
        Home(
            viewState = viewState,
            navigateToComplejo = navigateToComplejo,
            modifier = Modifier.padding(paddingValues),
            navigateToMap = navigateToMap
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun Home(
    viewState:HomeState,
    navigateToComplejo: (id:Long) -> Unit,
    navigateToMap: () -> Unit,
    modifier: Modifier = Modifier

) {
    val listImage = listOf("https://img.freepik.com/free-photo/abstract-grunge-decorative-relief-navy-blue-stucco-wall-texture-wide-angle-rough-colored-background_1258-28311.jpg",
    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR-Ob3Iu4pkCSLtguCfIJiKrDZ9RTG01Vk9tQ",
    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRCJo8cI1DBvDVUd3-EJh175ef2AnBOr38KiQ&usqp=CAU")
    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        HomeMediaCarousel(list = listImage, onItemClicked = {})
        Text(text = stringResource(id = R.string.near_me),
        modifier = Modifier.padding(horizontal = 10.dp),style = MaterialTheme.typography.labelLarge)

        LazyRow(contentPadding = PaddingValues(10.dp), modifier = Modifier.height(120.dp)){
        if(viewState.loading){
            items(4){
                EstablecimientoCardSkeleton()
            }
        }else{
            items(items = viewState.establecimientos){item->
            EstablecimientoCard(item = item, navigateToComplejo = navigateToComplejo)
            }
        }
        }

        Text(text = stringResource(id = R.string.explore_exceptional_destinations),
            modifier = Modifier.padding(horizontal = 10.dp),style = MaterialTheme.typography.labelLarge)
        LazyRow(contentPadding = PaddingValues(10.dp), modifier = Modifier.height(120.dp)){
            if(viewState.loading){
                items(4){
                    EstablecimientoCardSkeleton()
                }
            }else {
                items(items = viewState.establecimientos) { item ->
                    EstablecimientoCard(item = item, navigateToComplejo = navigateToComplejo)
                }
            }
        }


            MapImage { navigateToMap() }
    }
}

@Composable
fun EstablecimientoCard(
    item:EstablecimientoDto,
    navigateToComplejo:(Long)->Unit
){
        Box(modifier = Modifier
            .height(95.dp)
            .width(165.dp)
            .padding(5.dp)){
        PosterCardImageDark(
            model = item.photo,
            modifier = Modifier
                .height(95.dp)
                .width(165.dp)
                .clickable { navigateToComplejo(item.id.toLong()) }
        )
        Text(text = item.name + "293293 21 321323", style = MaterialTheme.typography.labelMedium,
        modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(5.dp), maxLines = 1,color = Color.White)
        }
}
@Composable
fun EstablecimientoCardSkeleton(){
    Skeleton(modifier = Modifier.height(95.dp).width(165.dp).padding(5.dp))
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

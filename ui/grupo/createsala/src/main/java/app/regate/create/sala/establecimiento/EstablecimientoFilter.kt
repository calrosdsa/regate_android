package app.regate.create.sala.establecimiento

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import app.regate.common.resources.R
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import app.regate.common.composes.ui.Loader
import app.regate.common.composes.ui.PosterCardImage
import app.regate.common.composes.ui.PosterCardImageDark
import app.regate.common.composes.ui.SimpleTopBar
import app.regate.common.composes.util.itemsCustom
import app.regate.common.composes.viewModel
import app.regate.data.dto.empresa.establecimiento.EstablecimientoDto
import app.regate.models.Establecimiento
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

typealias EstablecimientoFilter= @Composable (
    navigateUp:()->Unit,
    navigateToCreateSala:(establecimientoId:Long,grupoId:Long)->Unit,
    navigateToEstablecimiento:(Long)->Unit
) -> Unit

@Inject
@Composable
fun EstablecimientoFilter(
    viewModelFactory:(SavedStateHandle)-> EstablecimientoFilterViewModel,
    @Assisted navigateUp: () -> Unit,
    @Assisted navigateToCreateSala: (establecimientoId: Long,grupoId:Long) -> Unit,
    @Assisted navigateToEstablecimiento: (Long) -> Unit,
){
    EstablecimientoFilter(
        viewModel = viewModel(factory = viewModelFactory),
        navigateToCreateSala = navigateToCreateSala,
        navigateUp = navigateUp,
        navigateToEstablecimiento = navigateToEstablecimiento
    )
}

@Composable
internal fun EstablecimientoFilter(
    viewModel: EstablecimientoFilterViewModel,
    navigateToCreateSala: (establecimientoId: Long,grupoId:Long) -> Unit,
    navigateToEstablecimiento: (Long) -> Unit,
    navigateUp: () -> Unit
) {
//    val viewState by viewModel.state.collectAsState()

    EstablecimientoFilter(
//        viewState = viewState,
        lazyPagingItems = viewModel.pagedList.collectAsLazyPagingItems(),
        navigateUp = navigateUp,
        navigateToCreateSala = {
            val grupoId = viewModel.geIdGroup()
            if(grupoId != null){
            navigateToCreateSala(it,grupoId)
            }
        },
        navigateToEstablecimiento = navigateToEstablecimiento
    )
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun EstablecimientoFilter(
//    viewState:EstablecimientoFilterState,
    lazyPagingItems: LazyPagingItems<EstablecimientoDto>,
    navigateToEstablecimiento: (Long) -> Unit,
    navigateUp:()->Unit,
    navigateToCreateSala: (establecimientoId: Long) -> Unit
) {

    val refreshing = lazyPagingItems.loadState.refresh == LoadState.Loading
    val pullRefreshState = rememberPullRefreshState(refreshing = refreshing, onRefresh = lazyPagingItems::refresh)
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {
            SimpleTopBar(
                navigateUp =  navigateUp, title = stringResource(id = R.string.choose_where_create_room),
                scrollBehavior = scrollBehavior
            )
        },
    ) {paddingValues->
        Box(modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)){
        LazyColumn(modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .padding(paddingValues)
            .padding(horizontal = 10.dp)) {
//            item{
//                Text(text = stringResource(id = R.string.favorites),style = MaterialTheme.typography.titleLarge)
//            }
            itemsCustom(
                items=lazyPagingItems
            ){item->
                if(item !=null){
                    EstablecimientoCard2(
                        item = item,
                        navigateToEstablecimiento = navigateToEstablecimiento,
                        navigateToCreateSala = navigateToCreateSala
                    )
                    Divider()
                }

            }
            item{
                if(lazyPagingItems.loadState.append == LoadState.Loading){
                    Loader()
                }
            }
        }

            PullRefreshIndicator(
                refreshing = refreshing,
                state = pullRefreshState,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(paddingValues),
                scale = true,
            )
        }
    }
}

@Composable
fun EstablecimientoCard(
    item:Establecimiento,
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
        Text(text = item.name , style = MaterialTheme.typography.labelMedium,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(5.dp), maxLines = 1,color = Color.White)
    }
}

@Composable
internal fun EstablecimientoCard2(
    item:EstablecimientoDto,
    navigateToEstablecimiento:(Long)->Unit,
    navigateToCreateSala:(Long)->Unit,
    modifier:Modifier = Modifier,
){
    Row(modifier = modifier
        .fillMaxWidth()
        .padding(vertical = 7.dp)) {
    PosterCardImage(model = item.photo,
    modifier = Modifier
        .width(135.dp)
        .height(85.dp))
        Spacer(modifier = Modifier.width(10.dp))

        Column(verticalArrangement = Arrangement.SpaceBetween,modifier = Modifier.height(90.dp)) {
            Text(text = item.name,maxLines=1,style = MaterialTheme.typography.titleMedium)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                ElevatedButton(onClick = { navigateToEstablecimiento(item.id.toLong()) }) {
                    Text(text = stringResource(id = R.string.see))
                }
                ElevatedButton(onClick = {  navigateToCreateSala(item.id.toLong()) }) {
                    Text(text = stringResource(id = R.string.create_sala))
                }
            }
        }
    }
}
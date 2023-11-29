package app.regate.filterSalas
import androidx.compose.foundation.layout.Box
import app.regate.common.resources.R
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import app.regate.common.composes.LocalAppDateFormatter
import app.regate.common.composes.component.item.SalaItem
import app.regate.common.composes.ui.Loader
import app.regate.common.composes.util.itemsCustom
import app.regate.common.composes.viewModel
import app.regate.data.auth.AppAuthState
import app.regate.data.dto.empresa.salas.SalaDto
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject


typealias FilterSalas= @Composable (
    navigateUp:()->Unit,
//    navigateToCreateSala: (id: Long) -> Unit,
    openAuthBottomSheet:()->Unit,
    navigateToSala:(Long)->Unit,
    navigateToSelectEstablecimiento:()->Unit,
) -> Unit

@Inject
@Composable
fun FilterSalas(
    viewModelFactory:()-> FilterSalasViewModel,
    @Assisted navigateUp: () -> Unit,
//    @Assisted navigateToCreateSala: (id: Long) -> Unit,
    @Assisted openAuthBottomSheet: () -> Unit,
    @Assisted navigateToSala: (Long) -> Unit,
    @Assisted navigateToSelectEstablecimiento: () -> Unit
){
    FilterSalas(
        viewModel = viewModel(factory = viewModelFactory),
        navigateUp = navigateUp,
//        navigateToCreateSala = navigateToCreateSala,
        openAuthBottomSheet = openAuthBottomSheet,
        navigateToSala = navigateToSala,
        navigateToSelectEstablecimiento = navigateToSelectEstablecimiento
    )
}

@Composable
internal fun FilterSalas(
    viewModel: FilterSalasViewModel,
    navigateUp: () -> Unit,
//    navigateToCreateSala: (id:Long) -> Unit,
    navigateToSala: (Long) -> Unit,
    navigateToSelectEstablecimiento: () -> Unit,
    openAuthBottomSheet: () -> Unit,
){
    val state by viewModel.state.collectAsState()
    val formatter = LocalAppDateFormatter.current
    FilterSalas(
        viewState = state,
        lazyPagingItems =  viewModel.pagedList.collectAsLazyPagingItems(),
        navigateUp = navigateUp,
//        navigateToCreateSala = navigateToCreateSala,
        openAuthBottomSheet = openAuthBottomSheet,
        formatShortTime = formatter::formatShortTime,
        formatDate = formatter::formatShortDate,
        navigateToSala = navigateToSala,
        navigateToSelectEstablecimiento = navigateToSelectEstablecimiento,
    )
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
internal fun FilterSalas(
    viewState: FilterSalaState,
    lazyPagingItems: LazyPagingItems<SalaDto>,
    navigateUp: () -> Unit,
//    navigateToCreateSala: (id: Long) -> Unit,
    navigateToSelectEstablecimiento: () -> Unit,
    formatShortTime:(time: String,plusMinutes:Long)->String,
    formatDate:(date: String)->String,
    navigateToSala:(Long)->Unit,
    openAuthBottomSheet: () -> Unit
){
    val refreshing = lazyPagingItems.loadState.refresh == LoadState.Loading
    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = lazyPagingItems::refresh
    )
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navigateUp() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                title = {
                    Text(text = stringResource(id = R.string.rooms))
                },
                actions = {
                    IconButton(onClick = {
                        if(viewState.authState == AppAuthState.LOGGED_IN){
                            navigateToSelectEstablecimiento()
                        }else{
                        openAuthBottomSheet()
                        }
                    }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "more_vert")
                    }
                    IconButton(onClick = {  }) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "more_vert")
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .padding(paddingValues)
            .pullRefresh(pullRefreshState)
            .fillMaxSize()
        ){
        LazyColumn(modifier = Modifier
            .fillMaxSize(),content = {
            itemsCustom(
                    items = lazyPagingItems
                ){sala->
                if (sala != null) {
                    SalaItem(
                        sala = sala,
                        formatDate = formatDate,
                        formatShortTime = formatShortTime,
                        navigateToSala = navigateToSala
                    )
                }
                }
            item{
                if(lazyPagingItems.loadState.append == LoadState.Loading){
                    Loader()
                }
            }
        })
            PullRefreshIndicator(
                refreshing = refreshing,
                state = pullRefreshState,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(paddingValues),
                scale = true
            )
        }
    }
}
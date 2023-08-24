package app.regate.usersalas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import app.regate.common.composes.components.item.SalaItem
import app.regate.common.composes.ui.Loader
import app.regate.common.composes.util.itemsCustom
import app.regate.common.composes.viewModel
import app.regate.data.auth.AppAuthState
import app.regate.data.dto.empresa.salas.SalaDto
import app.regate.models.SalaEntity
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject


typealias UserSalas = @Composable (
    navigateToSala:(Long)->Unit,
) -> Unit

@Inject
@Composable
fun UserSalas(
    viewModelFactory:()-> UserSalasViewModel,
    @Assisted navigateToSala: (Long) -> Unit,
){
    UserSalas(
        viewModel = viewModel(factory = viewModelFactory),
        navigateToSala = navigateToSala,
    )
}

@Composable
internal fun UserSalas(
    viewModel: UserSalasViewModel,
    navigateToSala: (Long) -> Unit,
){
    val state by viewModel.state.collectAsState()
    val formatter = LocalAppDateFormatter.current
    UserSalas(
        viewState = state,
        lazyPagingItems =  viewModel.pagedList.collectAsLazyPagingItems(),
//        navigateToCreateSala = navigateToCreateSala,
        formatShortTime =formatter::formatShortTime,
        formatDate =formatter::formatShortDate,
        navigateToSala = navigateToSala,
    )
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
internal fun UserSalas(
    viewState: UserSalasState,
    lazyPagingItems: LazyPagingItems<SalaDto>,
    formatShortTime:(time: String,plusMinutes:Long)->String,
    formatDate:(date: String)->String,
    navigateToSala:(Long)->Unit,
){
    val refreshing = lazyPagingItems.loadState.refresh == LoadState.Loading
    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = lazyPagingItems::refresh
    )
    LaunchedEffect(key1 = true, block = {
        viewState.loading
    })
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
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
                if ( sala != null) {
                    SalaItem (
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
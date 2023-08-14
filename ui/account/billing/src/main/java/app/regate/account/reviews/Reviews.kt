package app.regate.account.reviews

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import app.regate.common.resources.R
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import app.regate.common.composes.components.item.ReviewItem
import app.regate.common.composes.ui.Loader
import app.regate.common.composes.ui.SimpleTopBar
import app.regate.common.composes.util.itemsCustom
import app.regate.common.composes.viewModel
import app.regate.data.auth.AppAuthState
import app.regate.data.dto.empresa.establecimiento.EstablecimientoReview
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

typealias Reviews = @Composable (
    navigateUp:()->Unit,
//    navigateToCreateSala: (id: Long) -> Unit,
    openAuthBottomSheet:()->Unit,
    navigateToProfile:(Long)->Unit,
) -> Unit


@Inject
@Composable
fun Reviews(
    viewModelFactory:(SavedStateHandle)-> ReviewsViewModel,
    @Assisted navigateUp: () -> Unit,
    @Assisted openAuthBottomSheet: () -> Unit,
    @Assisted navigateToProfile: (Long) -> Unit
){
    Reviews(
        viewModel = viewModel(factory = viewModelFactory),
        navigateUp = navigateUp,
        openAuthBottomSheet = openAuthBottomSheet,
        navigateToProfile = navigateToProfile
    )

}

@Composable
internal fun Reviews(
    viewModel: ReviewsViewModel,
    navigateUp: () -> Unit,
    openAuthBottomSheet: () -> Unit,
    navigateToProfile: (Long) -> Unit
) {
    val state by viewModel.state.collectAsState()
    Reviews(
        viewState = state, navigateUp = navigateUp, navigateToProfile = navigateToProfile,
        openAuthBottomSheet = openAuthBottomSheet,
        lazyPagingItems = viewModel.pagedList.collectAsLazyPagingItems()
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
internal fun Reviews(
    viewState: ReviewsState,
    lazyPagingItems: LazyPagingItems<EstablecimientoReview>,
    navigateUp: () -> Unit,
    navigateToProfile: (Long) -> Unit,
    openAuthBottomSheet: () -> Unit
){
    val refreshing = lazyPagingItems.loadState.refresh == LoadState.Loading
    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = lazyPagingItems::refresh
    )
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        topBar = {
            SimpleTopBar(
                navigateUp = navigateUp,
                title = stringResource(id = R.string.reviews),
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            SmallFloatingActionButton(onClick = {
                if (viewState.authState == AppAuthState.LOGGED_OUT) {
                    openAuthBottomSheet()
                }else {
                    //todo()
                }
            }) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .pullRefresh(pullRefreshState)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                content = {
                    itemsCustom(
                        items = lazyPagingItems
                    ) { item ->
                        if (item != null) {
                            ReviewItem(review = item, navigateToProfile = navigateToProfile)
                            Divider(modifier = Modifier.padding(vertical = 5.dp))
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
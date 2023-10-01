package app.regate.search

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import app.regate.common.resources.R
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import app.regate.common.compose.components.item.EstablecimientoItem
import app.regate.common.compose.components.item.ReviewItem
import app.regate.common.compose.ui.Loader
import app.regate.common.compose.ui.SimpleTopBar
import app.regate.common.compose.util.itemsCustom
import app.regate.common.compose.viewModel
import app.regate.data.auth.AppAuthState
import app.regate.data.dto.empresa.establecimiento.EstablecimientoDto
import app.regate.data.dto.empresa.establecimiento.EstablecimientoReview
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

typealias Search = @Composable (
    navigateUp:()->Unit,
//    navigateToCreateSala: (id: Long) -> Unit,
    openAuthBottomSheet:()->Unit,
    navigateToProfile:(Long)->Unit,
) -> Unit


@Inject
@Composable
fun Search(
    viewModelFactory:(SavedStateHandle)-> SearchViewModel,
    @Assisted navigateUp: () -> Unit,
    @Assisted openAuthBottomSheet: () -> Unit,
    @Assisted navigateToProfile: (Long) -> Unit,
){
    Search(
        viewModel = viewModel(factory = viewModelFactory),
        navigateUp = navigateUp,
        openAuthBottomSheet = openAuthBottomSheet,
        navigateToProfile = navigateToProfile,
    )

}

@Composable
internal fun Search(
    viewModel: SearchViewModel,
    navigateUp: () -> Unit,
    openAuthBottomSheet: () -> Unit,
    navigateToProfile: (Long) -> Unit,
) {
    val state by viewModel.state.collectAsState()
    Search(
        viewState = state, navigateUp = navigateUp, navigateToProfile = navigateToProfile,
        openAuthBottomSheet = openAuthBottomSheet,
        lazyPagingItems = viewModel.pagedList.collectAsLazyPagingItems(),
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
internal fun Search(
    viewState: SearchState,
    lazyPagingItems: LazyPagingItems<EstablecimientoDto>,
    navigateUp: () -> Unit,
    navigateToProfile: (Long) -> Unit,
    openAuthBottomSheet: () -> Unit,
){
    var query by remember {
        mutableStateOf("")
    }
    val refreshing = lazyPagingItems.loadState.refresh == LoadState.Loading
    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = lazyPagingItems::refresh
    )
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        topBar = {
            Row(modifier = Modifier.padding(5.dp),
            verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navigateUp() }) {
                    Icon(imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(id = R.string.back))
                }

                BasicTextField(value = query,
                    onValueChange ={query = it},
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onBackground),
                    decorationBox = { innerTextField ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .padding(horizontal = 0.dp),
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.inverseOnSurface
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(5.dp)) {
                                Spacer(modifier = Modifier.width(5.dp))
                                Box(modifier = Modifier.fillMaxWidth(0.9f)){
                                    if(query.isBlank()){
                                        Text(text = stringResource(id = R.string.search),
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.inverseSurface)
                                    }
                                innerTextField()
                                }
                                Crossfade(targetState = !query.isBlank()) {
                                    if(it){
                                        IconButton(onClick = { query = "" }) {
                                            Icon(imageVector = Icons.Outlined.Close,
                                                contentDescription = null)
                                        }
                                    }
                                }
                            }
                            }
                        }
                    )

            }
        },
        floatingActionButton = {
            SmallFloatingActionButton(onClick = {
                if (viewState.authState == AppAuthState.LOGGED_OUT) {
                    navigateToProfile(1)
                    openAuthBottomSheet()
                }else {
                    //TODO()
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
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                content = {
                    itemsCustom(
                        items = lazyPagingItems
                    ) { item ->
                        if (item != null) {
                            EstablecimientoItem(name = item.name, photo = item.photo)
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
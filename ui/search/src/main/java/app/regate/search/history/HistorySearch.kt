package app.regate.search.history

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.TextButton
import androidx.compose.material3.ExperimentalMaterial3Api
import app.regate.common.resources.R
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.SavedStateHandle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import app.regate.common.composes.ui.Loader
import app.regate.common.composes.ui.SimpleTopBar
import app.regate.common.composes.util.itemsCustom
import app.regate.common.composes.viewModel
import app.regate.models.SearchHistory
import app.regate.search.HistorySearchItem
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

typealias HistorySearch = @Composable (
    navigateUp:()->Unit,
    navigateToSearch:(String)->Unit
) -> Unit


@Inject
@Composable
fun HistorySearch(
    viewModelFactory:(SavedStateHandle)-> HistorySearchViewModel,
    @Assisted navigateUp: () -> Unit,
    @Assisted navigateToSearch: (String) -> Unit
){
    HistorySearch(
        viewModel = viewModel(factory = viewModelFactory),
        navigateUp = navigateUp,
        navigateToSearch = navigateToSearch
    )

}

@Composable
internal fun HistorySearch(
    viewModel: HistorySearchViewModel,
    navigateUp: () -> Unit,
    navigateToSearch: (String) -> Unit
) {
//    val state by viewModel.state.collectAsState()
    HistorySearch(
//        viewState = state,
        navigateUp = navigateUp,
        lazyPagingItems = viewModel.pagedList.collectAsLazyPagingItems(),
        navigateToSearch = navigateToSearch
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
internal fun HistorySearch(
//    viewState: HistorySearchState,
    lazyPagingItems: LazyPagingItems<SearchHistory>,
    navigateUp: () -> Unit,
    navigateToSearch: (String) -> Unit
){

    Scaffold(
        topBar = {
          SimpleTopBar(navigateUp = navigateUp,
          title = stringResource(id = R.string.history),
          actions = {
              TextButton(onClick = { /*TODO*/ }) {
                  Text(text = stringResource(id = R.string.clear_all))
              }
          })
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                content = {
                    itemsCustom(
                        items = lazyPagingItems
                    ) { item ->
                        if (item != null) {
                            HistorySearchItem(
                                item = item,
                                onClick = navigateToSearch
                            )
                        }
                    }

                    item{
                        if(lazyPagingItems.loadState.append == LoadState.Loading){
                            Loader()
                        }
                    }
                })


        }
    }
}
package app.regate.search.salas

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import app.regate.common.composes.LocalAppDateFormatter
import app.regate.common.composes.component.item.SalaItem
import app.regate.common.composes.ui.Loader
import app.regate.common.composes.util.itemsCustom
import app.regate.common.composes.viewModel
import app.regate.common.resources.R
import app.regate.data.dto.empresa.salas.SalaDto
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

typealias SearchSalas = @Composable (
    navigateToSala:(Long)->Unit,
) -> Unit


@Inject
@Composable
fun SearchSalas(
    viewModelFactory:()-> SearchSalasViewModel,
    @Assisted navigateToSala: (Long) -> Unit
){
    SearchSalas(
        viewModel = viewModel(factory = viewModelFactory),
        navigateToSala = navigateToSala
    )

}

@Composable
internal fun SearchSalas(
    viewModel: SearchSalasViewModel,
    navigateToSala: (Long) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val formatter = LocalAppDateFormatter.current

    SearchSalas(
        viewState = state,
        lazyPagingItems = viewModel.pagedList.collectAsLazyPagingItems(),
        navigateToSala = navigateToSala,
        formatShortTime = formatter::formatShortTime,
        formatDate = formatter::formatShortDate,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SearchSalas(
    viewState: SearchSalasState,
    lazyPagingItems: LazyPagingItems<SalaDto>,
    formatShortTime:(time:String,plusMinutes:Long)->String,
    formatDate:(date:String)->String,
    navigateToSala: (Long) -> Unit
) {
    LaunchedEffect(key1 = viewState.filterData, block = {
        if(viewState.filterData.query.isNotBlank()){
            lazyPagingItems.refresh()
        }
    })
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold() { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                content = {
                    if(lazyPagingItems.loadState.refresh == LoadState.Loading) {
                        item {
                            Loader()
                        }
                    }else {
                        if (lazyPagingItems.itemCount == 0) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(top = 40.dp)
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.no_results_for_saerch),
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier
                                            .align(Alignment.Center)
                                            .fillMaxWidth(0.8f),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                    itemsCustom(
                        items = lazyPagingItems
                    ) { item ->
                        if (item != null) {
                            SalaItem(
                                sala = item,
                                formatDate = formatDate,
                                formatShortTime = formatShortTime,
                                navigateToSala = navigateToSala
                            )
                        }
                    }

                    item {
                        if (lazyPagingItems.loadState.append == LoadState.Loading) {
                            Loader()
                        }
                    }
                })


        }
    }
}
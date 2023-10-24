package app.regate.search.grupos

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
import app.regate.common.composes.component.item.GrupoItem
import app.regate.common.composes.ui.Loader
import app.regate.common.composes.util.itemsCustom
import app.regate.common.composes.viewModel
import app.regate.common.resources.R
import app.regate.data.dto.empresa.grupo.GrupoDto
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

typealias SearchGrupos = @Composable (
//    navigateToCreateSala: (id: Long) -> Unit,
    navigateToGroup:(Long)->Unit,
    navigateToInfoGrupo:(Long)->Unit
) -> Unit


@Inject
@Composable
fun SearchGrupos(
    viewModelFactory:()-> SearchGroupsViewModel,
    @Assisted navigateToGroup: (Long) -> Unit,
    @Assisted navigateToInfoGrupo: (Long) -> Unit,
    ){
    SearchGrupos(
        viewModel = viewModel(factory = viewModelFactory),
        navigateToGroup = navigateToGroup,
        navigateToInfoGrupo = navigateToInfoGrupo
    )

}

@Composable
internal fun SearchGrupos(
    viewModel: SearchGroupsViewModel,
    navigateToGroup: (Long) -> Unit,
    navigateToInfoGrupo: (Long) -> Unit
) {
    val state by viewModel.state.collectAsState()
    SearchGrupos(
        viewState = state,
        lazyPagingItems = viewModel.pagedList.collectAsLazyPagingItems(),
        navigateToGroup = navigateToGroup,
        joinToGroup = viewModel::joinToGroup,
        navigateToInfoGrupo = navigateToInfoGrupo
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SearchGrupos(
    viewState: SearchGruposState,
    lazyPagingItems: LazyPagingItems<GrupoDto>,
    navigateToGroup: (Long) -> Unit,
    joinToGroup:(Long,Int)->Unit,
    navigateToInfoGrupo: (Long) -> Unit,
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
                    ) { result ->
                        if (result != null) {
                            val grupoU = viewState.userGroups.find { it.id == result.id }
                            if(grupoU != null){
                                GrupoItem(
                                    navigate = navigateToGroup,
                                    grupo = result.copy(
                                        grupo_request_estado = grupoU.request_estado.ordinal
                                    ),
                                    joinToGroup = {it1,it2->
                                        joinToGroup(it1,it2)
                                    },
                                    navigateToInfoGrupo = navigateToInfoGrupo
                                )
                            }else{
                                GrupoItem(
                                    navigate = navigateToInfoGrupo,
                                    grupo = result,
                                    joinToGroup = {it1,it2->
                                        joinToGroup(it1,it2)
                                    }
                                )
                            }


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
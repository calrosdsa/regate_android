package app.regate.search

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.WatchLater
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import app.regate.common.resources.R
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import app.regate.common.composes.component.item.EstablecimientoItemWithLocation
import app.regate.common.composes.component.item.GrupoItem
import app.regate.common.composes.component.item.ProfileItem
import app.regate.common.composes.ui.Loader
import app.regate.common.composes.util.itemsCustom
import app.regate.common.composes.util.spacerLazyList
import app.regate.common.composes.viewModel
import app.regate.constant.Route
import app.regate.constant.id
import app.regate.data.dto.empresa.establecimiento.EstablecimientoDto
import app.regate.data.dto.empresa.grupo.GrupoDto
import app.regate.models.SearchHistory
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

typealias Search = @Composable (
    navController: NavController,
    queryArg:String,
    searchGrupos: @Composable () ->Unit,
    searchProfiles: @Composable () ->Unit,
    searchSalas: @Composable () ->Unit,
    ) -> Unit


@Inject
@Composable
fun Search(
    viewModelFactory:(SavedStateHandle)-> SearchViewModel,
    @Assisted navController: NavController,
    @Assisted queryArg: String,
    @Assisted searchGrupos: @Composable () -> Unit,
    @Assisted searchProfiles: @Composable () -> Unit,
    @Assisted searchSalas: @Composable () -> Unit,
){
    Search(
        viewModel = viewModel(factory = viewModelFactory),
        navigateUp = navController::navigateUp,
        navigateToGroup = {navController.navigate(Route.GRUPO id it)},
        navigateToProfile = { navController.navigate(Route.PROFILE id it)},
        navigateToEstablecimiento = {navController.navigate(Route.ESTABLECIMIENTO id it id 0) },
        navigateToHistorySearch = {navController.navigate(Route.HISTORY_SEARCH) },
        navigateToInfoGrupo= { navController.navigate(Route.INFO_GRUPO id it)},
        queryArg = queryArg,
        searchGrupos = searchGrupos,
        searchProfiles = searchProfiles,
        searchSalas = searchSalas
        )

}

@Composable
internal fun Search(
    viewModel: SearchViewModel,
    navigateUp: () -> Unit,
    navigateToProfile: (Long) -> Unit,
    navigateToEstablecimiento: (Long) -> Unit,
    navigateToHistorySearch: () -> Unit,
    navigateToGroup: (Long) -> Unit,
    navigateToInfoGrupo: (Long) -> Unit,
    queryArg:String,
    searchGrupos: @Composable () -> Unit,
    searchProfiles: @Composable () -> Unit,
    searchSalas: @Composable () -> Unit,
    ) {
    val state by viewModel.state.collectAsState()
    Search(
        viewState = state, navigateUp = navigateUp, navigateToProfile = navigateToProfile,
        search = viewModel::search,
        lazyPagingItems = viewModel.pagedList.collectAsLazyPagingItems(),
        navigateToHistorySearch = navigateToHistorySearch,
        navigateToEstablecimiento = navigateToEstablecimiento,
        navigateToGroup = navigateToGroup,
        navigateToInfoGrupo = navigateToInfoGrupo,
        queryArg = queryArg,
        searchGrupos = searchGrupos,
        searchProfiles= searchProfiles,
        searchSalas = searchSalas,
        joinToGroup = viewModel::joinToGroup
//        joinToGroup = viewModel::joinToGroup
        )
}
@OptIn(
    ExperimentalFoundationApi::class
)
@Composable
internal fun Search(
    viewState: SearchState,
    queryArg: String,
    lazyPagingItems: LazyPagingItems<EstablecimientoDto>,
    navigateUp: () -> Unit,
    navigateToProfile: (Long) -> Unit,
    search:(String)->Unit,
    navigateToHistorySearch: () -> Unit,
    navigateToGroup: (Long) -> Unit,
    navigateToEstablecimiento: (Long) -> Unit,
    navigateToInfoGrupo:(Long)->Unit,
    joinToGroup:(Long,Int,GrupoDto)->Unit,
    searchGrupos: @Composable () -> Unit,
    searchProfiles: @Composable () -> Unit,
    searchSalas: @Composable () -> Unit,
) {
    var query by rememberSaveable {
        mutableStateOf("")
    }
    val isAddressDevice by remember(viewState.addressDevice) {
        derivedStateOf {
            viewState.addressDevice != null
        }
    }
    var shouldShowResults by rememberSaveable {
        mutableStateOf(false)
    }
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }
    val pagerState = rememberPagerState()


    fun onSearch(searchQuery: String) {
        search(searchQuery)
        focusManager.clearFocus(true)
        shouldShowResults = true
    }
    LaunchedEffect(key1 = Unit, block = {
        if (queryArg.isNotBlank()) {
            query = queryArg
            onSearch(queryArg)
        }
    })

    LaunchedEffect(key1 = isFocused, block = {
        if (isFocused) {
            shouldShowResults = false
        }
    })

    LaunchedEffect(key1 = viewState.filterData, block = {
        if (viewState.filterData.query.isNotBlank()) {
            lazyPagingItems.refresh()
        }
    })

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier.padding(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navigateUp() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(id = R.string.back)
                    )
                }
                BasicTextField(value = query,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            onSearch(query)
                        }
                    ),
                    modifier = Modifier.focusRequester(focusRequester),
                    maxLines = 1,
                    onValueChange = { query = it },
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onBackground),
                    interactionSource = interactionSource,
                    decorationBox = { innerTextField ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .padding(horizontal = 0.dp),
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.inverseOnSurface
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(5.dp)
                            ) {
                                Spacer(modifier = Modifier.width(5.dp))
                                Box(modifier = Modifier.fillMaxWidth(0.9f)) {
                                    if (query.isBlank()) {
                                        Text(
                                            text = stringResource(id = R.string.search),
                                            style = MaterialTheme.typography.labelLarge,
                                            color = MaterialTheme.colorScheme.inverseSurface
                                        )
                                    }
                                    innerTextField()
                                }
                                Crossfade(targetState = !query.isBlank()) {
                                    if (it) {
                                        IconButton(onClick = {
                                            query = ""
                                            shouldShowResults = false
                                            focusRequester.requestFocus()
                                        }) {
                                            Icon(
                                                imageVector = Icons.Outlined.Close,
                                                contentDescription = null
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                )
            }
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            if (shouldShowResults) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Indicators(navToTab = {
                        coroutineScope.launch {
                            pagerState.scrollToPage(it)
                        }
                    }, currentTab = pagerState.currentPage)

                    if (viewState.loading) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Loader(modifier = Modifier.align(Alignment.TopCenter))
                        }
                    } else {
                        HorizontalPager(
                            pageCount = 4, state = pagerState,
                        ) { page ->

                            when (page) {
                                0 -> LazyColumn(
                                    contentPadding = PaddingValues(6.dp),
                                    content = {
                                        if (viewState.profiles.isEmpty() && viewState.grupos.isEmpty()
                                            && lazyPagingItems.itemCount == 0
                                        ) {
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
                                        if (viewState.profiles.isNotEmpty()) {
                                            item {
                                                Text(
                                                    text = stringResource(id = R.string.people),
                                                    style = MaterialTheme.typography.titleMedium
                                                )
                                            }
                                            spacerLazyList()
                                            items(
                                                items = viewState.profiles,
                                            ) { profile ->
                                                ProfileItem(
                                                    id = profile.profile_id,
                                                    photo = profile.profile_photo,
                                                    nombre = profile.nombre,
                                                    apellido = profile.apellido,
                                                    navigateToProfile = navigateToProfile
                                                )
                                            }
                                        }

                                        if (viewState.grupos.isNotEmpty()) {
                                            item {
                                                Text(
                                                    text = stringResource(id = R.string.groups),
                                                    style = MaterialTheme.typography.titleMedium
                                                )
                                            }
                                            spacerLazyList()
                                            items(
                                                items = viewState.grupos,
                                            ) { result ->
                                                val grupoU =
                                                    viewState.userGroups.find { it.id == result.id }

                                                if (grupoU != null) {
                                                    GrupoItem(
                                                        navigate = navigateToGroup,
                                                        grupo = result.copy(
                                                            grupo_request_estado = grupoU.request_estado.ordinal
                                                        ),
                                                        joinToGroup = { it1, it2 ->
                                                            joinToGroup(it1, it2,result)
                                                        },
                                                        navigateToInfoGrupo = navigateToInfoGrupo
                                                    )
                                                } else {
                                                    GrupoItem(
                                                        navigate = navigateToInfoGrupo,
                                                        grupo = result,
                                                        joinToGroup = { it1, it2 ->
                                                            joinToGroup(it1, it2,result)
                                                        }
                                                    )
                                                }

                                            }
                                        }

                                        if (lazyPagingItems.itemCount > 0) {
                                            item {
                                                Text(
                                                    text = stringResource(id = R.string.establecimientos),
                                                    style = MaterialTheme.typography.titleMedium
                                                )
                                            }
                                            spacerLazyList()
                                            itemsCustom(
                                                items = lazyPagingItems
                                            ) { item ->
                                                if (item != null) {
                                                    EstablecimientoItemWithLocation(
                                                        name = item.name,
                                                        photo = item.photo,
                                                        navigate = {
                                                            navigateToEstablecimiento(item.id.toLong())
                                                        },
                                                        isAddressDevice = isAddressDevice,
                                                        distance = item.distance,
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

                                1 -> searchProfiles()
                                2 -> searchGrupos()
                                3 -> searchSalas()
                            }
                        }
                    }
                }


            } else {
                LazyColumn(
                    contentPadding = PaddingValues(6.dp),
                    modifier = Modifier
                        .fillMaxSize(),
                    content = {
                        item {
                            Divider(modifier = Modifier.padding(vertical = 5.dp))
                        }
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 5.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(id = R.string.recent),
                                    style = MaterialTheme.typography.titleLarge
                                )
                                TextButton(onClick = { navigateToHistorySearch() }) {
                                    Text(
                                        text = stringResource(id = R.string.see_all),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                        items(
                            items = viewState.history
                        ) { item ->

                            HistorySearchItem(
                                item = item,
                                onClick = {
                                    onSearch(item.query)
                                    query = item.query
                                }
                            )
                        }
                    })
            }
        }
    }
}


@Composable
internal fun HistorySearchItem(
    item:SearchHistory,
    onClick:(String)->Unit = {},
){
    Row(modifier = Modifier
        .clickable { onClick(item.query) }
        .fillMaxWidth()
        .padding(vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = Icons.Outlined.WatchLater, contentDescription = null,modifier =
        Modifier.size(30.dp))
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = item.query,style = MaterialTheme.typography.labelLarge)
    }
}


@Composable
internal fun Indicators(
    navToTab:(tab:Int) ->Unit,
    currentTab:Int,
    modifier:Modifier = Modifier
){
//    val widthTab = LocalConfiguration.current.screenWidthDp.dp /4
        TabRow(selectedTabIndex = currentTab,
            modifier = modifier.fillMaxWidth()) {
            Tab(
                text = { Text(text = stringResource(id = R.string.all), style = MaterialTheme.typography.labelMedium) },
                selected = currentTab == 0,
                onClick = {
                    navToTab(0)
                },
            )
            Tab(
                text = { Text(text = stringResource(id = R.string.people),style = MaterialTheme.typography.labelMedium) },
                selected = currentTab ==1,
                onClick = {
                    // Animate to the selected page when clicked
                    navToTab(1)
                },
            )
            Tab(
                text = { Text(text = stringResource(id = R.string.groups),style = MaterialTheme.typography.labelMedium) },
                selected = currentTab ==1,
                onClick = {
                    // Animate to the selected page when clicked
                    navToTab(2)
                },
            )
            Tab(
                text = { Text(text = stringResource(id = R.string.rooms),style = MaterialTheme.typography.labelMedium) },
                selected = currentTab ==1,
                onClick = {
                    // Animate to the selected page when clicked
                    navToTab(3)
                },
            )
        }
}

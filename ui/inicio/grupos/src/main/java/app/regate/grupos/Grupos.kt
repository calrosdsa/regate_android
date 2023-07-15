package app.regate.grupos

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.More
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import app.regate.common.composes.ui.BottomBar
import app.regate.common.composes.ui.PosterCardImage
import app.regate.common.composes.ui.TopBar
import app.regate.common.composes.viewModel
import app.regate.common.resources.R
import app.regate.constant.Route
import app.regate.constant.id
import app.regate.models.Grupo
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

typealias Grupos = @Composable (
    navController: NavController,
    userGroups:@Composable () -> Unit,
//    navigateToReserva:(id:Long)->Unit,
//    navigateToSignUpScreen:() -> Unit,
) -> Unit

@Inject
@Composable
fun Grupos (
    viewModelFactory:()->GruposViewModel,
    @Assisted navController: NavController,
    @Assisted userGroups:@Composable () -> Unit,
//    @Assisted navigateToReserva: (id:Long) -> Unit,
//    viewModelFactory:()->ReservasViewModel
){
    Grupos(navController = navController,
        viewModel = viewModel(factory = viewModelFactory),
        userGroups = userGroups
    )
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun Grupos(
    navController: NavController,
    viewModel:GruposViewModel,
    userGroups:@Composable () -> Unit,
    ) {
    val viewState by viewModel.state.collectAsState()
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        bottomBar = {
            BottomBar(navController = navController)
        },
    topBar = {
            Indicators(navToTab = {
               coroutineScope.launch {
                   pagerState.animateScrollToPage(it)
               }
            } , currentTab = pagerState.currentPage,
               createGroup = { navController.navigate(Route.CREATE_GROUP)}
            )

    }
    ) { paddingValue ->
        HorizontalPager(pageCount = 2,state= pagerState,modifier = Modifier
            .padding(paddingValue)) {page->
            when (page) {
                0 -> userGroups()
                1 -> Grupos(
                    modifier = Modifier,
                    viewState = viewState,
                    navigateToGrupoChat = { navController.navigate(Route.CHAT_SALA id it) }
                )
            }
        }
    }
}

@Composable
internal fun Grupos(
    viewState:GruposState,
    navigateToGrupoChat:(id:Long)->Unit,
    modifier:Modifier = Modifier
){
    Column(modifier = modifier.fillMaxSize()) {

        viewState.grupos.map{
            GrupoItem(grupo = it,navigateToChatGrupo = navigateToGrupoChat)
        }
    }
}

@Composable
fun GrupoItem(
    grupo:Grupo,
    navigateToChatGrupo: (id: Long) -> Unit,
    modifier:Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { navigateToChatGrupo(grupo.id) }
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PosterCardImage(
            model = grupo.photo, modifier = Modifier
                .size(70.dp), shape = CircleShape
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = grupo.name, style = MaterialTheme.typography.titleMedium)
    }
}




@Composable
internal fun Indicators(
    navToTab:(tab:Int) ->Unit,
    createGroup:()->Unit,
    currentTab:Int,
    modifier:Modifier = Modifier
){
    var expanded by remember { mutableStateOf(false) }
//    val widthTab = LocalConfiguration.current.screenWidthDp.dp /4
    Row(modifier = Modifier
        .padding(top = 12.dp, start = 5.dp, end = 5.dp)
        .fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
    ScrollableTabRow(selectedTabIndex = currentTab, edgePadding = 1.dp,
        modifier = modifier.fillMaxWidth(0.6f)) {
        Tab(
            text = { Text(text = "Mis Grupos", style = MaterialTheme.typography.labelMedium) },
            selected = currentTab == 0,
            onClick = {
                navToTab(0)
            },
        )
        Tab(
            text = { Text(text = "Grupos",style = MaterialTheme.typography.labelMedium) },
            selected = currentTab ==1,
            onClick = {
                // Animate to the selected page when clicked
                navToTab(1)
            },
        )
    }
        Row() {
            Box() {
            IconButton(onClick = { expanded = !expanded }) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "more_vert")
            }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    DropdownMenuItem(
                        text = { Text(text = stringResource(id = R.string.create_group)) },
                        onClick = { createGroup() }
                    )
                    DropdownMenuItem(
                        text = { Text("Save") },
                        onClick = {  }
                    )
                }
            }
                AnimatedVisibility(visible = currentTab == 1) {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.Sort, contentDescription = "sort")
                    }
                }
        }
    }
}

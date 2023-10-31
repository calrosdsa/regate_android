package app.regate.grupos

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import app.regate.common.composes.LocalAppDateFormatter
import app.regate.common.composes.ui.BottomBar
import app.regate.common.composes.viewModel
import app.regate.common.resources.R
import app.regate.constant.Route
import app.regate.constant.id
import app.regate.data.auth.AppAuthState
import app.regate.chats.ChatsUser
import app.regate.chats.ChatsViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

typealias Grupos = @Composable (
    navController: NavController,
    filterGroups:@Composable () -> Unit,
    userSalas:@Composable () -> Unit,
    uuid:String,
//    navigateToReserva:(id:Long)->Unit,
//    navigateToSignUpScreen:() -> Unit,
) -> Unit

@Inject
@Composable
fun Grupos (
    viewModelFactory:()->ChatsViewModel,
    @Assisted navController: NavController,
    @Assisted filterGroups:@Composable () -> Unit,
    @Assisted userSalas:@Composable () -> Unit,
    @Assisted uuid:String,
//    @Assisted navigateToReserva: (id:Long) -> Unit,
//    viewModelFactory:()->ReservasViewModel
){
    val formatter = LocalAppDateFormatter.current
    Grupos(navController = navController,
        viewModel = viewModel(factory = viewModelFactory),
        filterGroups = filterGroups,
        userSalas = userSalas,
        formatShortRelativeTime = formatter::formatShortRelativeTime,
        openAuthBottomSheet = { navController.navigate(Route.AUTH_DIALOG) },
        navigateToUserGrupoRequests = { navController.navigate(Route.USER_PENDING_REQUESTS)},
        navigateToInvitationGrupo = { navController.navigate(Route.INVITATION_GRUPO + "?uuid=${it}")},
        uuid = uuid
//        navigateToPhoto = {
////            val url = URLEncoder.encode(it, StandardCharsets.UTF_8.toString())
//            navController.navigate(Route.PHOTO id it)
//        },
    )
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun Grupos(
    navController: NavController,
    viewModel:ChatsViewModel,
    uuid:String,
    formatShortRelativeTime: (Instant) -> String,
    openAuthBottomSheet:()->Unit,
    navigateToUserGrupoRequests:()->Unit,
    navigateToInvitationGrupo:(String)->Unit,
//    navigateToPhoto:(String)->Unit,
    filterGroups:@Composable () -> Unit,
    userSalas:@Composable () -> Unit,
    ) {
//    val pagingItems = viewModel.pagingList.collectasLa
    val viewState by viewModel.state.collectAsState()
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    val openBottomSheet = rememberSaveable {
        mutableStateOf(uuid.isNotBlank())
    }
    LaunchedEffect(key1 = Unit, block = {
        if(openBottomSheet.value){
            navigateToInvitationGrupo(uuid)
            openBottomSheet.value = false
        }
    })
    Scaffold(
        bottomBar = {
            BottomBar(navController = navController)
        },
    topBar = {
            Indicators(navToTab = {
               coroutineScope.launch {
                   pagerState.scrollToPage(it)
               }
            } , currentTab = pagerState.currentPage,
               createGroup = { navController.navigate(Route.CREATE_GROUP)},
                navigateToCreateSala = {navController.navigate(Route.ESTABLECIMIENTO_FILTER id 0)},
                openAuthBottomSheet = openAuthBottomSheet,
                appAuthState = viewState.authState,
                navigateToUserGrupoRequests = navigateToUserGrupoRequests
            )

    }
    ) { paddingValue ->
        HorizontalPager(pageCount = 3,state= pagerState,modifier = Modifier
            .padding(paddingValue)) {page->
            when (page) {
                0 -> ChatsUser(
//                    modifier = Modifier,
                    lazyPagingItems = viewModel.pagedList.collectAsLazyPagingItems(),
                    formatShortRelativeTime = formatShortRelativeTime,
                    navigateToChat = {id,parentId,type->
                        navController.navigate(Route.CHAT_GRUPO + "?id=$id&parentId=$parentId&typeChat=$type") },
//                    navigateToEstablecimientoInbox = { establecimientoId:Long,conversationId:Long->
//                        navController.navigate(Route.CONVERSATION  id conversationId id establecimientoId)
//                    },
//                    navigateToChatSala = {chatId:Long,salaId:Long->
//                        navController.navigate(Route.CHAT_SALA + "?id=$chatId&salaId=$salaId")
//                    }
                )
                1 -> filterGroups()
                2 -> userSalas()
            }
        }
    }
}



//@Composable
//fun GrupoItem(
//    grupo:Grupo,
//    navigateToChatGrupo: (id: Long) -> Unit,
//    formatShortRelativeTime:(Instant)->String,
//    modifier:Modifier = Modifier
//) {
//    Row(
//        modifier = modifier
//            .fillMaxWidth()
//            .clickable { navigateToChatGrupo(grupo.id) }
//            .padding(10.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        PosterCardImage(
//            model = grupo.photo, modifier = Modifier
//                .size(70.dp), shape = CircleShape
//        )
//        Spacer(modifier = Modifier.width(10.dp))
//        Column() {
//            Row(modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
//                Text(text = grupo.name ,style = MaterialTheme.typography.labelLarge,
//                    modifier = Modifier.fillMaxWidth(0.65f), overflow = TextOverflow.Ellipsis)
//                if(grupo.last_message_created != null){
//                    Text(text = formatShortRelativeTime(grupo.last_message_created!!),
//                        style = MaterialTheme.typography.labelSmall, maxLines = 1,
//                    overflow = TextOverflow.Ellipsis)
//                }
//            }
//
//            if(grupo.last_message.isNotBlank()){
//                Spacer(modifier = Modifier.height(5.dp))
//                Text(text = grupo.last_message,style = MaterialTheme.typography.labelMedium.copy(
//                    fontWeight = FontWeight.Normal
//                ), overflow = TextOverflow.Ellipsis, maxLines = 1)
//            }
//        }
//
//    }
//}
//



@Composable
internal fun Indicators(
    navToTab:(tab:Int) ->Unit,
    createGroup:()->Unit,
    currentTab:Int,
    appAuthState:AppAuthState,
    navigateToCreateSala:()->Unit,
    openAuthBottomSheet: () -> Unit,
    navigateToUserGrupoRequests: () -> Unit,
    modifier:Modifier = Modifier
){
    var expanded by remember { mutableStateOf(false) }
//    val widthTab = LocalConfiguration.current.screenWidthDp.dp /4
    Row(modifier = Modifier
        .padding(top = 12.dp, start = 5.dp, end = 5.dp)
        .fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
    ScrollableTabRow(selectedTabIndex = currentTab, edgePadding = 1.dp,
        modifier = modifier.fillMaxWidth(0.8f)) {
        Tab(
            text = { Text(text = "Mis Grupos", style = MaterialTheme.typography.labelMedium) },
            selected = currentTab == 0,
            onClick = {
                navToTab(0)
            },
        )
        Tab(
            text = { Text(text = stringResource(id = R.string.groups),style = MaterialTheme.typography.labelMedium) },
            selected = currentTab ==1,
            onClick = {
                // Animate to the selected page when clicked
                navToTab(1)
            },
        )
        Tab(
            text = { Text(text = stringResource(id = R.string.rooms),style = MaterialTheme.typography.labelMedium) },
            selected = currentTab ==1,
            onClick = {
                // Animate to the selected page when clicked
                navToTab(2)
            },
        )
    }
        AnimatedVisibility(visible = currentTab != 1) {
        Row() {
            Box() {
            IconButton(onClick = { expanded = !expanded }) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "more_vert")
            }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    if(currentTab == 0) {
                        DropdownMenuItem(
                            text = { Text(text = stringResource(id = R.string.create_group)) },
                            onClick = {
                                if (appAuthState == AppAuthState.LOGGED_IN) {
                                    createGroup()
                                }else{
                                    openAuthBottomSheet()
                                }
                            }
                        )
                    }
                    if(currentTab == 2){
                    DropdownMenuItem(
                        text = { Text(text = stringResource(id = R.string.create_sala)) },
                        onClick = {
                            if (appAuthState == AppAuthState.LOGGED_IN) {
                                navigateToCreateSala()
                            } else {
                                openAuthBottomSheet()
                            }
                        }
                    )
                    }


                    DropdownMenuItem(
                        text = { Text(text = stringResource(id = R.string.requests)) },
                        onClick = {
                            if (appAuthState == AppAuthState.LOGGED_IN) {
                                navigateToUserGrupoRequests()
                            } else {
                                openAuthBottomSheet()
                            }
                        }
                    )

                    DropdownMenuItem(
                        text = { Text(text = stringResource(id = R.string.invitations)) },
                        onClick = {

                        }
                    )
                    }
                }
            }
//                AnimatedVisibility(visible = currentTab == 1) {
//                    IconButton(onClick = { /*TODO*/ }) {
//                        Icon(imageVector = Icons.Default.Sort, contentDescription = "sort")
//                    }
//                }
        }
        }
}

package app.regate.grupo

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.DoorBack
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.ViewSidebar
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DismissValue
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.SavedStateHandle
import app.regate.common.composes.LocalAppDateFormatter
import app.regate.common.composes.components.CustomButton
import app.regate.common.composes.components.card.InstalacionCard
import app.regate.common.composes.components.dialog.DialogConfirmation
import app.regate.common.composes.components.item.ProfileItem
import app.regate.common.composes.components.item.SalaItem
import app.regate.common.composes.components.skeleton.SalaItemSkeleton
import app.regate.common.composes.ui.CommonTopBar
import app.regate.common.composes.ui.PosterCardImage
import app.regate.common.composes.ui.Skeleton
import app.regate.common.composes.util.Layout
import app.regate.common.composes.util.dividerLazyList
import app.regate.common.composes.util.spacerLazyList
import app.regate.common.composes.viewModel
import app.regate.data.auth.AppAuthState
import kotlinx.datetime.toInstant
import app.regate.common.resources.R
import app.regate.data.dto.empresa.salas.SalaDto
import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import kotlin.time.Duration.Companion.minutes

typealias Grupo = @Composable (
    navigateUp:()->Unit,
    editGroup:(Long)->Unit,
//    navigateToChat:(id:Long)->Unit,
    openAuthBottomSheet:()->Unit,
    createSala:(id:Long)->Unit,
    navigateToSala:(id:Long)->Unit,
        ) -> Unit

@Inject
@Composable
fun Grupo(
    viewModelFactory:(SavedStateHandle)-> GrupoViewModel,
    @Assisted navigateUp: () -> Unit,
    @Assisted editGroup: (Long) -> Unit,
    @Assisted openAuthBottomSheet: () -> Unit,
    @Assisted createSala: (id:Long) -> Unit,
    @Assisted navigateToSala: (id: Long) -> Unit
){
    Grupo(
        viewModel = viewModel(factory = viewModelFactory),
        navigateUp = navigateUp,
//        navigateToChat= navigateToChat,
        openAuthBottomSheet = openAuthBottomSheet,
        createSala = createSala,
        navigateToSala = navigateToSala,
        editGroup = editGroup
    )
}

@Composable
internal fun Grupo(
    viewModel: GrupoViewModel,
    navigateUp: () -> Unit,
    editGroup: (Long) -> Unit,
//    navigateToChat: (id:Long) -> Unit,
    openAuthBottomSheet: () -> Unit,
    createSala: (id:Long) -> Unit,
    navigateToSala: (id: Long) -> Unit
){
    val viewState by viewModel.state.collectAsState()
    val formatter = LocalAppDateFormatter.current
    val joinSalaDialog = remember {
        mutableStateOf(false)
    }
    Grupo(
        viewState = viewState,
        navigateUp = navigateUp,
        formatShortTime = {formatter.formatShortTime(it)},
        formatDate = {formatter.formatWithSkeleton(it.toEpochMilliseconds(),formatter.monthDaySkeleton)},
//        navigateToChat = navigateToChat,
        openAuthBottomSheet = openAuthBottomSheet,
        openDialogConfirmation = {joinSalaDialog.value = true},
        clearMessage = viewModel::clearMessage,
        refresh = viewModel::refresh,
        createSala = createSala,
        navigateToSala = navigateToSala,
        editGroup = editGroup,
        selectUser = viewModel::selectUser,
        removeUserFromGroup = viewModel::removeUserFromGroup,
        removeAdminUser = viewModel::removeUserAdmin,
        addAdminUser = viewModel::addUserAdmin,
        leaveGroup = { viewModel.leaveGroup(navigateUp) }
    )
    DialogConfirmation(open = joinSalaDialog.value,
        dismiss = { joinSalaDialog.value = false },
        confirm = {
            viewModel.joinGrupo()
            joinSalaDialog.value = false
        }
    )

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun Grupo(
    viewState: GrupoState,
    navigateUp: () -> Unit,
    formatShortTime:(time:Instant)->String,
    formatDate:(date:Instant)->String,
    editGroup: (Long) -> Unit,
//    navigateToChat: (id:Long) -> Unit,
    openAuthBottomSheet: () -> Unit,
    openDialogConfirmation:()->Unit,
    refresh:()->Unit,
    clearMessage:(id:Long)->Unit,
    createSala: (id:Long) -> Unit,
    navigateToSala: (id: Long) -> Unit,
    selectUser:(Long)->Unit,
    removeUserFromGroup:()->Unit,
    removeAdminUser:()->Unit,
    addAdminUser:()->Unit,
    leaveGroup:()->Unit
    ) {
    val isLogged by remember(viewState.authState){
        derivedStateOf {
            viewState.authState == AppAuthState.LOGGED_IN
        }
    }
    var dialogUserMenu by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    var expanded by remember { mutableStateOf(false) }
    val refreshState = rememberPullRefreshState(refreshing = false, onRefresh = { refresh()})
    viewState.message?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message.message)
            clearMessage(message.id)
        }
    }
    if(dialogUserMenu){
        Dialog(onDismissRequest = { dialogUserMenu = false }) {
            Column(modifier = Modifier
                .padding(10.dp)
                .clip(MaterialTheme.shapes.small)
                .background(MaterialTheme.colorScheme.background)
                .fillMaxWidth()
            ) {
             Text(text = "${viewState.selectedUser?.nombre} ${viewState.selectedUser?.apellido?:""}",
             style = MaterialTheme.typography.titleSmall,modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp))
            Text(text = stringResource(id = R.string.remove_user),modifier = Modifier
                .clickable { removeUserFromGroup();dialogUserMenu = false }
                .padding(8.dp)
                .fillMaxWidth(),
                style = MaterialTheme.typography.labelLarge
            )
                Divider()
            Text(text = stringResource(id = R.string.meke_user_admin),modifier = Modifier
                .clickable { addAdminUser();dialogUserMenu = false }
                .padding(8.dp)
                .fillMaxWidth(),
                style = MaterialTheme.typography.labelLarge
            )
                Divider()
                Text(text = stringResource(id = R.string.remove_user_admin),modifier = Modifier
                    .clickable { removeAdminUser();dialogUserMenu = false }
                    .padding(8.dp)
                    .fillMaxWidth(),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
    Scaffold(
        topBar = {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
          IconButton(onClick = { navigateUp() }) {
              Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back_arrow")
          }
            Box(modifier = Modifier){
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = "dots_more")
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    DropdownMenuItem(
                        text = { Text(text = stringResource(id = R.string.create_sala)) },
                        onClick = { viewState.grupo?.let { createSala(it.id) } }
                    )
                    if(viewState.user?.user_id == viewState.grupo?.user_id){
                    DropdownMenuItem(
                        text = { Text("Editar") },
                        onClick = { viewState.grupo?.id?.let { editGroup(it) } }
                    )
                    }
                }
            }
            }
        },
        floatingActionButton = {
            if(viewState.user?.profile_id !in viewState.usersProfileGrupo.map { it.id })
                               Button(onClick =  {
                                   if(isLogged){ openDialogConfirmation() }else{ openAuthBottomSheet() }
                               }) {
                                   Text(text = "Unirme")
                               }
        },
        floatingActionButtonPosition = FabPosition.Center,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        modifier = Modifier
            .padding(10.dp)
    ) { paddingValues ->
        Box(modifier = Modifier
            .pullRefresh(state = refreshState)
            .padding(paddingValues)
            .fillMaxSize()) {

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                viewState.grupo?.let { grupo ->
                    item {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            PosterCardImage(
                                model = grupo.photo,
                                modifier = Modifier
                                    .size(100.dp)
                                    .align(Alignment.Center),
                                shape = CircleShape
                            )
                        }
                    }
                    spacerLazyList()
                    item {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = grupo.name,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                    dividerLazyList()
                    item{
                        Text(text = stringResource(id = R.string.group_description),modifier = Modifier.padding(5.dp),
                        style = MaterialTheme.typography.labelLarge)
                    }
                    item {
                        grupo.description?.let { Text(text = it, style = MaterialTheme.typography.bodySmall) }
                    }
                    dividerLazyList()
                        item {
                            Text(
                                text = stringResource(R.string.rooms),
                                style = MaterialTheme.typography.labelLarge,
                            )
                        }

                    if(viewState.loading){
                        item{
                        SalaItemSkeleton()
                        }
                    }else{
                    if(viewState.salas.isNotEmpty()) {
                        items(
                            items = viewState.salas,
//                            key = { it. }
                        ) { sala ->
                            SalaItem(
                                sala = sala,
                                formatDate = formatDate,
                                navigateToSala = navigateToSala,
                                formatShortTime = formatShortTime
                            )
                        }
                    }else{
                        item {
                            Box(modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth()) {
                            OutlinedButton(onClick = { createSala(grupo.id) },modifier = Modifier.align(
                                Alignment.Center)) {
                                Text(text = stringResource(id = R.string.create_sala))
                            }
                            }
                        }
                    }
                    }
                    dividerLazyList()

                    item {
                        Text(
                            text = "${viewState.usersProfileGrupo.size} miembros",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                    spacerLazyList()
                    items(
                        items = viewState.usersProfileGrupo,
                        key = { it.id }
                    ) { profile ->
                        ProfileItem(
                            nombre = profile.nombre,
                            apellido = profile.apellido,
                            photo = profile.profile_photo,
                            is_admin = profile.is_admin,
                            isCurrentUserAdmin= viewState.isAdmin,
                            selectUser = {
                                dialogUserMenu = true
                                selectUser(profile.id)
                            }
                        )
                    }

                    item{
                        Spacer(modifier = Modifier.height(10.dp))
                        TextButton(modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(), onClick = { leaveGroup() },
                        shape = CircleShape,
                        border = BorderStroke(width = 1.dp,color = MaterialTheme.colorScheme.error)
                        ) {
                            Text(text= stringResource(id = R.string.leave_group),color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }

//            PullRefreshIndicator(
//                refreshing = viewState.loading,
//                state = refreshState,
//                modifier = Modifier
//                    .align(Alignment.TopCenter)
//                    .padding(paddingValues)
//                    .padding(top = 20.dp),
//                scale = true,
//            )
        }
    }
}






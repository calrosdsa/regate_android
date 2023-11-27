package app.regate.sala

import android.content.Context
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import app.regate.common.composes.LocalAppDateFormatter
import app.regate.common.composes.component.CustomButton
import app.regate.common.composes.component.card.InstalacionCard
import app.regate.common.composes.component.dialog.DialogConfirmation
import app.regate.common.composes.component.dialog.LoaderDialog
import app.regate.common.composes.component.item.ProfileItem
import app.regate.common.composes.ui.PosterCardImage
import app.regate.common.composes.util.dividerLazyList
import app.regate.common.composes.util.spacerLazyList
import app.regate.common.composes.viewModel
import app.regate.data.auth.AppAuthState
import app.regate.common.resources.R
import app.regate.constant.Route
import app.regate.constant.id
import app.regate.data.dto.empresa.salas.SalaEstado
import app.regate.data.mappers.toInstalacion
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

typealias Sala = @Composable (
    navigateUp:()->Unit,
    openAuthBottomSheet:()->Unit,
    navigateToInstalacion:(Long) -> Unit,
    navigateToEstablecimiento:(Long)->Unit,
    navigateToComplete:(Long) -> Unit,
    navigateToSelectGroup:(String)->Unit,
    navController:NavController,
    ) -> Unit

@Inject
@Composable
fun Sala(
    viewModelFactory: (SavedStateHandle) -> SalaViewModel,
    @Assisted navigateUp: () -> Unit,
    @Assisted openAuthBottomSheet: () -> Unit,
    @Assisted navigateToInstalacion: (Long) -> Unit,
    @Assisted navigateToEstablecimiento: (Long) -> Unit,
    @Assisted navigateToComplete:(Long) -> Unit,
    @Assisted navigateToSelectGroup: (String) -> Unit,
    @Assisted navController: NavController
) {

    Sala(
        viewModel = viewModel(factory = viewModelFactory),
        navigateUp = navigateUp,
        openAuthBottomSheet = openAuthBottomSheet,
        navigateToEstablecimiento = navigateToEstablecimiento,
        navigateToInstalacion = navigateToInstalacion,
        navigateToComplete = navigateToComplete,
        navigateToSelectGroup = navigateToSelectGroup,
//        navigateToGroup = { navController.navigate(Route.GRUPO id it) },
        navigateToInfoGrupo = {navController.navigate(Route.INFO_GRUPO id it)},
        navigateToChat = {id,parentId,type->
            navController.navigate(Route.CHAT_GRUPO + "?id=$id&parentId=$parentId&typeChat=$type") },
        )
}

@Composable
internal fun Sala(
    viewModel: SalaViewModel,
    navigateUp: () -> Unit,
    navigateToChat: (id: Long,parentId:Long,typeChat:Int) -> Unit,
    openAuthBottomSheet: () -> Unit,
    navigateToEstablecimiento: (Long) -> Unit,
    navigateToInstalacion: (Long) -> Unit,
    navigateToComplete: (Long) -> Unit,
    navigateToSelectGroup: (String) -> Unit,
    navigateToInfoGrupo:(Long)->Unit
){
    val viewState by viewModel.state.collectAsState()
    val formatter = LocalAppDateFormatter.current
//    val context = LocalContext.current
    val joinSalaDialog = remember {
        mutableStateOf(false)
    }
    Sala(
        viewState = viewState,
        navigateUp = navigateUp,
        formatShortTime = formatter::formatShortTime,
        formatDate = formatter::formatShortDate,
        navigateToChat = {
           viewModel.navigateToChat(it,navigateToChat)
        },
        openAuthBottomSheet = openAuthBottomSheet,
        openDialogConfirmation = {joinSalaDialog.value = true},
        clearMessage = viewModel::clearMessage,
        refresh = viewModel::refresh,
        navigateToInstalacion = navigateToInstalacion,
        navigateToEstablecimiento = navigateToEstablecimiento,
//        exitSala = {viewModel.exitSala(context,navigateUp)},
        navigateToComplete = navigateToComplete,
        shareSalaWithGroup = {
            viewModel.navigateSelect(navigateToSelectGroup)
        },
//        navigateToGroup = navigateToGroup,
        navigateToInfoGrupo = navigateToInfoGrupo,
        exitSala = viewModel::exitSala,
    )
    DialogConfirmation(open = joinSalaDialog.value,
        dismiss = { joinSalaDialog.value = false },
        confirm = {
            viewModel.joinSala()
            joinSalaDialog.value = false
        }
    )

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun Sala(
    viewState: SalaState,
    navigateUp: () -> Unit,
    formatShortTime:(time:String,plusMinutes:Long)->String,
    formatDate:(date:String)->String,
    navigateToChat: (id: Long) -> Unit,
    openAuthBottomSheet: () -> Unit,
    openDialogConfirmation:()->Unit,
    refresh:()->Unit,
    clearMessage:(id:Long)->Unit,
    navigateToInstalacion: (Long) -> Unit,
    navigateToEstablecimiento: (Long) -> Unit,
    exitSala:(Context,()->Unit)->Unit,
    navigateToComplete: (Long) -> Unit,
    shareSalaWithGroup:()->Unit,
//    navigateToGroup:(Long)->Unit,
    navigateToInfoGrupo: (Long) -> Unit
) {
    val context = LocalContext.current
    val participantes = remember(viewState.data?.profiles) {
        derivedStateOf {
            viewState.data?.profiles?.size
        }
    }
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val isPriceOverlap by remember(viewState.data) {
        derivedStateOf {
            viewState.data?.sala?.let { sala ->
                (sala.paid + sala.precio_cupo) > sala.precio
            }
        }
    }
    val iAmInTheRoom by remember(key1 = viewState.data, key2 = viewState.authState) {
        derivedStateOf {
            viewState.user?.profile_id?.let { it2 ->
                viewState.data?.profiles?.map { it.profile_id }?.contains(
                    it2
                ) ?: false
            }
        }
    }
    val snackbarHostState = remember { SnackbarHostState() }

    val refreshState = rememberPullRefreshState(refreshing = false, onRefresh = { refresh() })
    viewState.message?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message.message)
            // Notify the view model that the message has been dismissed
            clearMessage(message.id)
        }
    }
    LoaderDialog(loading = viewState.loading)

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        ModalNavigationDrawer(drawerState = drawerState,
            drawerContent = {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    ModalDrawerSheet(
                        drawerShape = RoundedCornerShape(0.dp),
                        modifier = Modifier.fillMaxWidth(0.7f)
                    ) {
                        SalaMenu(
                            leaveRoom = {exitSala(context,navigateUp)},
                            shareSalaWithGroup = shareSalaWithGroup,
                            imIntheRoom = iAmInTheRoom?: false,
                            navigateToChat = {
                                viewState.data?.sala?.let {
                                    navigateToChat(
                                        it.id,
                                    )
                                }
                            },
                            salaTitle = viewState.data?.sala?.titulo?:"",
                            navigateToComplete = { viewState.data?.sala?.let { navigateToComplete(it.id) } },
                            navigateToInfoGrupo = { viewState.data?.sala?.let { navigateToInfoGrupo(it.grupo_id)}}
//                            navigateToGroup = { viewState.data?.sala?.let { navigateToGroup(it.grupo_id)}},
                            )

                    }
                }
            }) {
            Scaffold(
                topBar = {
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                        SalaTopBar(
                            onBack = navigateUp,
                            openDrawer = {
                                coroutineScope.launch { drawerState.open() }
                            }
                        )
                    }
                },

                snackbarHost = {
                    SnackbarHost(hostState = snackbarHostState)
                },
                modifier = Modifier
                    .fillMaxSize()
            ) { paddingValues ->
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {

                    Box(
                        modifier = Modifier
                            .pullRefresh(state = refreshState)
                            .padding(paddingValues)
                            .padding(10.dp)
                            .fillMaxSize()
                    ) {
                        viewState.data?.let { data ->

                            LazyColumn(modifier = Modifier.fillMaxSize()) {
                                data.sala.let { sala ->
                                    item {
                                        Text(
                                            text = sala.titulo,
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                    }
                                    spacerLazyList()
                                    item {
                                        Text(
                                            text = sala.descripcion,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                    spacerLazyList()
                                    item {
                                        when (sala.estado) {
                                            SalaEstado.RESERVED.ordinal -> {
                                                Text(
                                                    text = stringResource(id = R.string.reserved_room),
                                                    style = MaterialTheme.typography.labelLarge,
                                                    color = MaterialTheme.colorScheme.primary
                                                )
                                            }

                                            SalaEstado.UNAVAILABLE.ordinal -> {
                                                Text(
                                                    text = stringResource(id = R.string.room_not_available),
                                                    style = MaterialTheme.typography.labelLarge,
                                                    color = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                        }
                                    }
                                    spacerLazyList()
                                    item {
                                        Text(
                                            text = "Hora en la que se jugara",
                                            style = MaterialTheme.typography.labelLarge
                                        )
                                    }
                                    item {
                                        Text(
                                            text = "${formatDate(sala.horas.first())} ${
                                                formatShortTime(
                                                    sala.horas.first(),
                                                    0
                                                )
                                            } a ${
                                                formatShortTime(sala.horas.last(), 30)
                                            }",
                                            style = MaterialTheme.typography.titleSmall
                                        )
                                    }
                                    item {
                                        Divider(modifier = Modifier.padding(top = 10.dp))
                                    }
                                    item {
                                        Row(modifier = Modifier
                                            .clickable { navigateToEstablecimiento(1) }
                                            .fillMaxWidth()
                                            .padding(vertical = 5.dp),
                                            verticalAlignment = Alignment.CenterVertically) {
                                            PosterCardImage(
                                                model = sala.establecimiento_photo,
                                                modifier = Modifier.size(40.dp), shape = CircleShape
                                            )
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Text(
                                                text = sala.establecimiento_name,
                                                style = MaterialTheme.typography.labelLarge,
                                                maxLines = 1
                                            )
                                        }
                                    }
                                    item {
                                        InstalacionCard(
                                            instalacion = data.instalacion.copy(precio_hora = sala.precio)
                                                .toInstalacion(),
                                            navigate = navigateToInstalacion,
                                            modifier = Modifier
                                                .height(200.dp)
                                                .clip(MaterialTheme.shapes.large)
                                                .fillMaxWidth(),
                                            imageHeight = 155.dp

                                        )
                                    }
                                    spacerLazyList()
                                    item {
                                        Row(
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(40.dp)
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = "${participantes.value}/${sala.cupos}",
                                                    style = MaterialTheme.typography.labelLarge
                                                )
                                                Spacer(modifier = Modifier.width(5.dp))
                                                Icon(
                                                    imageVector = Icons.Default.Group,
                                                    contentDescription = sala.titulo,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            }

                                            if (sala.estado == SalaEstado.AVAILABLE.ordinal ||
                                                sala.estado == SalaEstado.RESERVED.ordinal
                                            ) {
                                                if (iAmInTheRoom == false) {
                                                    if (isPriceOverlap == false) {
                                                        CustomButton(onClick = {
                                                            if (viewState.authState == AppAuthState.LOGGED_IN) {
                                                                openDialogConfirmation()
                                                            } else {
                                                                openAuthBottomSheet()
                                                            }
                                                        }) {
                                                            Text(text = "Unirse: ${sala.precio_cupo}")
                                                        }
                                                    }
                                                } else {
                                                    CustomButton(onClick = {
                                                        if (viewState.authState == AppAuthState.LOGGED_IN) {
                                                            navigateToComplete(sala.id)
                                                        } else {
                                                            openAuthBottomSheet()
                                                        }
                                                    }) {
                                                        Text(text = "Completar ${sala.paid}/${sala.precio}")
                                                    }
                                                }
                                            }

                                        }
                                    }
                                    dividerLazyList()
                                    item {
                                        Text(
                                            text = "${participantes.value} participantes",
                                            style = MaterialTheme.typography.labelLarge
                                        )
                                    }
                                    spacerLazyList()
                                    items(
                                        items = data.profiles,
//                        key = { it.profile_id }
                                    ) { profile ->
                                        ProfileItem(
                                            id = profile.profile_id,
                                            nombre = profile.nombre,
                                            apellido = profile.apellido,
                                            photo = profile.profile_photo,
                                            isMe = viewState.user?.profile_id == profile.profile_id
                                        )
                                    }


                                }
                            }
                        }


                        PullRefreshIndicator(
                            refreshing = viewState.loading,
                            state = refreshState,
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .padding(paddingValues)
                                .padding(top = 20.dp),
                            scale = true,
                        )
                    }
                }
            }
        }
    }
}



@Composable
fun SalaTopBar(
    onBack:()->Unit,
    openDrawer:() ->Unit,
    modifier:Modifier = Modifier,
) {
    val expanded = remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { onBack() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back_esta")
            }
            Row() {

                Box(modifier = Modifier) {

                    IconButton(onClick = {
                        expanded.value = !expanded.value
                    }) {
                        Icon(imageVector = Icons.Default.Info, contentDescription = "back_esta")
                    }
                    DropdownMenu(
                        expanded = expanded.value,
                        onDismissRequest = { expanded.value = false },
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = stringResource(id = R.string.sala_help_text),
                                style = MaterialTheme.typography.labelMedium
                            )
                            Text(
                                text = stringResource(id = R.string.sala_help_text2),
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }

                IconButton(onClick = { openDrawer() }) {
                    Icon(imageVector = Icons.Default.Menu, contentDescription = "menu")
                }


            }
        }
            Divider()
    }
}
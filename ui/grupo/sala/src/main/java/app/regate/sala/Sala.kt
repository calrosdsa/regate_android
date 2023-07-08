package app.regate.sala

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import app.regate.common.composes.LocalAppDateFormatter
import app.regate.common.composes.components.CustomButton
import app.regate.common.composes.components.card.InstalacionCard
import app.regate.common.composes.components.dialog.DialogConfirmation
import app.regate.common.composes.components.item.ProfileItem
import app.regate.common.composes.ui.CommonTopBar
import app.regate.common.composes.util.Layout
import app.regate.common.composes.util.dividerLazyList
import app.regate.common.composes.util.spacerLazyList
import app.regate.common.composes.viewModel
import app.regate.data.auth.AppAuthState
import kotlinx.datetime.Instant
import kotlinx.datetime.toInstant
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import kotlin.time.Duration.Companion.minutes

typealias Sala = @Composable (
    navigateUp:()->Unit,
    navigateToChat:(id:Long)->Unit,
    openAuthBottomSheet:()->Unit
        ) -> Unit

@Inject
@Composable
fun Sala(
    viewModelFactory:(SavedStateHandle)-> SalaViewModel,
    @Assisted navigateUp: () -> Unit,
    @Assisted navigateToChat: (id:Long) -> Unit,
    @Assisted openAuthBottomSheet: () -> Unit
){
    Sala(
        viewModel = viewModel(factory = viewModelFactory),
        navigateUp = navigateUp,
        navigateToChat= navigateToChat,
        openAuthBottomSheet = openAuthBottomSheet
    )
}

@Composable
internal fun Sala(
    viewModel: SalaViewModel,
    navigateUp: () -> Unit,
    navigateToChat: (id:Long) -> Unit,
    openAuthBottomSheet: () -> Unit
){
    val viewState by viewModel.state.collectAsState()
    val formatter = LocalAppDateFormatter.current
    val joinSalaDialog = remember {
        mutableStateOf(false)
    }
    Sala(
        viewState = viewState,
        navigateUp = navigateUp,
        formatShortTime = {formatter.formatShortTime(it)},
        formatDate = {formatter.formatWithSkeleton(it.toEpochMilliseconds(),formatter.monthDaySkeleton)},
        navigateToChat = navigateToChat,
        openAuthBottomSheet = openAuthBottomSheet,
        openDialogConfirmation = {joinSalaDialog.value = true},
        clearMessage = viewModel::clearMessage,
        refresh = viewModel::refresh
    )
    DialogConfirmation(open = joinSalaDialog.value,
        dismiss = { joinSalaDialog.value = false },
        confirm = {
            viewModel.joinSala()
            joinSalaDialog.value = false
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
internal fun Sala(
    viewState: SalaState,
    navigateUp: () -> Unit,
    formatShortTime:(time:Instant)->String,
    formatDate:(date:Instant)->String,
    navigateToChat: (id:Long) -> Unit,
    openAuthBottomSheet: () -> Unit,
    openDialogConfirmation:()->Unit,
    refresh:()->Unit,
    clearMessage:(id:Long)->Unit
) {
    val participantes = remember(viewState.profiles) {
        derivedStateOf {
            viewState.profiles.size
        }
    }
    val snackbarHostState = remember { SnackbarHostState() }
    val dismissSnackbarState = rememberDismissState(
        confirmValueChange = { value ->
            if (value != DismissValue.Default) {
                snackbarHostState.currentSnackbarData?.dismiss()
                true
            } else {
                false
            }
        },
    )
    val refreshState = rememberPullRefreshState(refreshing = false, onRefresh = { refresh()})
    viewState.message?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message.message)
            // Notify the view model that the message has been dismissed
            clearMessage(message.id)
        }
    }
    Scaffold(
        topBar = {
            CommonTopBar(onBack = navigateUp)
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewState.sala?.let { navigateToChat(it.grupo_id) } }) {
                Icon(imageVector = Icons.Default.Chat, contentDescription = "chat")
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                SwipeToDismiss(
                    state = dismissSnackbarState,
                    background = {},
                    dismissContent = { Snackbar(snackbarData = data) },
                    modifier = Modifier
                        .padding(horizontal = Layout.bodyMargin)
                        .fillMaxWidth(),
                )
            }
        },
        modifier = Modifier
            .padding(10.dp)
    ) { paddingValues ->
        Box(modifier = Modifier
            .pullRefresh(state = refreshState)
            .padding(paddingValues).fillMaxSize()) {

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                viewState.sala?.let { sala ->
                    item {
                        Text(text = sala.titulo, style = MaterialTheme.typography.titleMedium)
                    }
                    spacerLazyList()
                    item {
                        Text(text = sala.descripcion, style = MaterialTheme.typography.bodySmall)
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
                            text = "${formatDate(sala.fecha)} ${formatShortTime(sala.start_time)} a ${
                                formatShortTime(
                                    sala.end_time.plus(30.minutes)
                                )
                            }",
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                    item {
                        Divider(modifier = Modifier.padding(vertical = 10.dp))
                    }
                    item {
                        viewState.instalacion?.let {
                            InstalacionCard(
                                instalacion = it.copy(precio_hora = sala.precio),
                                navigate = {},
                                modifier = Modifier
                                    .height(200.dp)
                                    .clip(MaterialTheme.shapes.large)
                                    .fillMaxWidth(),
                                imageHeight = 155.dp

                            )
                        }
                    }
                    spacerLazyList()
                    item {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "${participantes.value}/${sala.cupos}",
                                style = MaterialTheme.typography.labelLarge
                            )
                            CustomButton(onClick = {
                                if (viewState.authState == AppAuthState.LOGGED_IN) {
                                    openDialogConfirmation()
                                } else {
                                    openAuthBottomSheet()
                                }
                            }) {
                                Text(text = "Unirse: ${sala.precio / sala.cupos}")
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
                        items = viewState.profiles,
                        key = { it.profile_id }
                    ) { profile ->
                        ProfileItem(
                            nombre = profile.nombre,
                            apellido = profile.apellido,
                            photo = profile.profile_photo
                        )
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


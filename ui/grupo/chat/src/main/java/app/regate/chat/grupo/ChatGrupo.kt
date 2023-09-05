package app.regate.chat.grupo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardDoubleArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.SavedStateHandle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import app.regate.common.composes.LocalAppDateFormatter
import app.regate.common.composes.components.input.ChatInput
import app.regate.common.composes.util.appendErrorOrNull
import app.regate.common.composes.util.prependErrorOrNull
import app.regate.common.composes.util.refreshErrorOrNull
import app.regate.common.composes.viewModel
import app.regate.compoundmodels.MessageProfile
import app.regate.compoundmodels.UserProfileGrupo
import app.regate.data.common.MessageData
import app.regate.data.common.ReplyMessageData
import app.regate.data.dto.empresa.grupo.CupoInstalacion
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

typealias ChatGrupo  = @Composable (
    navigateUp:()->Unit,
    openAuthBottomSheet:()->Unit,
    navigateToCreateSala:(id:Long)->Unit,
    navigateToGroup:(id:Long)->Unit,
    navigateToInstalacionReserva:(Long,Long)->Unit
        ) -> Unit

@Inject
@Composable
fun ChatGrupo(
    viewModelFactory:(SavedStateHandle)-> ChatGrupoViewModel,
    @Assisted navigateUp: () -> Unit,
    @Assisted  openAuthBottomSheet:()->Unit,
    @Assisted navigateToCreateSala: (id: Long) -> Unit,
    @Assisted navigateToGroup: (id: Long) -> Unit,
    @Assisted navigateToInstalacionReserva:(Long,Long)->Unit
){
    ChatGrupo(
        viewModel = viewModel(factory = viewModelFactory),
        navigateUp = navigateUp,
        openAuthBottomSheet = openAuthBottomSheet,
        navigateToCreateSala = navigateToCreateSala,
        navigateToGroup = navigateToGroup,
        navigateToInstalacionReserva = navigateToInstalacionReserva
    )
}

@Composable
internal fun ChatGrupo(
    viewModel: ChatGrupoViewModel,
    navigateUp: () -> Unit,
    openAuthBottomSheet: () -> Unit,
    navigateToCreateSala: (id: Long) -> Unit,
    navigateToGroup: (id: Long) -> Unit,
    navigateToInstalacionReserva: (Long, Long) -> Unit,
){
    val viewState by viewModel.state.collectAsState()

    val formatter = LocalAppDateFormatter.current
    ChatGrupo(
        viewState = viewState,
        lazyPagingItems = viewModel.pagedList.collectAsLazyPagingItems(),
        navigateUp = navigateUp,
        sendMessage = viewModel::sendMessage,
        openAuthBottomSheet = openAuthBottomSheet,
        formatterRelativeTime = formatter::formatShortRelativeTime,
        clearMessage = viewModel::clearMessage,
        navigateToCreateSala = navigateToCreateSala,
        navigateToGroup = navigateToGroup,
        getUserProfileGrupo = viewModel::getUserGrupo,
        formatShortDate = {
            formatter.formatWithSkeleton(it.toEpochMilliseconds(),formatter.monthDaySkeleton)
        },
        formatShortTime = formatter::formatShortTime,
        navigateToInstalacionReserva = {instalacionId,establecimientoId,cupos->
            viewModel.navigateToInstalacionReserva(instalacionId,establecimientoId,cupos,navigateToInstalacionReserva)
        },
//        formatShortTime = {formatter.formatShortTime(it.toInstant())},
//        formatDate = {formatter.formatWithSkeleton(it.toInstant().toEpochMilliseconds(),formatter.monthDaySkeleton)}
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun ChatGrupo(
    viewState: ChatGrupoState,
    lazyPagingItems: LazyPagingItems<MessageProfile>,
    navigateUp: () -> Unit,
    sendMessage:(MessageData,()->Unit)->Unit,
    openAuthBottomSheet: () -> Unit,
    formatterRelativeTime:(date:Instant)->String,
    clearMessage:(id:Long)->Unit,
    navigateToCreateSala: (id: Long) -> Unit,
    navigateToGroup: (id: Long) -> Unit,
    getUserProfileGrupo:(id:Long)->UserProfileGrupo?,
    formatShortDate:(Instant)->String,
    formatShortTime:(Instant)->String,
    navigateToInstalacionReserva: (Long, Long,List<CupoInstalacion>) -> Unit,
) {
    val colors = listOf(
        MaterialTheme.colorScheme.inverseOnSurface,
        MaterialTheme.colorScheme.inverseOnSurface
    )
    val message = remember {
        mutableStateOf("")
    }
    val coroutineScope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val replyMessage = remember {
        mutableStateOf<ReplyMessageData?>(null)
    }
    val lazyListState = rememberLazyListState()

    val snackbarHostState = remember { SnackbarHostState() }
    val isLastItemVisible by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex > 5
        }
    }

    viewState.message?.let { messageSnack ->
        LaunchedEffect(messageSnack) {
            snackbarHostState.showSnackbar(messageSnack.message)
            clearMessage(messageSnack.id)
        }
    }

    lazyPagingItems.loadState.prependErrorOrNull()?.let {
        LaunchedEffect(it) {
            snackbarHostState.showSnackbar(it.message)
        }
    }
    lazyPagingItems.loadState.appendErrorOrNull()?.let {
        LaunchedEffect(it) {
            snackbarHostState.showSnackbar(it.message)
        }
    }
    lazyPagingItems.loadState.refreshErrorOrNull()?.let {
        LaunchedEffect(it) {
            snackbarHostState.showSnackbar(it.message)
        }
    }


    Scaffold(
        topBar = {
            TopBarChat(
                navigateUp = navigateUp,
                grupo = viewState.grupo,
                navigateTocreateSala = navigateToCreateSala,
                navigateToGroup = navigateToGroup,
                users = viewState.usersGrupo
            )
        },
        modifier = Modifier
            .fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        floatingActionButton = {
            AnimatedVisibility(visible = isLastItemVisible,
                enter = scaleIn(),
                exit = scaleOut()) {
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            lazyListState.scrollToItem(0)
                        }
                    }, colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardDoubleArrowDown,
                        contentDescription = "arrow_down"
                    )
                }
            }
        },
        bottomBar = {
            ChatInput(
                authState = viewState.authState,
                replyMessage = replyMessage.value,
                clearReplyMessage = {replyMessage.value = null},
                updateMessage = { message.value =it},
                message = message.value,
                clearFocus = { focusManager.clearFocus() },
                focusRequester = focusRequester,
                openAuthBottomSheet = openAuthBottomSheet,
                sendMessage = {sendMessage(it) {
                    coroutineScope.launch {
                        delay(300)
                        lazyListState.animateScrollToItem(0)
                    }
                }
                },
            )
        }

    ) { paddingValues ->

        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Chat(
                colors = colors,
                lazyPagingItems = lazyPagingItems,
                user = viewState.user,
                formatterRelatimeTime = formatterRelativeTime,
                setReply = {
                    coroutineScope.launch {
                    launch{ replyMessage.value =it }
                    }
                },
                lazyListState = lazyListState,
                getUserProfileGrupo = getUserProfileGrupo,
                formatShortDate = formatShortDate,
                navigateToInstalacionReserva = navigateToInstalacionReserva,
                formatShortTime = formatShortTime,
            )
        }
    }
}

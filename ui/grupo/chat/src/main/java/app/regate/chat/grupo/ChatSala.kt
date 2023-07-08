package app.regate.chat.grupo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardDoubleArrowDown
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.zIndex
import androidx.lifecycle.SavedStateHandle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import app.regate.common.composes.LocalAppDateFormatter
import app.regate.common.composes.util.Layout
import app.regate.common.composes.util.appendErrorOrNull
import app.regate.common.composes.util.prependErrorOrNull
import app.regate.common.composes.util.refreshErrorOrNull
import app.regate.common.composes.viewModel
import app.regate.compoundmodels.MessageProfile
import app.regate.compoundmodels.UserProfileGrupo
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

typealias ChatSala   = @Composable (
    navigateUp:()->Unit,
    openAuthBottomSheet:()->Unit,
    navigateToCreateSala:(id:Long)->Unit,
    navigateToGroup:(id:Long)->Unit
        ) -> Unit

@Inject
@Composable
fun ChatSala    (
    viewModelFactory:(SavedStateHandle)-> ChatSalaViewModel,
    @Assisted navigateUp: () -> Unit,
    @Assisted  openAuthBottomSheet:()->Unit,
    @Assisted navigateToCreateSala: (id: Long) -> Unit,
    @Assisted navigateToGroup: (id: Long) -> Unit
){
    ChatSala(
        viewModel = viewModel(factory = viewModelFactory),
        navigateUp = navigateUp,
        openAuthBottomSheet = openAuthBottomSheet,
        navigateToCreateSala = navigateToCreateSala,
        navigateToGroup = navigateToGroup
    )
}

@Composable
internal fun ChatSala   (
    viewModel: ChatSalaViewModel,
    navigateUp: () -> Unit,
    openAuthBottomSheet: () -> Unit,
    navigateToCreateSala: (id: Long) -> Unit,
    navigateToGroup: (id: Long) -> Unit
){
    val viewState by viewModel.state.collectAsState()

    val formatter = LocalAppDateFormatter.current
    ChatSala (
        viewState = viewState,
        lazyPagingItems = viewModel.pagedList.collectAsLazyPagingItems(),
        navigateUp = navigateUp,
        sendMessage = viewModel::sendMessage,
        openAuthBottomSheet = openAuthBottomSheet,
        formatterRelativeTime = formatter::formatShortRelativeTime,
        clearMessage = viewModel::clearMessage,
        navigateToCreateSala = navigateToCreateSala,
        navigateToGroup = navigateToGroup,
        getUserProfileGrupo = viewModel::getUserGrupo
//        formatShortTime = {formatter.formatShortTime(it.toInstant())},
//        formatDate = {formatter.formatWithSkeleton(it.toInstant().toEpochMilliseconds(),formatter.monthDaySkeleton)}
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
internal fun ChatSala   (
    viewState: ChatSalaState,
    lazyPagingItems: LazyPagingItems<MessageProfile>,
    navigateUp: () -> Unit,
    sendMessage:(Message)->Unit,
    openAuthBottomSheet: () -> Unit,
    formatterRelativeTime:(date:Instant)->String,
    clearMessage:(id:Long)->Unit,
    navigateToCreateSala: (id: Long) -> Unit,
    navigateToGroup: (id: Long) -> Unit,
    getUserProfileGrupo:(id:Long)->UserProfileGrupo?
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
        mutableStateOf<MessageProfile?>(null)
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
                updateReplyMessage = {replyMessage.value = it},
                updateMessage = { message.value = it},
                message = message.value,
                clearFocus = { focusManager.clearFocus() },
                focusRequester = focusRequester,
                openAuthBottomSheet = openAuthBottomSheet,
                sendMessage = {sendMessage(it)}
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
                    focusRequester.requestFocus()
                    replyMessage.value = it
                },
                lazyListState = lazyListState,
                getUserProfileGrupo = getUserProfileGrupo
            )
        }
    }
}

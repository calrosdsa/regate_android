package app.regate.chat.sala

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardDoubleArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import app.regate.common.composes.LocalAppDateFormatter
import app.regate.common.composes.LocalAppUtil
import app.regate.common.composes.component.chat.Chat
import app.regate.common.composes.component.input.ChatInput
import app.regate.common.composes.component.input.Keyboard
import app.regate.common.composes.component.input.emoji.EmojiLayout
import app.regate.common.composes.component.input.keyboardAsState
import app.regate.common.composes.util.appendErrorOrNull
import app.regate.common.composes.util.prependErrorOrNull
import app.regate.common.composes.util.refreshErrorOrNull
import app.regate.common.composes.viewModel
import app.regate.compoundmodels.MessageProfile
import app.regate.compoundmodels.UserProfileGrupoAndSala
import app.regate.data.common.MessageData
import app.regate.data.common.ReplyMessageData
import app.regate.data.dto.empresa.grupo.CupoInstalacion
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

typealias ChatSala = @Composable (
    navigateUp:()->Unit,
    openAuthBottomSheet:()->Unit,
    navigateToSala:(id:Long)->Unit,
    navigateToInstalacionReserva:(Long,Long)->Unit,
        ) -> Unit

@Inject
@Composable
fun ChatSala    (
    viewModelFactory:(SavedStateHandle)-> ChatSalaViewModel,
    @Assisted navigateUp: () -> Unit,
    @Assisted  openAuthBottomSheet:()->Unit,
    @Assisted navigateToSala: (id: Long) -> Unit,
    @Assisted navigateToInstalacionReserva:(Long,Long)->Unit,
    ){
    ChatSala(
        viewModel = viewModel(factory = viewModelFactory),
        navigateUp = navigateUp,
        openAuthBottomSheet = openAuthBottomSheet,
        navigateToSala = navigateToSala,
        navigateToInstalacionReserva = navigateToInstalacionReserva
    )
}

@Composable
internal fun ChatSala   (
    viewModel: ChatSalaViewModel,
    navigateUp: () -> Unit,
    openAuthBottomSheet: () -> Unit,
    navigateToSala: (id: Long) -> Unit,
    navigateToInstalacionReserva:(Long,Long)->Unit,
){
    val viewState by viewModel.state.collectAsState()

    val formatter = LocalAppDateFormatter.current
    ChatSala (
        viewState = viewState,
        lazyPagingItems = viewModel.pagedList.collectAsLazyPagingItems(),
        navigateUp = navigateUp,
        sendMessage = viewModel::sendMessage,
        openAuthBottomSheet = openAuthBottomSheet,
        clearMessage = viewModel::clearMessage,
        getUserProfileGrupoAndSala = viewModel::getUserGrupo,
        formatterRelativeTime = formatter::formatShortRelativeTime,
        formatShortDate = {
            formatter.formatWithSkeleton(it.toEpochMilliseconds(),formatter.monthDaySkeleton)
        },
        formatShortTime = formatter::formatShortTime,
        formatShortDateFromString = formatter::formatShortDate,
        formatShortTimeFromString = {time,minutes->
            formatter.formatShortTime(time,minutes.toLong())
        },
        navigateToSala = navigateToSala,
        navigateToInstalacionReserva = {instalacionId,establecimientoId,cupos->
            viewModel.navigateToInstalacionReserva(instalacionId,establecimientoId,cupos,navigateToInstalacionReserva)
        },
        resetScroll = viewModel::resetScroll,
        getKeyboardHeight = viewModel::getKeyBoardHeight,
        setKeyboardHeight = viewModel::setKeyboardHeight,

    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
internal fun ChatSala   (
    viewState: ChatSalaState,
    lazyPagingItems: LazyPagingItems<MessageProfile>,
    navigateUp: () -> Unit,
    sendMessage:(MessageData,()->Unit)->Unit,
    openAuthBottomSheet: () -> Unit,
    clearMessage:(id:Long)->Unit,
    getUserProfileGrupoAndSala:(id:Long)-> UserProfileGrupoAndSala?,
    formatterRelativeTime:(date:Instant)->String,
    formatShortDate:(Instant)->String,
    formatShortTime:(Instant)->String,
    formatShortDateFromString: (String) -> String,
    formatShortTimeFromString: (String,Int) -> String,
    getKeyboardHeight:()->Int,
    setKeyboardHeight:(Int)->Unit,
    navigateToInstalacionReserva: (Long, Long,List<CupoInstalacion>) -> Unit,
    navigateToSala: (Long) -> Unit,
    resetScroll:()->Unit,
) {
    val appUtil = LocalAppUtil.current
    val clipboardManager = LocalClipboardManager.current
    val colors = listOf(
        MaterialTheme.colorScheme.inverseOnSurface,
        MaterialTheme.colorScheme.inverseOnSurface
    )
    val message = remember {
        mutableStateOf(TextFieldValue())
    }
    val coroutineScope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
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
    val context = LocalContext.current as Activity
    var bottomImePadding by remember {
        mutableStateOf(0)
    }
    val bottomImePaddingDp = with(LocalDensity.current){
        bottomImePadding.toDp()
    }
    var openBottomLayout by remember{
        mutableStateOf(false)
    }

    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    LaunchedEffect(key1 = isFocused) {
        if (isFocused) {
            bottomImePadding = getKeyboardHeight()
            keyboardController?.hide()
            keyboardController?.show()
        }
    }
    val isKeyboardOpen by keyboardAsState()
//    val density = LocalDensity.current
    LaunchedEffect(key1 = isKeyboardOpen, block = {
        if(isKeyboardOpen == Keyboard.Opened){
            appUtil.updateKeboardHeight(context){
                setKeyboardHeight(it)
            }
            openBottomLayout = false
        }else{
            if(!openBottomLayout){
                focusManager.clearFocus()
                bottomImePadding = 0
            }
        }
    })



    DisposableEffect(key1 = viewState.scrollToBottom, effect = {
        coroutineScope.launch {
            delay(500)
            if(viewState.scrollToBottom == null) return@launch
            Log.d("DEBUG_APP_SCROLL","SCROLLING")
            lazyListState.animateScrollToItem(0)
        }
        onDispose {
            resetScroll()
        }
    })

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

    BackHandler(enabled = bottomImePadding > 0) {
        Log.d("DEBUG_APP_BACK","trigger")
        keyboardController?.hide()
        bottomImePadding = 0
        openBottomLayout = false
        focusManager.clearFocus()
    }


    Scaffold(
        topBar = {
            Surface(
                color = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Row(modifier =Modifier.fillMaxWidth().padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navigateUp() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back",
                        tint = MaterialTheme.colorScheme.onPrimary)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    viewState.chat?.let {chat->
                        Text(text = chat.name,style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.clickable {
                                navigateToSala(chat.parent_id)
                            })
                    }
                }
            }
        },
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
            Column {
                ChatInput(
                    authState = viewState.authState,
                    replyMessage = replyMessage.value,
                    clearReplyMessage = { replyMessage.value = null },
                    updateMessage = { message.value = it },
                    message = message.value,
                    clearFocus = { focusManager.clearFocus() },
                    focusRequester = focusRequester,
                    openAuthBottomSheet = openAuthBottomSheet,
                    sendMessage = {
                        sendMessage(it) {
                            coroutineScope.launch {
                                delay(300)
//                        lazyListState.animateScrollToItem(0)
                            }
                        }
                    },
                    openBottomLayout = {
                        if (openBottomLayout) {
                            keyboardController?.show()
                            focusRequester.requestFocus()
                        } else {
                            coroutineScope.launch {
                                launch { keyboardController?.hide() }
                                launch {
                                    delay(200)
                                    bottomImePadding = getKeyboardHeight()
                                }
                            }
                        }
                        openBottomLayout = !openBottomLayout
                    },
                    isBottomLayoutOpen = openBottomLayout,
                    interactionSource = interactionSource
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(bottomImePaddingDp)
//                        .windowInsetsBottomHeight(WindowInsets.ime)
                ) {
                    if (openBottomLayout) {
                        EmojiLayout(
                            emojis = viewState.emojiData,
                            updateMessage = { message.value = it },
                            message = message.value,
                            coroutineScope=coroutineScope,
                        )
                    }
                }
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Chat(
                colors = colors,
                lazyPagingItems = lazyPagingItems,
                user = viewState.user,
                setReply = {
                    coroutineScope.launch {
                        launch{ replyMessage.value =it }
                    }
                },
                formatterRelatimeTime = formatterRelativeTime,
                formatShortDate = formatShortDate,
                formatShortTime = formatShortTime,
                formatShortTimeFromString = formatShortTimeFromString,
                formatShortDateFromString = formatShortDateFromString,
                navigateToInstalacionReserva = navigateToInstalacionReserva,
                getUserProfileGrupoAndSala = getUserProfileGrupoAndSala,
                lazyListState = lazyListState,
                navigateToSala = {navigateToSala(it.toLong())},
                copyMessage = {text:String,isLink:Boolean->
                    clipboardManager.setText(AnnotatedString(text))
                    if(isLink){
                        Toast.makeText(context,"Enlace copiado", Toast.LENGTH_SHORT).show()
                    }
                },
                openLink = {url->
                    try{
                        appUtil.openInBrowser(context,url)
                    }catch(e:Exception){
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("No se pudo abrir el enlace.")
                        }
                        Log.d("DEBUG_APP",e.localizedMessage?:"")
                    }
                }
            )
        }

    }
}

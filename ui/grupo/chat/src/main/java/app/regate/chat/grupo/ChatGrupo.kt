package app.regate.chat.grupo

import android.app.Activity
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardDoubleArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.intl.Locale
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.SavedStateHandle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import app.regate.common.compose.LocalAppDateFormatter
import app.regate.common.compose.components.input.ChatInput
import app.regate.common.compose.components.input.Keyboard
import app.regate.common.compose.components.input.emoji.EmojiLayout
import app.regate.common.compose.components.input.keyboardAsState
import app.regate.common.compose.util.appendErrorOrNull
import app.regate.common.compose.util.prependErrorOrNull
import app.regate.common.compose.util.refreshErrorOrNull
import app.regate.common.compose.viewModel
import app.regate.compoundmodels.MessageProfile
import app.regate.compoundmodels.UserProfileGrupo
import app.regate.data.app.Emoji
import app.regate.data.app.EmojisState
import app.regate.data.common.MessageData
import app.regate.data.common.ReplyMessageData
import app.regate.data.dto.empresa.grupo.CupoInstalacion
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject


typealias ChatGrupo  = @Composable (
    navigateUp:()->Unit,
    openAuthBottomSheet:()->Unit,
    navigateToCreateSala:(id:Long)->Unit,
    navigateToGroup:(id:Long)->Unit,
    navigateToInstalacionReserva:(Long,Long)->Unit,
    navigateToSala:(Long)->Unit
        ) -> Unit

@Inject
@Composable
fun ChatGrupo(
    viewModelFactory:(SavedStateHandle)-> ChatGrupoViewModel,
    @Assisted navigateUp: () -> Unit,
    @Assisted  openAuthBottomSheet:()->Unit,
    @Assisted navigateToCreateSala: (id: Long) -> Unit,
    @Assisted navigateToGroup: (id: Long) -> Unit,
    @Assisted navigateToInstalacionReserva:(Long,Long)->Unit,
    @Assisted navigateToSala: (Long) -> Unit
){
    ChatGrupo(
        viewModel = viewModel(factory = viewModelFactory),
        navigateUp = navigateUp,
        openAuthBottomSheet = openAuthBottomSheet,
        navigateToCreateSala = navigateToCreateSala,
        navigateToGroup = navigateToGroup,
        navigateToInstalacionReserva = navigateToInstalacionReserva,
        navigateToSala = navigateToSala
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
    navigateToSala: (Long) -> Unit
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
        formatShortDateFromString = formatter::formatShortDate,
        formatShortTimeFromString = {time,minutes->
        formatter.formatShortTime(time,minutes.toLong())
    },
        navigateToInstalacionReserva = {instalacionId,establecimientoId,cupos->
            viewModel.navigateToInstalacionReserva(instalacionId,establecimientoId,cupos,navigateToInstalacionReserva)
        },
        resetScroll = viewModel::resetScroll,
        getKeyboardHeight = viewModel::getKeyBoardHeight,
        setKeyboardHeight = viewModel::setKeyboardHeight,
        navigateToSala = navigateToSala
//        formatShortTime = {formatter.formatShortTime(it.toInstant())},
//        formatDate = {formatter.formatWithSkeleton(it.toInstant().toEpochMilliseconds(),formatter.monthDaySkeleton)}
    )
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalLayoutApi::class,
    ExperimentalComposeUiApi::class
)
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
    formatShortDateFromString: (String) -> String,
    formatShortTimeFromString: (String,Int) -> String,
    getKeyboardHeight:()->Int,
    setKeyboardHeight:(Int)->Unit,
    navigateToInstalacionReserva: (Long, Long,List<CupoInstalacion>) -> Unit,
    navigateToSala: (Long) -> Unit,
    resetScroll:()->Unit,
) {
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

    val emojis = remember {
        mutableStateOf(EmojisState())
    }
    LaunchedEffect(key1 = Unit, block = {
//        focusRequester.requestFocus()
//        keyboardController?.hide()
        try {
            val decoder = Json {
                ignoreUnknownKeys = true
            }
            val emoticonosString: String =
                context.assets.open("emojis/${Locale.current.language}/smileys-emotion.json")
                    .bufferedReader().use {
                        it.readText()
                    }
            val emoticonos = decoder.decodeFromString<List<Emoji>>(emoticonosString)
            val peopleString: String =
                context.assets.open("emojis/${Locale.current.language}/people-body.json")
                    .bufferedReader().use {
                        it.readText()
                    }
            val people = decoder.decodeFromString<List<Emoji>>(peopleString)
            val animalsAndNatureString: String =
                context.assets.open("emojis/${Locale.current.language}/animals-nature.json")
                    .bufferedReader().use {
                        it.readText()
                    }
            val animalsAndNature = decoder.decodeFromString<List<Emoji>>(animalsAndNatureString)
            val activitiesString: String =
                context.assets.open("emojis/${Locale.current.language}/activities.json")
                    .bufferedReader().use {
                        it.readText()
                    }
            val activities = decoder.decodeFromString<List<Emoji>>(activitiesString)
            val objectsString: String =
                context.assets.open("emojis/${Locale.current.language}/objects.json")
                    .bufferedReader().use {
                        it.readText()
                    }
            val objects = decoder.decodeFromString<List<Emoji>>(objectsString)
            val flagsString: String =
                context.assets.open("emojis/${Locale.current.language}/flags.json")
                    .bufferedReader().use {
                        it.readText()
                    }
            val flags = decoder.decodeFromString<List<Emoji>>(flagsString)
            val symbolsString: String =
                context.assets.open("emojis/${Locale.current.language}/symbols.json")
                    .bufferedReader().use {
                        it.readText()
                    }
            val symbols = decoder.decodeFromString<List<Emoji>>(symbolsString)
            val travelAndPlacesString: String =
                context.assets.open("emojis/${Locale.current.language}/travel-places.json")
                    .bufferedReader().use {
                        it.readText()
                    }
            val travelAndPlaces = decoder.decodeFromString<List<Emoji>>(travelAndPlacesString)

            val foodAndDringString: String =
                context.assets.open("emojis/${Locale.current.language}/food-drink.json")
                    .bufferedReader().use {
                        it.readText()
                    }
            val foodAndDrink = decoder.decodeFromString<List<Emoji>>(foodAndDringString)
            emojis.value = EmojisState(
                emoticonos = emoticonos,
                people = people,
                animals_nature = animalsAndNature,
                activities = activities,
                objects = objects,
                flags = flags,
                symbols = symbols,
                travel_places = travelAndPlaces,
                food_drink = foodAndDrink
            )
            Log.d("DEBUG_APP", emoticonos[0].emoji)
        } catch (e: Exception) {
            Log.d("DEBUG_APP", e.localizedMessage ?: "")
        }
    })

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
            updateKeboardHeight(context){
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
            TopBarChat(
                navigateUp = navigateUp,
                grupo = viewState.grupo,
                navigateTocreateSala = navigateToCreateSala,
                navigateToGroup = navigateToGroup,
                users = viewState.usersGrupo
            )
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
                        } else {
                            coroutineScope.launch {
                                launch { keyboardController?.hide() }
                                launch {
                                    delay(200)
                                    bottomImePadding = 800
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
                            emojis = emojis.value,
                            updateMessage = { message.value = it },
                            message = message.value
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
                formatShortTimeFromString = formatShortTimeFromString,
                formatShortDateFromString = formatShortDateFromString,
                navigateToSala = {navigateToSala(it.toLong())}
            )


        }
    }
}

fun updateKeboardHeight(
    activity: Activity,
    setBottomLayoutHeight:(Int)->Unit
){
    val insets = ViewCompat.getRootWindowInsets(activity.window.decorView)
    val keyboardHeight =
        insets?.getInsets(WindowInsetsCompat.Type.ime())?.bottom
    val bottomBarHeight = insets?.getInsets(WindowInsetsCompat.Type.navigationBars())?.bottom
    Log.d("DEBUG_APP_INSET",keyboardHeight.toString())
    Log.d("DEBUG_APP_INSET",bottomBarHeight.toString())

    setBottomLayoutHeight(keyboardHeight!! - bottomBarHeight!!)
}
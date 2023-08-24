package app.regate.conversation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.SavedStateHandle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import app.regate.common.composes.LocalAppDateFormatter
import app.regate.common.composes.components.input.ChatInput
import app.regate.common.composes.ui.SimpleTopBar
import app.regate.common.composes.viewModel
import app.regate.inbox.ConversationsState
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import app.regate.common.resources.R
import app.regate.compoundmodels.MessageConversation
import app.regate.data.auth.AppAuthState
import app.regate.data.common.MessageData
import app.regate.data.common.ReplyMessageData
import kotlinx.datetime.Instant

typealias Conversation= @Composable (
    navigateUp:()->Unit
//    navigateToReserva:(id:Long)->Unit,
//    navigateToSignUpScreen:() -> Unit,
) -> Unit

@Inject
@Composable
fun Conversation (
    viewModelFactory:(SavedStateHandle)-> ConversationViewModel,
    @Assisted navigateUp: () -> Unit,
//    @Assisted navigateToReserva: (id:Long) -> Unit,
//    viewModelFactory:()->ReservasViewModel
){
    Conversation(
        viewModel = viewModel(factory = viewModelFactory),
        navigateUp = navigateUp
    )


}

@Composable
internal fun Conversation(
    viewModel: ConversationViewModel,
    navigateUp: () -> Unit
){
    val state by viewModel.state.collectAsState()
    val formatter = LocalAppDateFormatter.current
    Conversation(
        viewState = state,
        navigateUp = navigateUp,
        sendMessage = viewModel::sendMessage,
        lazyPagingItems = viewModel.pagedList.collectAsLazyPagingItems(),
        formatterRelativeTime = formatter::formatShortRelativeTime,
        formatShortDate = {
            formatter.formatWithSkeleton(it.toEpochMilliseconds(),formatter.monthDaySkeleton)
        }
        )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Conversation(
    viewState: ConversationsState,
    lazyPagingItems: LazyPagingItems<MessageConversation>,
    formatterRelativeTime:(date: Instant)->String,
    formatShortDate:(Instant)->String,
    navigateUp: () -> Unit,
    sendMessage:(MessageData)->Unit,
    ){
    val message = remember {
        mutableStateOf("")
    }
    val lazyListState = rememberLazyListState()
//    val coroutineScope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val replyMessage = remember {
        mutableStateOf<ReplyMessageData?>(null)
    }
    Scaffold(
        topBar = {
            SimpleTopBar(navigateUp =  navigateUp, title = stringResource(id = R.string.inbox))
//            Row() {
//                IconButton(onClick = { navigateUp() }) {
//                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
//                }
//            }
        },
        bottomBar = {
            ChatInput(
                replyMessage = replyMessage.value,
                clearFocus = { focusManager.clearFocus() },
                clearReplyMessage = { replyMessage.value = null },
                message = message.value,
                updateMessage = {message.value = it},
                focusRequester = focusRequester,
                authState = AppAuthState.LOGGED_IN,
                openAuthBottomSheet = { /*TODO*/ },
                sendMessage = {sendMessage(it)}
            )
        }
    ) {paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)){
            Chat(
                lazyPagingItems = lazyPagingItems,
                setReply = {
                    focusRequester.requestFocus()
                    replyMessage.value =it
                },
                formatShortDate = formatShortDate,
                formatterRelatimeTime = formatterRelativeTime,
                lazyListState = lazyListState,
                user = viewState.user
            )
        }
    }
}
package app.regate.conversation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import app.regate.common.compose.LocalAppDateFormatter
import app.regate.common.compose.components.input.ChatInput
import app.regate.common.compose.ui.PosterCardImage
import app.regate.common.compose.viewModel
import app.regate.inbox.ConversationsState
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import app.regate.compoundmodels.MessageConversation
import app.regate.data.auth.AppAuthState
import app.regate.data.common.MessageData
import app.regate.data.common.ReplyMessageData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
        sendMessage = {it1,it2 ->
            viewModel.sendMessage(it1,it2) },
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
    sendMessage:(MessageData,()->Unit)->Unit,
    ){
    val message = remember {
        mutableStateOf(TextFieldValue())
    }
    val colors = listOf(
        MaterialTheme.colorScheme.inverseOnSurface,
        MaterialTheme.colorScheme.inverseOnSurface
    )
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val replyMessage = remember {
        mutableStateOf<ReplyMessageData?>(null)
    }
    Scaffold(
        topBar = {
//            SimpleTopBar(navigateUp =  navigateUp, title = stringResource(id = R.string.inbox))
//            Surface(contentColor = MaterialTheme.colorScheme.onPrimary,
//            color = MaterialTheme.colorScheme.primary) {
            Column {
              Row(modifier = Modifier
                  .fillMaxWidth()
                  .padding(5.dp)
                  ,verticalAlignment = Alignment.CenterVertically) {
                  IconButton(onClick = { navigateUp() }) {
                      Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                  }
                  Spacer(modifier = Modifier.width(5.dp))
                  viewState.establecimiento?.let {
                  PosterCardImage(model = it.photo,modifier = Modifier.size(35.dp),
                  shape = CircleShape)
                      Spacer(modifier = Modifier.width(10.dp))
                      Text(text = it.name,style = MaterialTheme.typography.titleMedium)
                  }
              }
                Divider()
            }
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
                sendMessage = {sendMessage(it){
                    coroutineScope.launch {
                        delay(300)
                        lazyListState.animateScrollToItem(0)
                    }
                } }
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
                colors = colors,
                formatShortDate = formatShortDate,
                formatterRelatimeTime = formatterRelativeTime,
                lazyListState = lazyListState,
                user = viewState.user
            )
        }
    }
}
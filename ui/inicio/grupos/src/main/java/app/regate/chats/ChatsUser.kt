package app.regate.chats

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import app.regate.common.composes.component.chat.DeleteMessage
import app.regate.common.composes.component.dialog.DialogConfirmation
import app.regate.common.composes.ui.PosterCardImage
import app.regate.common.composes.util.itemsCustom
import app.regate.data.auth.AppAuthState
import app.regate.data.dto.chat.TypeChat
import app.regate.models.chat.Chat
import com.dokar.sheets.rememberBottomSheetState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant



@Composable
internal fun ChatsUser(
    lazyPagingItems: LazyPagingItems<Chat>,
    coroutineScope: CoroutineScope,
    viewState:ChatsState,
    selectChat:(Chat)->Unit,
    deleteChat:()->Unit,
    formatShortRelativeTime:(Instant)->String,
    navigateToChat: (id: Long,grupoId:Long,typeChat:Int) -> Unit,
    navigateToParent:(Long,Int)->Unit,
){
    val sheetState = rememberBottomSheetState()
    var deleteDialogConfirmation by remember {
        mutableStateOf(false)
    }

//    LaunchedEffect(key1 = viewState.authState, block = {
//        if(viewState.authState == AppAuthState.LOGGED_IN){
//            lazyPagingItems.refresh();
//        }
//    })

    LazyColumn(modifier = Modifier.fillMaxSize()){
        itemsCustom(items = lazyPagingItems, key = {it.id}){item->
            if(item!= null){
                ChatItem(chat = item, navigateToChat = navigateToChat,
                    formatShortRelativeTime = formatShortRelativeTime,
                    selectChat = {
                        coroutineScope.launch {
                            selectChat(it)
                            sheetState.expand()
                        }
                    },
                )
            }
        }
    }

    if(viewState.selectedChat != null){
        ChatSelectDialog(
            state = sheetState,
            deleteChat = {deleteDialogConfirmation = true},
            chat = viewState.selectedChat,
            close = {coroutineScope.launch {
                sheetState.collapse()
            }},
           navigateToParent = navigateToParent
        )
    }

    DialogConfirmation(open = deleteDialogConfirmation,
        dismiss = { deleteDialogConfirmation = false },
        confirm = { deleteChat()})

}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
internal fun ChatItem(
//    grupo: GrupoWithMessage,
    chat: Chat,
    selectChat:(Chat)->Unit,
    navigateToChat: (id: Long,grupoId:Long,typeChat:Int) -> Unit,
//    navigateToEstablecimientoInbox: (Long, Long) -> Unit,
    formatShortRelativeTime:(Instant)->String,
    modifier:Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onLongClick = {
                    selectChat(chat)
                }
            ) {
                navigateToChat(chat.id, chat.parent_id, chat.type_chat)
            }
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PosterCardImage(
            model = chat.photo, modifier = Modifier
                .size(60.dp), shape = CircleShape
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column() {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = chat.name, style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.fillMaxWidth(0.65f), overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                if (chat.last_message_created != null) {
                    Text(
                        text = formatShortRelativeTime(chat.last_message_created!!),
                        style = MaterialTheme.typography.labelSmall, maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

            }
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                if (!chat.is_message_deleted) {

                    if (chat.last_message?.isNotBlank() == true) {
                        chat.last_message?.let {
                            Text(
                                text = it, style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Normal
                                ), overflow = TextOverflow.Ellipsis, maxLines = 1,
                                modifier = Modifier.fillMaxWidth(0.88f)
                            )
                        }
                    }
                } else {
                    DeleteMessage()
                }
                if (chat.messages_count > 0) {
                    Badge(
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Text(
                            text = (chat.messages_count).toString(),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }

    }
}

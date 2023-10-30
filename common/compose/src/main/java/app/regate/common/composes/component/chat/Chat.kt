package app.regate.common.composes.component.chat

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Reply
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.LazyPagingItems
import app.regate.common.composes.component.images.ProfileImage
import app.regate.common.composes.component.input.MessengerIcon2
import app.regate.common.composes.util.Layout
import app.regate.common.composes.util.itemsCustom
import app.regate.compoundmodels.MessageProfile
import app.regate.compoundmodels.UserProfileGrupoAndSala
import app.regate.models.account.User
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import app.regate.common.resources.R
import app.regate.data.common.ReplyMessageData
import app.regate.data.dto.chat.TypeChat
import app.regate.data.dto.empresa.grupo.CupoInstalacion
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import java.util.regex.Pattern


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Chat (
    lazyPagingItems: LazyPagingItems<MessageProfile>,
//    colors: List<Color>,
    lazyListState:LazyListState,
    copyMessage:(m:String)->Unit,
    selectMessage:(MessageProfile)->Unit,
    setReply:(message:ReplyMessageData?)->Unit,
    formatShortDate:(Instant)->String,
    formatShortTime:(Instant)->String,
    formatShortDateFromString: (String) -> String,
    formatShortTimeFromString: (String,Int) -> String,
    formatterRelatimeTime:(date:Instant)->String,
    navigateToInstalacionReserva:(Long,Long,List<CupoInstalacion>)->Unit,
    openLink:(String)->Unit,
    navigateToSala: (Int) -> Unit,
    getTypeOfChat:()->TypeChat,
    modifier: Modifier = Modifier,
    user: User? = null,
    getUserProfileGrupoAndSala: (id:Long)->UserProfileGrupoAndSala? = {null},
) {
    val typeOfChat = remember {
        getTypeOfChat()
    }

    val coroutineScope = rememberCoroutineScope()

    val items by remember(lazyPagingItems.itemSnapshotList.items) {
        mutableStateOf(lazyPagingItems.itemSnapshotList.items)
    }

    val selectedMessage = remember {
        mutableStateOf<Long>(0)
    }

//    LaunchedEffect(key1 = lazyPagingItems.itemSnapshotList.items, block = {
//        lazyListState.animateScrollToItem(0)
//    })
    fun checkIsLast(date:LocalDate,item:MessageProfile):Boolean{
        return try{
            val isLast =  items
//                .sortedBy { it.first }
                .map {
                    it
                }
                .last { it.message.created_at.toLocalDateTime(TimeZone.UTC).date == date }
            isLast.message.id == item.message.id
        }catch(e:Exception){
            false
        }
    }


    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        reverseLayout = true,
        state = lazyListState
    ) {
        itemsCustom(items = lazyPagingItems,key={it.message.id}) { result->
                result?.let { item ->
                    val isUserExists = if(typeOfChat == TypeChat.TYPE_CHAT_INBOX_ESTABLECIMIENTO) false else user != null && item.profile?.id == user.profile_id
//                    if (isUserExists || item.message.is_user) {
                        SwipeableActionsBox(
                            startActions = listOf(SwipeAction(
                                icon = rememberVectorPainter(image = Icons.Default.Reply),
                                background = Color.Transparent,
                                onSwipe = {
                                    setReply(null)
                                    setReply(ReplyMessageData(
                                        nombre = item.profile?.nombre?:"",
                                        apellido = item.profile?.apellido,
                                        content = item.message.content,
                                        id = item.message.id,
                                        type_message = item.message.type_message,
                                        data = item.message.data
                                    ))
                                }
                            )),
                            swipeThreshold = 100.dp,
                            backgroundUntilSwipeThreshold = Color.Transparent,
                            modifier = Modifier
                                .padding(horizontal = Layout.bodyMargin)
                                .fillMaxWidth(),
                        ) {
                                MessageComponent(
                                    item = item,
                                    scrollToItem = {
                                        coroutineScope.launch {
                                            try {

                                                items.forEachIndexed { index, messageProfile ->
                                                    if (messageProfile.message.id == item.message.reply_to) {
                                                        lazyListState.scrollToItem(index)
                                                        selectedMessage.value =
                                                            messageProfile.message.id
                                                        return@launch
                                                    }
                                                }
                                            } catch (e: Exception) {
                                                Log.d(
                                                    "DEBUG_LIST",
                                                    e.localizedMessage ?: ""
                                                )
                                            }
                                        }
                                    },
                                    selectMessage = selectMessage,
                                    formatterRelatimeTime = formatterRelatimeTime,
                                    formatShortDate = formatShortDate,
                                    formatShortTime = formatShortTime,
                                    formatShortTimeFromString = formatShortTimeFromString,
                                    formatShortDateFromString = formatShortDateFromString,
                                    navigateToInstalacionReserva = navigateToInstalacionReserva,
                                    getUserProfileGrupoAndSala = getUserProfileGrupoAndSala,
                                    openLink = openLink,
                                    copyMessage = copyMessage,
                                    typeOfChat = typeOfChat,
                                    navigateToSala = navigateToSala,
                                    isUserExist = isUserExists,
                                    selectedMessage = selectedMessage.value,
                                )
                        }
//                    } else {
//                        SwipeableActionsBox(
//                            startActions = listOf(SwipeAction(
//                                icon = rememberVectorPainter(image = Icons.Default.Reply),
//                                background = Color.Transparent,
//                                onSwipe = {
//                                    setReply(null)
//                                    setReply(ReplyMessageData(
//                                        nombre = item.profile?.nombre?:"",
//                                        apellido = item.profile?.apellido,
//                                        content = item.message.content,
//                                        id = item.message.id,
//                                        type_message = item.message.type_message,
//                                        data = item.message.data
//                                    ))
//                                }
//                            )),
//                            swipeThreshold = 100.dp,
//                            backgroundUntilSwipeThreshold = Color.Transparent,
//                            modifier = Modifier
//                                .padding(horizontal = Layout.bodyMargin)
//                                .fillMaxWidth(),
//                        ) {
//                            MessageComponent(
//                                item = item,
//                                scrollToItem = {
//                                    coroutineScope.launch {
//                                        try {
//
//                                            items.forEachIndexed { index, messageProfile ->
//                                                if (messageProfile.message.id == item.message.reply_to) {
//                                                    lazyListState.scrollToItem(index)
//                                                    selectedMessage.value =
//                                                        messageProfile.message.id
//                                                    return@launch
//                                                }
//                                            }
//                                        } catch (e: Exception) {
//                                            Log.d(
//                                                "DEBUG_LIST",
//                                                e.localizedMessage ?: ""
//                                            )
//                                        }
//                                    }
//                                },
//                                selectMessage = selectMessage,
//                                formatterRelatimeTime = formatterRelatimeTime,
//                                formatShortDate = formatShortDate,
//                                formatShortTime = formatShortTime,
//                                formatShortTimeFromString = formatShortTimeFromString,
//                                formatShortDateFromString = formatShortDateFromString,
//                                navigateToInstalacionReserva = navigateToInstalacionReserva,
//                                getUserProfileGrupoAndSala = getUserProfileGrupoAndSala,
//                                openLink = openLink,
//                                copyMessage = copyMessage,
//                                typeOfChat = typeOfChat,
//                                navigateToSala = navigateToSala,
//                                isUserExist = isUserExists,
//                                selectedMessage = selectedMessage.value
//                            )
//                        }
//
//                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    if (checkIsLast(
                            item.message.created_at.toLocalDateTime(TimeZone.UTC).date,
                            item
                        )
                    ) {
                        Box(modifier = Modifier.fillMaxWidth()) {
                        Card(
                            modifier = Modifier
                                .padding(top = 10.dp, bottom = 1.dp)
                                .align(Alignment.Center)
                        ) {
                            Text(
                                text = formatShortDate(item.message.created_at),
                                modifier = Modifier.padding(horizontal = 15.dp, vertical = 5.dp),
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
internal fun Message(
    message:String,
    openLink:(String)->Unit,
    copyMessage: (m: String) -> Unit,
){
    if(message.isNotBlank()){

    val list = message.split(" ").toList()
//    val annotatedString = buildAnnotatedString {
    FlowRow() {
        list.map {word->
            val matcher = urlPattern.matcher(word)
            if(matcher.find()){
                Text(text = "$word ",style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,modifier = Modifier.combinedClickable(
                        onLongClick = { copyMessage(word) }
                ) {
                    openLink(word)
                    })
//                pushStringAnnotation(tag = "URL", annotation = word)
//                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
//                    append("$word ")
//                }
//                pop()
            }else{
//                append("$word ")
                Text(text = "$word ",style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
    }
    }
//    ClickableText(text = annotatedString, style = MaterialTheme.typography.bodyMedium, onClick = { offset ->
//        annotatedString.getStringAnnotations(tag = "URL", start = offset, end = offset).firstOrNull()?.let {
//            openLink(it.item)
//        }
//    },
//    )



private val urlPattern: Pattern = Pattern.compile(
    "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
            + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
            + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
    Pattern.CASE_INSENSITIVE or Pattern.MULTILINE or Pattern.DOTALL
)


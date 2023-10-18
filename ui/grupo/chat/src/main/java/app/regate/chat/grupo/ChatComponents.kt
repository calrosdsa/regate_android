package app.regate.chat.grupo

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Reply
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.paging.compose.LazyPagingItems
import app.regate.common.composes.component.images.ProfileImage
import app.regate.common.composes.component.input.MessengerIcon
import app.regate.common.composes.component.input.MessengerIcon2
import app.regate.common.composes.ui.PosterCardImage
import app.regate.common.composes.util.Layout
import app.regate.common.composes.util.itemsCustom
import app.regate.compoundmodels.MessageProfile
import app.regate.compoundmodels.UserProfileGrupo
import app.regate.models.User
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import app.regate.common.resources.R
import app.regate.data.common.ReplyMessageData
import app.regate.data.dto.empresa.grupo.CupoInstalacion
import app.regate.data.dto.empresa.grupo.MessageInstalacionPayload
import app.regate.data.dto.empresa.grupo.GrupoMessageType
import app.regate.data.dto.empresa.grupo.MessageSalaPayload
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import java.util.regex.Pattern
import kotlin.time.Duration.Companion.minutes


@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun Chat (
    lazyPagingItems: LazyPagingItems<MessageProfile>,
    colors: List<Color>,
    lazyListState:LazyListState,
    copyMessage:(m:String,isLink:Boolean)->Unit,
    setReply:(message:ReplyMessageData?)->Unit,
    formatShortDate:(Instant)->String,
    formatShortTime:(Instant)->String,
    formatShortDateFromString: (String) -> String,
    formatShortTimeFromString: (String,Int) -> String,
    formatterRelatimeTime:(date:Instant)->String,
    navigateToInstalacionReserva:(Long,Long,List<CupoInstalacion>)->Unit,
    openLink:(String)->Unit,
    navigateToSala: (Int) -> Unit,
    modifier: Modifier = Modifier,
    user:User? = null,
    getUserProfileGrupo: (id:Long)->UserProfileGrupo?,
) {

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
                    val isUserExists = user != null && item.profile?.id == user.profile_id
                    if (isUserExists) {
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
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(if (selectedMessage.value == item.message.id) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface)
                            ) {
                                Spacer(modifier = Modifier.fillMaxWidth(0.25f))


                                Column(
                                    modifier = Modifier
                                        .clip(
                                            RoundedCornerShape(
                                                topStart = 15.dp,
                                                bottomStart = 15.dp,
                                                bottomEnd = 15.dp
                                            )
                                        )
                                        .combinedClickable(
                                            onLongClick = {
                                                copyMessage(item.message.content,false)
                                            }
                                        ) {  }
                                        .fillMaxWidth(0.95f)
                                        .background(MaterialTheme.colorScheme.inverseOnSurface)
                                        .padding(
                                            start = 10.dp,
                                            end = 10.dp,
                                            top = 5.dp,
                                            bottom = 5.dp
                                        )
                                ) {
                                    if (item.message.reply_to != null) {
                                        MessageReply(
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

////                                                    lazyPagingItems.itemCount
//                                                    lazyListState.scrollToItem( lazyPagingItems.itemCount)

                                                }
                                            }, getUserProfileGrupo = getUserProfileGrupo,
                                            navigateToInstalacionReserva = navigateToInstalacionReserva,
                                            formatShortDate = formatShortDate,
                                            formatShortTime = formatShortTime,
                                            formatShortTimeFromString = formatShortTimeFromString,
                                            formatShortDateFromString = formatShortDateFromString,
                                            navigateToSala = navigateToSala
                                        )
                                    }
                                    item.message.data?.let {data->
                                    MessageContent1(
                                        content = data,
                                        messageType = item.message.type_message,
                                        navigateToInstalacionReserva = navigateToInstalacionReserva,
                                        formatShortDate = formatShortDate,
                                        formatShortTime = formatShortTime,
                                        formatShortTimeFromString = formatShortTimeFromString,
                                        formatShortDateFromString = formatShortDateFromString,
                                        navigateToSala = navigateToSala
                                    )
                                    }
                                    Message(
                                        message = item.message.content,
                                        openLink = openLink,
                                        copyMessage = copyMessage
                                    )
//                                    if(item.message.content.isBlank()) {
//                                        Text(
//                                            text = item.message.content,
//                                            style = MaterialTheme.typography.bodySmall,
//                                        )
//                                    }
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = formatterRelatimeTime(item.message.created_at),
                                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp)
                                        )
                                        if (item.message.sended) {
                                            Image(
                                                painter = painterResource(id = R.drawable.doble_check),
                                                contentDescription = "double_check",
                                                modifier = Modifier.size(14.dp),
                                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                                            )
                                        } else {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = "check",
                                                modifier = Modifier.size(14.dp),
//                                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                                            )
                                        }
                                    }
                                }
//                                MessengerIcon(colors)
                            }
                        }


                    } else {
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
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(if (selectedMessage.value == item.message.id) 1f else 0.8f)
                                    .background(if (selectedMessage.value == item.message.id) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface)
                            ) {
                                ProfileImage(
                                    profileImage = item.profile?.profile_photo,
                                    contentDescription = item.profile?.nombre ?: "",
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .size(30.dp)
                                )
                                MessengerIcon2(colors)
                                Column(
                                    modifier = Modifier
                                        .clickable {
                                            copyMessage(item.message.content,false)
                                        }
                                        .clip(
                                            RoundedCornerShape(
                                                topEnd = 15.dp,
                                                bottomStart = 15.dp,
                                                bottomEnd = 15.dp
                                            )
                                        )
                                        .background(MaterialTheme.colorScheme.inverseOnSurface)
                                        .padding(
                                            start = 10.dp,
                                            end = 10.dp,
                                            top = 5.dp,
                                            bottom = 5.dp
                                        )
                                ) {
                                    Text(
                                        text = "${item.profile?.nombre ?: ""} ${item.profile?.apellido ?: ""}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    if (item.message.reply_to != null) {
                                        MessageReply(item = item, scrollToItem = {
                                            coroutineScope.launch {
                                                items.forEachIndexed { index, messageProfile ->

                                                    if (messageProfile.message.id == item.message.reply_to) {
                                                        lazyListState.scrollToItem(index)
                                                        selectedMessage.value =
                                                            messageProfile.message.id

                                                        return@forEachIndexed
                                                    }
                                                }
                                            }
                                        }, getUserProfileGrupo = getUserProfileGrupo,
                                            navigateToInstalacionReserva = navigateToInstalacionReserva,
                                            formatShortDate = formatShortDate,
                                            formatShortTime = formatShortTime,
                                            formatShortTimeFromString = formatShortTimeFromString,
                                            formatShortDateFromString = formatShortDateFromString,
                                            navigateToSala = navigateToSala
                                        )
                                    }
                                    item.message.data?.let {data->
                                        MessageContent1(
                                            content = data,
                                            messageType = item.message.type_message,
                                            navigateToInstalacionReserva = navigateToInstalacionReserva,
                                            formatShortDate = formatShortDate,
                                            formatShortTime = formatShortTime,
                                            formatShortTimeFromString = formatShortTimeFromString,
                                            formatShortDateFromString = formatShortDateFromString,
                                            navigateToSala = navigateToSala
                                        )
                                    }
//                                    Text(
//                                        text = item.message.content,
//                                        style = MaterialTheme.typography.bodySmall,
//                                    )
                                    Message(
                                        message = item.message.content,
                                        openLink = openLink,
                                        copyMessage = copyMessage
                                    )
                                    Text(
                                        text = formatterRelatimeTime(item.message.created_at),
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp)
                                    )
                                }
                            }
                        }

                    }
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
    copyMessage: (m: String, isLink: Boolean) -> Unit,
){
    val list = message.split(" ").toList()
//    val annotatedString = buildAnnotatedString {
    FlowRow() {
        list.map {word->
            val matcher = urlPattern.matcher(word)
            if(matcher.find()){
                Text(text = "$word ",style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,modifier = Modifier.combinedClickable(
                        onLongClick = { copyMessage(word,true) }
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


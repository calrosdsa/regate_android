package app.regate.chat.grupo

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Reply
import androidx.compose.material3.Card
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.LazyPagingItems
import app.regate.common.composes.components.images.ProfileImage
import app.regate.common.composes.components.input.MessengerIcon
import app.regate.common.composes.components.input.MessengerIcon2
import app.regate.common.composes.ui.PosterCardImage
import app.regate.common.composes.util.Layout
import app.regate.common.composes.util.itemsCustom
import app.regate.common.composes.util.itemsCustomIndexed
import app.regate.compoundmodels.MessageProfile
import app.regate.compoundmodels.UserProfileGrupo
import app.regate.models.User
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import app.regate.common.resources.R
import app.regate.data.common.ReplyMessageData
import app.regate.data.dto.empresa.grupo.CupoInstalacion
import app.regate.data.dto.empresa.grupo.GrupoMessageInstalacion
import app.regate.data.dto.empresa.grupo.GrupoMessageType
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import kotlin.time.Duration.Companion.minutes


@Composable
internal fun Chat (
    lazyPagingItems: LazyPagingItems<MessageProfile>,
    colors: List<Color>,
    setReply:(message:ReplyMessageData?)->Unit,
    formatShortDate:(Instant)->String,
    formatShortTime:(Instant)->String,
    formatterRelatimeTime:(date:Instant)->String,
    navigateToInstalacionReserva:(Long,Long,List<CupoInstalacion>)->Unit,
    lazyListState:LazyListState,
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
                                        .fillMaxWidth(0.95f)
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
                                        )
                                    }
                                    item.message.data?.let {data->
                                    MessageContent1(
                                        content = data,
                                        messageType = item.message.type_message,
                                        navigateToInstalacionReserva = navigateToInstalacionReserva,
                                        formatShortDate = formatShortDate,
                                        formatShortTime = formatShortTime,
                                    )
                                    }
//                                    if(item.message.content.isBlank()) {
                                        Text(
                                            text = item.message.content,
                                            style = MaterialTheme.typography.bodySmall,
                                        )
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
                                MessengerIcon(colors)
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
                                            formatShortTime = formatShortTime,)
                                    }
                                    item.message.data?.let {data->
                                        MessageContent1(
                                            content = data,
                                            messageType = item.message.type_message,
                                            navigateToInstalacionReserva = navigateToInstalacionReserva,
                                            formatShortDate = formatShortDate,
                                            formatShortTime = formatShortTime,
                                        )
                                    }
                                    Text(
                                        text = item.message.content,
                                        style = MaterialTheme.typography.bodySmall,
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

@Composable
internal fun MessageContent1(
    messageType:Int,
    content:String,
    navigateToInstalacionReserva: (Long, Long,List<CupoInstalacion>) -> Unit,
    formatShortDate: (Instant) -> String,
    formatShortTime: (Instant) -> String,
    modifier: Modifier = Modifier
){
    when(messageType){
        GrupoMessageType.MESSAGE.ordinal ->{
            Text(
                text = content,
                style = MaterialTheme.typography.bodySmall,
                modifier = modifier
            )
        }
        GrupoMessageType.INSTALACION.ordinal ->{
            val instalacion = try{ Json.decodeFromString<GrupoMessageInstalacion>(content) }catch (e:Exception){
                Log.d("DEBUG_APP",e.localizedMessage?:"")
                null
            }
            if(instalacion != null){
                Column(modifier = Modifier
                    .padding(vertical = 5.dp)
                    .clickable {
                    navigateToInstalacionReserva(instalacion.id.toLong(),instalacion.establecimiento_id.toLong(),instalacion.cupos)
                }) {
                    PosterCardImage(model = instalacion.photo,modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp))
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(text = instalacion.name,style = MaterialTheme.typography.labelLarge)
                    Text(text = "${stringResource(id = R.string.total_price)}: ${instalacion.total_price}",
                        style = MaterialTheme.typography.labelMedium)
                    Text(
                        text = stringResource(id = R.string.time_game_will_played),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = formatShortDate(instalacion.cupos.first().time) +
                                " ${formatShortTime(instalacion.cupos.first().time)} a ${
                                    formatShortTime(
                                        instalacion.cupos.last().time.plus(30.minutes)
                                    )
                                }",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}

@Composable
fun MessageReply(
    item:MessageProfile,
    scrollToItem:()->Unit,
    getUserProfileGrupo: (id:Long)->UserProfileGrupo?,
    navigateToInstalacionReserva: (Long, Long,List<CupoInstalacion>) -> Unit,
    formatShortDate: (Instant) -> String,
    formatShortTime: (Instant) -> String,
    modifier:Modifier = Modifier
){
    val profile = item.reply?.let { getUserProfileGrupo(it.profile_id) }
    Surface(
        border = BorderStroke(1.dp, Color.LightGray),
        shape = MaterialTheme.shapes.medium,
        onClick = {
            scrollToItem()

        }
    ) {

    Column(modifier = modifier
        .fillMaxWidth()
        .padding(10.dp)
    ) {
        Text(
            text = "${profile?.nombre?:""} ${profile?.apellido ?: ""}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary
        )
        item.reply?.data?.let { data->
        MessageContent1(
            content = data,
            messageType = item.reply?.type_message?:0,
            navigateToInstalacionReserva = navigateToInstalacionReserva,
            formatShortDate = formatShortDate,
            formatShortTime = formatShortTime,
        )
        }
        Text(
            text = item.message.content,
            style = MaterialTheme.typography.bodySmall,
        )
    }
    }
}

package app.regate.chat.grupo

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Reply
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.LazyPagingItems
import app.regate.common.composes.components.images.ProfileImage
import app.regate.common.composes.util.Layout
import app.regate.common.composes.util.itemsCustom
import app.regate.common.composes.util.itemsCustomIndexed
import app.regate.compoundmodels.MessageProfile
import app.regate.compoundmodels.UserProfileGrupo
import app.regate.models.User
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import app.regate.common.resources.R
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox


@Composable
fun Chat(
    lazyPagingItems: LazyPagingItems<MessageProfile>,
    colors: List<Color>,
    setReply:(message:MessageProfile?)->Unit,
    formatterRelatimeTime:(date:Instant)->String,
    lazyListState:LazyListState,
    modifier: Modifier = Modifier,
    user:User? = null,
    getUserProfileGrupo: (id:Long)->UserProfileGrupo?
) {

    val coroutineScope = rememberCoroutineScope()

    val items by remember(lazyPagingItems.itemSnapshotList.items) {
        val messages = lazyPagingItems.itemSnapshotList.items.groupBy {
            it.message.created_at.toLocalDateTime(TimeZone.UTC).date
        }
        mutableStateOf(lazyPagingItems.itemSnapshotList.items)
    }
    val selectedMessage = remember {
        mutableStateOf<Long>(0)
    }

    LaunchedEffect(key1 = lazyPagingItems.itemSnapshotList.items, block = {
//        Log.d("DEBUG_APP","UPDATE___ ")
        lazyListState.animateScrollToItem(0)
    })


    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        reverseLayout = true,
        state = lazyListState
    ) {
        itemsCustomIndexed(items = lazyPagingItems,key={it.message.id}) {  result,index->
            result?.let { item ->
                val isUserExists = user != null && item.profile?.id == user.profile_id
                if (isUserExists) {
                    SwipeableActionsBox(
                        startActions = listOf(SwipeAction(
                            icon = rememberVectorPainter(image = Icons.Default.Reply),
                            background = Color.Transparent,
                            onSwipe = {
                                setReply(null)
                                setReply(item)
                            }
                        )),
                        swipeThreshold= 100.dp,
                        backgroundUntilSwipeThreshold = Color.Transparent,
                        modifier = Modifier
                            .padding(horizontal = Layout.bodyMargin)
                            .fillMaxWidth(),
                    )  {
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .background(if (selectedMessage.value == item.message.id) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface)) {
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
                                            }, getUserProfileGrupo = getUserProfileGrupo
                                        )
                                    }

                                    Text(
                                        text = item.message.content + "$index",
                                        style = MaterialTheme.typography.bodySmall,
                                    )
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
                                setReply(item)
                            }
                        )),
                        swipeThreshold= 100.dp,
                        backgroundUntilSwipeThreshold = Color.Transparent,
                        modifier = Modifier
                            .padding(horizontal = Layout.bodyMargin)
                            .fillMaxWidth(),
                    ) {
                            Row(modifier = Modifier
                                .fillMaxWidth(if (selectedMessage.value == item.message.id) 1f else 0.8f)
                                .background(if (selectedMessage.value == item.message.id) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface)
                            ) {
                                ProfileImage(
                                    profileImage = item.profile?.profile_photo,
                                    contentDescription = item.profile?.nombre?:"",
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
                                        text = "${item.profile?.nombre?:""} ${item.profile?.apellido ?: ""}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    if (item.message.reply_to != null) {
                                                MessageReply(item = item, scrollToItem = {
                                                    coroutineScope.launch {
                                                        items.forEachIndexed { index, messageProfile ->

                                                            if(messageProfile.message.id == item.message.reply_to){
                                                                lazyListState.scrollToItem(index)
                                                                selectedMessage.value = messageProfile.message.id

                                                                return@forEachIndexed
                                                            }
                                                        }
                                                    }
                                                },getUserProfileGrupo = getUserProfileGrupo)
                                    }
                                    Text(
                                        text = item.message.content,
                                        style = MaterialTheme.typography.bodySmall
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
            }
        }

    }
}

@Composable
fun MessageReply(
    item:MessageProfile,
    scrollToItem:()->Unit,
    getUserProfileGrupo: (id:Long)->UserProfileGrupo?,
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
        Text(
            text = item.reply?.content?:"",
            style = MaterialTheme.typography.bodySmall
        )
    }
    }
}



@Composable
fun MessengerIcon2(
    colors: List<Color>,
    modifier: Modifier=Modifier,
) {
    Canvas(
        modifier = modifier
            .width(9.dp)
            .height(12.dp)
//            .height(20.dp)
    ) {
//
        val trianglePath = Path().let {
            it.moveTo(this.size.width * 1f, this.size.height * 0f)
            it.cubicTo(
                size.width.times(0f),
                size.height.times(0f),
                size.width.times(-0.1f),
                size.height.times(0f),
                size.width.times(1f),
                size.height.times(1f),
            )
            it.close()
            it
        }
        drawPath(
            path = trianglePath,
            Brush.verticalGradient(colors = colors),
        )
    }
}
@Composable
fun MessengerIcon(
    colors:List<Color>,
    modifier: Modifier = Modifier,
) {
    Canvas(
        modifier = modifier
            .width(9.dp)
            .height(12.dp)
//            .height(20.dp)
    ) {
//
        val trianglePath = Path().let {
            it.cubicTo(   size.width.times(1f),
                this.size.height.times(0f),
                size.width.times(1f),
                size.height.times(0f),
                size.width.times(0f),
                size.height.times(1f),
            )
            it.close()
            it
        }



        drawPath(
            path = trianglePath,
            Brush.verticalGradient(colors = colors),
        )
    }
}




//fun <T : Any> LazyListScope.itemsCustom(
//    items: LazyPagingItems<T>,
//    key: ((item: T) -> Any)? = null,
//    contentType: (item: T) -> Any? = { null },
//    itemContent: @Composable LazyItemScope.(item: T?) -> Unit,
//) {
//    items(
//        count = items.itemCount,
//        contentType = { index ->
//            items.peek(index)?.let { contentType(it) }
//        },
//        key = if (key == null) {
//            null
//        } else {
//            { index ->
//                val item = items.peek(index)
//                if (item == null) {
//                    PagingPlaceholderKey(index)
//                } else {
//                    key(item)
//                }
//            }
//        },
//    ) { index ->
//        itemContent(items[index])
//    }
//}
//
//@SuppressLint("BanParcelableUsage")
//internal data class PagingPlaceholderKey(private val index: Int) : Parcelable {
//    override fun writeToParcel(parcel: Parcel, flags: Int) = parcel.writeInt(index)
//    override fun describeContents(): Int = 0
//
//    companion object {
//        @Suppress("unused")
//        @JvmField
//        val CREATOR: Parcelable.Creator<PagingPlaceholderKey> =
//            object : Parcelable.Creator<PagingPlaceholderKey> {
//                override fun createFromParcel(parcel: Parcel) =
//                    PagingPlaceholderKey(parcel.readInt())
//
//                override fun newArray(size: Int) = arrayOfNulls<PagingPlaceholderKey?>(size)
//            }
//    }
//}

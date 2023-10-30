package app.regate.common.composes.component.chat

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.regate.common.composes.component.images.ProfileImage
import app.regate.common.resources.R
import app.regate.compoundmodels.MessageProfile
import app.regate.compoundmodels.UserProfileGrupoAndSala
import app.regate.data.dto.chat.TypeChat
import app.regate.data.dto.empresa.grupo.CupoInstalacion
import kotlinx.datetime.Instant

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessageComponent(
    item:MessageProfile,
    typeOfChat:TypeChat,
    isUserExist:Boolean,
    selectedMessage:Long,
    scrollToItem:()->Unit,
    selectMessage:(MessageProfile)->Unit,
    formatShortDate:(Instant)->String,
    formatShortTime:(Instant)->String,
    formatShortDateFromString: (String) -> String,
    formatShortTimeFromString: (String,Int) -> String,
    formatterRelatimeTime:(date: Instant)->String,
    navigateToInstalacionReserva:(Long,Long,List<CupoInstalacion>)->Unit,
    navigateToSala: (Int) -> Unit,
    openLink:(String)->Unit,
    copyMessage:(m:String)->Unit,
    modifier:Modifier = Modifier,
    getUserProfileGrupoAndSala: (id:Long)-> UserProfileGrupoAndSala? = {null},

    ){
    Row(
        modifier = Modifier
            .fillMaxWidth(if (isUserExist || item.message.is_user) 1F else 0.75f)
            .background(if (selectedMessage == item.message.id) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface)
    ) {
        if(!isUserExist){
            if(typeOfChat != TypeChat.TYPE_CHAT_INBOX_ESTABLECIMIENTO){
                ProfileImage(
                    profileImage = item.profile?.profile_photo,
                    contentDescription = item.profile?.nombre ?: "",
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(30.dp)
                        .padding(end = 5.dp)
                )
            }
        }
        if(isUserExist || item.message.is_user) {
            Spacer(modifier = Modifier.fillMaxWidth(0.25f))
        }

        Column(
            modifier = modifier
                .clip(
                    RoundedCornerShape(
                        topStart = if (isUserExist) 15.dp else 0.dp,
                        bottomStart = 15.dp,
                        bottomEnd = 15.dp,
                        topEnd = if (isUserExist) 0.dp else 15.dp
                    )
                )
                .combinedClickable(
                    onLongClick = {
                        selectMessage(item)
                    }
                ) { }
                .fillMaxWidth(1f)
                .background(MaterialTheme.colorScheme.inverseOnSurface)
                .padding(
                    start = 10.dp,
                    end = 10.dp,
                    top = 5.dp,
                    bottom = 5.dp
                )
        ) {
                if(!isUserExist && typeOfChat != TypeChat.TYPE_CHAT_INBOX_ESTABLECIMIENTO){
                    Text(text = "${item.profile?.nombre} ${item.profile?.apellido}",
                    style = MaterialTheme.typography.labelMedium,color = MaterialTheme.colorScheme.primary,
                    maxLines = 1)
                }
            if(item.message.is_deleted){
              DeleteMessage()
            }else {

                    if (item.message.reply_to != null) {
                    MessageReply(
                        item = item,
                        scrollToItem = scrollToItem,
                        getUserProfileGrupoAndSala = getUserProfileGrupoAndSala,
                        navigateToInstalacionReserva = navigateToInstalacionReserva,
                        formatShortDate = formatShortDate,
                        formatShortTime = formatShortTime,
                        formatShortTimeFromString = formatShortTimeFromString,
                        formatShortDateFromString = formatShortDateFromString,
                        navigateToSala = navigateToSala,
                        typeOfChat = typeOfChat
                    )
                }
                item.message.data?.let { data ->
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
            }
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
                if(!item.message.is_deleted){

                if (isUserExist || item.message.is_user) {
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
            }
        }
    }
}


@Composable
fun DeleteMessage(){
    Text(text = stringResource(id = R.string.message_deleted), fontStyle = FontStyle.Italic,
        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Normal))
}
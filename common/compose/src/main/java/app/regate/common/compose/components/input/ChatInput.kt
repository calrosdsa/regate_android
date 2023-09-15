package app.regate.common.compose.components.input


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import app.regate.common.compose.LocalAppDateFormatter
import app.regate.common.compose.ui.PosterCardImage
import app.regate.common.resources.R
import app.regate.data.auth.AppAuthState
import app.regate.data.common.MessageData
import app.regate.data.common.ReplyMessageData
import app.regate.data.dto.empresa.grupo.GrupoMessageInstalacion
import app.regate.data.dto.empresa.grupo.GrupoMessageType
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.minutes

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ChatInput(
    replyMessage:ReplyMessageData?,
    clearFocus:()->Unit,
    clearReplyMessage:() ->Unit,
    message:String,
    updateMessage:(String)->Unit,
    focusRequester: FocusRequester,
    authState:AppAuthState?,
    openAuthBottomSheet:()->Unit,
    sendMessage:(MessageData)->Unit,
    modifier:Modifier = Modifier,
){

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Surface(
            modifier = Modifier.padding(5.dp),
            shape = MaterialTheme.shapes.large,
            border = BorderStroke(1.dp, Color.LightGray)
        ) {
            Column {
//                if(sharedMessage.isNotBlank()){
//                    SharedMessage(data = sharedMessage,
//                    clearSharedMessage = clearSharedMessage)
//                }
                AnimatedVisibility(visible = replyMessage != null,
                    enter = scaleIn(),
                    exit = scaleOut()
                ) {
                    replyMessage?.let { item ->
                        Box( modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(0.85f)
                            .border(BorderStroke(1.dp, Color.LightGray))
                            .clip(MaterialTheme.shapes.medium)
                            .padding(10.dp)){
                            Icon(imageVector = Icons.Outlined.Close, contentDescription = "cancel",
                                modifier = Modifier
                                    .size(20.dp)
                                    .clickable {
                                        clearFocus()
                                        clearReplyMessage()
                                    }
                                    .align(Alignment.TopEnd))
                            Column(
                            ) {
                                if(item.nombre != null){
                                Text(
                                    text = "${item.nombre?:""} ${item.apellido ?: ""}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                }
                                item.data?.let {data->
                                MessageContent2(data = data,
                                    messageType = item.type_message)
                                }

                                Text(
                                    text = item.content,
                                    style = MaterialTheme.typography.bodySmall,
                                )

                            }
                        }
                    }
                }
                BasicTextField(
                    value = message,
                    onValueChange = {
                        updateMessage(it)
                    },
                    modifier = Modifier
//                        .background(Color.White)
                        .fillMaxWidth(0.85f)
                        .focusRequester(focusRequester),
                    maxLines = 3,
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onBackground),
                    decorationBox = { innerTextField ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)) {
//                            ProvideTextStyle(value = ) {
                            innerTextField()
//                            }
                        }
//                            Icon(imageVector = Icons.Outlined.EmojiEmotions, contentDescription = "emoji")
//                        }
                    },
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (authState == AppAuthState.LOGGED_IN) {
                                if (message.isNotEmpty()) {
                                    if (replyMessage != null) {
                                        sendMessage(MessageData(content = message, reply_to = replyMessage.id))
                                        updateMessage("")
                                        clearReplyMessage()
                                    } else {
                                        sendMessage(MessageData(content = message))
                                        updateMessage("")
                                    }
                                }
                            } else {
                                openAuthBottomSheet()
                            }
                        }
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done,
                    ),
                )

            }
        }
        IconButton(onClick = {
            if (authState == AppAuthState.LOGGED_IN) {
                    if (message.isNotEmpty()) {
                        if (replyMessage != null) {
                            sendMessage(MessageData(content = message, reply_to = replyMessage.id))
                            updateMessage("")
                            clearReplyMessage()
                        } else {
                            sendMessage(MessageData(content = message))
                            updateMessage("")
                        }
                    }
            } else {
                openAuthBottomSheet()
            }
        },
            colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary)) {
            Icon(imageVector = Icons.Default.Send, contentDescription = "send")
        }

    }
}

@Composable
internal fun MessageContent2(
    data:String,
    messageType:Int,
//    clearSharedMessage:()->Unit
) {
        when(messageType){
            GrupoMessageType.MESSAGE.ordinal ->{
                Text(
                    text = data,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            GrupoMessageType.INSTALACION.ordinal ->{
                val formatter = LocalAppDateFormatter.current
                val instalacion = try{ Json.decodeFromString<GrupoMessageInstalacion>(data) }catch (e:Exception){ null }
                if(instalacion != null){
                    Column(modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                        .clickable {
//                        navigateToInstalacionReserva(instalacion.id.toLong(),instalacion.establecimiento_id.toLong(),instalacion.cupos)
                        }) {
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)){
//                            IconButton(onClick = { clearSharedMessage() },modifier = Modifier
//                                .align(Alignment.TopEnd)
//                                .zIndex(1f)) {
//                                Icon(
//                                    imageVector = Icons.Outlined.Close,
//                                    contentDescription = null,
//                                    tint = Color.White
//                                )
//                            }
                        PosterCardImage(model = instalacion.photo,
                            modifier = Modifier
                            .fillMaxSize())
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = instalacion.name,style = MaterialTheme.typography.labelLarge)
                        Text(text = "${stringResource(id = R.string.total_price)}: ${instalacion.total_price}",
                            style = MaterialTheme.typography.labelMedium)
                        Text(
                            text = stringResource(id = R.string.time_game_will_played),
                            style = MaterialTheme.typography.labelMedium
                        )
                        Text(
                            text = formatter.formatShortDate(instalacion.cupos.first().time) +
                                    " ${formatter.formatShortTime(instalacion.cupos.first().time)} a ${
                                        formatter.formatShortTime(
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

package app.regate.chats

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Reply
import androidx.compose.material.icons.filled.Shortcut
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.regate.common.compose.BuildConfig
import app.regate.common.composes.LocalAppUtil
import app.regate.common.composes.ui.PosterCardImage
import app.regate.common.resources.R
import app.regate.data.common.ReplyMessageData
import app.regate.data.dto.chat.TypeChat
import app.regate.models.chat.Chat
import com.dokar.sheets.BottomSheetState
import com.dokar.sheets.PeekHeight
import com.dokar.sheets.m3.BottomSheet

@Composable
internal fun ChatSelectDialog(
    state: BottomSheetState,
    close:()->Unit,
    chat:Chat,
    deleteChat:()->Unit,
    navigateToParent:(id:Long,typeChat:Int)->Unit,
    ) {
//    val appUtil = LocalAppUtil.current
//    val context = LocalContext.current
    BottomSheet(
        state = state,
        // PeekHeight.px(Int) and PeekHeight.fraction(Float) are supported as well.
        peekHeight = PeekHeight.dp(300),
        // Set to true you don't want the peeked state.
        skipPeeked = false,
    ) {
        Column {
            Row(modifier = Modifier.padding(horizontal =  20.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically) {
            PosterCardImage(model = chat.photo,
            modifier = Modifier.size(40.dp),
                shape = CircleShape
            )
             Spacer(modifier = Modifier.width(10.dp))
             Text(text = chat.name, style = MaterialTheme.typography.titleMedium)
            }

            DropdownMenuItem(
                text = {
                    when(chat.type_chat){
                        TypeChat.TYPE_CHAT_GRUPO.ordinal -> {
                            Text(
                                text = "Ver grupo",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                        TypeChat.TYPE_CHAT_SALA.ordinal -> {
                            Text(
                                text = "Ver sala",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                        TypeChat.TYPE_CHAT_INBOX_ESTABLECIMIENTO.ordinal -> {
                            Text(
                                text = "Ver establecimiento",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                },
                onClick = {
                    navigateToParent(chat.parent_id,chat.type_chat)
                    close()
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Visibility, contentDescription = null)
                }, contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
            )

            if (chat.is_user_out) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(id = R.string.delete_chat),
                            style = MaterialTheme.typography.labelLarge
                        )
                    },
                    onClick = {
                        deleteChat()
                        close()
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Filled.Delete, contentDescription = null)
                    }, contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
                )
            }


//            DropdownMenuItem(
//                text = {
//                    Text(
//                        text = stringResource(id = R.string.create_shortcut),
//                        style = MaterialTheme.typography.labelLarge
//                    )
//                },
//                onClick = {
//                    if(Build.VERSION_CODES.O < Build.VERSION.SDK_INT){
//                   appUtil.createShortcut(context,"Chat1","")
//                    }
//                    close()
//                },
//                leadingIcon = {
//                    Icon(imageVector = Icons.Filled.Shortcut, contentDescription = null)
//                }, contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
//            )

        }
    }
}
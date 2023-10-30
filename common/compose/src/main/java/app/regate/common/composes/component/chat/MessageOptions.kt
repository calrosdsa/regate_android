package app.regate.common.composes.component.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessibleForward
import androidx.compose.material.icons.filled.ArrowCircleRight
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.Forward
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.Forward30
import androidx.compose.material.icons.filled.Forward5
import androidx.compose.material.icons.filled.ForwardToInbox
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material.icons.filled.PhoneForwarded
import androidx.compose.material.icons.filled.Reply
import androidx.compose.material.icons.filled.ScreenShare
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShareLocation
import androidx.compose.material.icons.filled.SwipeRightAlt
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.regate.common.resources.R
import app.regate.compoundmodels.MessageProfile
import app.regate.data.common.ReplyMessageData
import com.dokar.sheets.BottomSheetState
import com.dokar.sheets.PeekHeight
import com.dokar.sheets.m3.BottomSheet

@Composable
fun MessageOptions(
    state: BottomSheetState,
    message:MessageProfile,
    onDeleteMessage:()->Unit,
    setReply:(message: ReplyMessageData?)->Unit,
    copyMessage:(m:String)->Unit,
    modifier: Modifier = Modifier
){
    BottomSheet(
        state = state,
        modifier = modifier,
        // PeekHeight.px(Int) and PeekHeight.fraction(Float) are supported as well.
        peekHeight = PeekHeight.dp(300),
        // Set to true you don't want the peeked state.
        skipPeeked = false,
    ) {
        Column() {
            DropdownMenuItem(text = { Text(text = stringResource(id = R.string.reply), style = MaterialTheme.typography.labelLarge) },
                onClick = { setReply(ReplyMessageData(
                    nombre = message.profile?.nombre?:"",
                    apellido = message.profile?.apellido,
                    content = message.message.content,
                    id = message.message.id,
                    type_message = message.message.type_message,
                    data = message.message.data
                ))},
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Reply, contentDescription =null )
                }, contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
            )
//            DropdownMenuItem(text = { Text(text = stringResource(id = R.string.reply), style = MaterialTheme.typography.labelLarge) },
//                onClick = { /*TODO*/ },
//                leadingIcon = {
//                    Icon(imageVector = Icons.Filled., contentDescription =null )
//                }, contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
//            )
            DropdownMenuItem(text = { Text(text = stringResource(id = R.string.delete_message), style = MaterialTheme.typography.labelLarge) },
                onClick = { onDeleteMessage() },
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Delete, contentDescription =null )
                }, contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
            )
            DropdownMenuItem(text = { Text(text = stringResource(id = R.string.copy_text), style = MaterialTheme.typography.labelLarge) },
                onClick = { copyMessage(message.message.content) },
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.ContentCopy, contentDescription =null )
                }, contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
            )



        }
    }
}
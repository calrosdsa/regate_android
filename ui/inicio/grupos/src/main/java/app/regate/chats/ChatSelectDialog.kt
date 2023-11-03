package app.regate.chats

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Reply
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.regate.common.resources.R
import app.regate.data.common.ReplyMessageData
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
    ) {
    BottomSheet(
        state = state,
        // PeekHeight.px(Int) and PeekHeight.fraction(Float) are supported as well.
        peekHeight = PeekHeight.dp(300),
        // Set to true you don't want the peeked state.
        skipPeeked = false,
    ) {
        Column() {
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


        }
    }
}
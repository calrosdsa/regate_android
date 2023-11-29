package app.regate.sala

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.regate.common.resources.R

@Composable
internal fun SalaMenu (
    salaTitle:String,
    leaveRoom: () -> Unit,
    shareSalaWithGroup: () -> Unit,
    navigateToChat:()->Unit,
    navigateToComplete: () -> Unit,
    navigateToInfoGrupo:()->Unit,
    imIntheRoom:Boolean,
){
    Column() {
        Text(text = salaTitle, style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(10.dp))
        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.view_group)) },
            onClick = {
                navigateToInfoGrupo()
                 },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Group, contentDescription = null)
            }
        )
        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.send_to_a_chat)) },
            onClick = { shareSalaWithGroup() },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Send, contentDescription = null)
            }
        )
        if(imIntheRoom){


            DropdownMenuItem(
                text = { Text(text = stringResource(id = R.string.room_chat)) },
                onClick = { navigateToChat() },
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Chat, contentDescription = null)
                }
            )
            DropdownMenuItem(
                text = { Text(text = stringResource(id = R.string.amount_contributed)) },
                onClick = { navigateToComplete() },
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.AttachMoney, contentDescription = null)
                }
            )

            DropdownMenuItem(
                text = { Text(stringResource(id = R.string.leave_room)) },
                onClick = { leaveRoom() },
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Logout, contentDescription = null)
                }
            )
        }

    }
}
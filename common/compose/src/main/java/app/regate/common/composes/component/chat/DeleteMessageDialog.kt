package app.regate.common.composes.component.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import app.regate.compoundmodels.MessageProfile
import app.regate.common.resources.R
import app.regate.models.account.User

@Composable
fun DeleteMessageDialog (
    open:Boolean,
    close:()->Unit,
    message:MessageProfile,
    user:User?,
    deleteMessageForEveryone:(Long)->Unit,
    deleteMessageForMe: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (open) {
        Dialog(onDismissRequest = { close() }) {
            Column(modifier = modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.background)
                .padding(15.dp)) {
                Text(text = stringResource(id = R.string.delete_message), fontWeight = FontWeight.SemiBold)
                Divider(modifier = Modifier.padding(vertical = 10.dp))
                Text(text = "Seguro que quieres eliminar este mensaje?")
                Spacer(modifier = Modifier.height(10.dp))
                Column(horizontalAlignment = Alignment.End,
                modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = {
                        deleteMessageForMe(message.message.id)
                        close()
                    }) {
                        Text(text = "Eliminar para mi")
                    }
                    if(!message.message.is_deleted){

                    if(user?.profile_id == message.profile?.id ){
                    TextButton(onClick = {
                        deleteMessageForEveryone(message.message.id)
                        close()
                    }) {
                        Text(text = "Eliminar para todos")
                    }
                    }
                    }
                }
            }
        }
    }
}
package app.regate.chat.grupo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.regate.common.composes.ui.PosterCardImage
import app.regate.compoundmodels.UserProfileGrupoAndSala
import app.regate.models.Grupo
import app.regate.models.chat.Chat


@Composable
fun TopBarChat (
    navigateUp:()->Unit,
    chat:Chat?,
//    navigateTocreateSala:(id:Long)->Unit,
    navigateToGroup:(id:Long)->Unit,
    users:List<UserProfileGrupoAndSala>,
    modifier:Modifier = Modifier
) {
//    var expanded by remember { mutableStateOf(false) }
//    val context = LocalContext.current

    Surface(color = MaterialTheme.colorScheme.primary,
    contentColor = MaterialTheme.colorScheme.onPrimary) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navigateUp() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back_esta")
            }
            PosterCardImage(
                model = chat?.photo, modifier = Modifier.size(40.dp),
                shape = CircleShape,
                onClick = {
                    if (chat != null) {
                        navigateToGroup(chat.id)
                    }
                }
            )
            Spacer(modifier = Modifier.width(5.dp))
            Column(modifier = Modifier.clickable {
                if (chat != null) {
                    navigateToGroup(chat.id)
                }
            }) {
            Text(text = chat?.name?:"", style = MaterialTheme.typography.titleMedium, maxLines = 1)
                if(users.isNotEmpty()){
                Text(text = users.joinToString(separator = " - ") { it.nombre  },
                    maxLines = 1, overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.fillMaxWidth(0.7f)
                )
                }
            }
        }
//        Row() {
//            IconButton(onClick = {
//                val sendIntent = Intent()
//                sendIntent.action = Intent.ACTION_SEND
//                sendIntent.putExtra(
//                    Intent.EXTRA_TEXT,
//                    "Hey check out my app at: http://teclu-portal.s3.sa-east-1.amazonaws.com/servicenter"
//                )
//                sendIntent.type = "text/plain"
//                context.startActivity(sendIntent)
//            }) {
//                Icon(imageVector = Icons.Default.Share, contentDescription = "share")
//            }
//            Box(modifier = Modifier) {
//                IconButton(onClick = { expanded = !expanded }) {
//                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = "dots_chat")
//                }
//                DropdownMenu(
//                    expanded = expanded,
//                    onDismissRequest = { expanded = false },
//                ) {
//                    DropdownMenuItem(
//                        text = { Text(text = stringResource(id = R.string.create_sala)) },
//                        onClick = {
//                            if (grupo != null) {
//                                navigateTocreateSala(grupo.id)
//                            }
//                        }
//                    )
//                    DropdownMenuItem(
//                        text = { Text("Save") },
//                        onClick = {  }
//                    )
//                }
//            }
//
//        }
    }
    }
}

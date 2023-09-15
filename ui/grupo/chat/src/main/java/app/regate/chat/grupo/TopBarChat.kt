package app.regate.chat.grupo

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.regate.common.compose.ui.PosterCardImage
import app.regate.common.resources.R
import app.regate.compoundmodels.UserProfileGrupo
import app.regate.models.Grupo


@Composable
fun TopBarChat (
    navigateUp:()->Unit,
    grupo:Grupo?,
    navigateTocreateSala:(id:Long)->Unit,
    navigateToGroup:(id:Long)->Unit,
    users:List<UserProfileGrupo>,
    modifier:Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

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
                model = grupo?.photo, modifier = Modifier.size(40.dp),
                shape = CircleShape
            )
            Spacer(modifier = Modifier.width(5.dp))
            Column(modifier = Modifier.clickable {
                if (grupo != null) {
                    navigateToGroup(grupo.id)
                }
            }) {
            Text(text = grupo?.name?:"", style = MaterialTheme.typography.titleMedium, maxLines = 1)
                Text(text = users.joinToString(separator = " - ") { it.nombre  },
                    maxLines = 1, overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.fillMaxWidth(0.7f)
                )
            }
        }
        Row() {
            IconButton(onClick = {
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    "Hey check out my app at: http://teclu-portal.s3.sa-east-1.amazonaws.com/servicenter"
                )
                sendIntent.type = "text/plain"
                context.startActivity(sendIntent)
            }) {
                Icon(imageVector = Icons.Default.Share, contentDescription = "share")
            }
            Box(modifier = Modifier) {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = "dots_chat")
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    DropdownMenuItem(
                        text = { Text(text = stringResource(id = R.string.create_sala)) },
                        onClick = {
                            if (grupo != null) {
                                navigateTocreateSala(grupo.id)
                            }
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Save") },
                        onClick = {  }
                    )
                }
            }

        }
    }
    }
}

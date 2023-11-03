package app.regate.grupo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.regate.common.composes.ui.PosterCardImage
import app.regate.common.resources.R
import app.regate.compoundmodels.UserProfileGrupoAndSala
import app.regate.data.common.encodeMediaData
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GrupoMenu(
    grupo: app.regate.models.Grupo,
    isCurrentUserisAdmin:Boolean?,
    isCurrentUserSuperAdmin:Boolean,
    pendingRequestCount:Int,
    currentUser: UserProfileGrupoAndSala?,
    members:Int,
    createSala: () -> Unit,
    editGroup: () -> Unit,
    leaveGroup: () -> Unit,
    navigateToReport: () -> Unit,
    navigateToPendingRequests: () -> Unit,
    navigateToChatGroup: (Long) -> Unit,
    navigateToPhoto: (String) -> Unit,
    navigateToInvitationLink:(Long)->Unit,
){
    Column() {
        Box(){
            PosterCardImage(model = grupo.photo,
                shape = RoundedCornerShape(0.dp),modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                onClick = {
                    val payload = Json.encodeToString(encodeMediaData(listOf(grupo.photo.toString())))
                    navigateToPhoto(payload)
                },
                darkerImage = true
            )
            Column(modifier = Modifier.padding(10.dp).align(Alignment.BottomStart)) {
                Text(text = grupo.name,style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold, color = Color.White)
                Text(text = stringResource(id = R.string.members,members),style= MaterialTheme.typography.labelMedium,
                    color = Color.White)
            }
        }
        if (isCurrentUserisAdmin == true) {
            DropdownMenuItem(
                text = { Text(text = stringResource(id = R.string.create_sala)) },
                onClick = {createSala() },
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = null)
                }
            )
        }
        if (isCurrentUserisAdmin == true) {
            DropdownMenuItem(
                text = { Text(text = stringResource(id = R.string.invitation_link)) },
                onClick = { navigateToInvitationLink(grupo.id)},
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Link, contentDescription = null)
                }
            )
        }
        if (isCurrentUserisAdmin == true) {
            DropdownMenuItem(
                text = { Text(text = stringResource(id = R.string.pending_requests)) },
                onClick = {navigateToPendingRequests() },
                leadingIcon = {
                    BadgedBox(badge ={
                        if(pendingRequestCount > 0 ){
                            Badge{
                                Text(text = pendingRequestCount.toString())
                            }
                        }
                    }) {
                        Icon(imageVector = Icons.Filled.PendingActions, contentDescription = null)
                    }
                }
            )
        }

        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.chat_group)) },
            onClick = { navigateToChatGroup(grupo.id) },
            leadingIcon = {
                Icon(imageVector = Icons.Filled.Chat, contentDescription = null)
            }
        )

        if (isCurrentUserSuperAdmin && currentUser != null) {
            DropdownMenuItem(
                text = { Text("Editar") },
                onClick = { editGroup()},
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Edit, contentDescription = null)
                }
            )
        }
        if (currentUser != null) {
            DropdownMenuItem(
                text = { Text(stringResource(id = R.string.leave_group)) },
                onClick = { leaveGroup() },
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Logout, contentDescription = null)
                }
            )
        }
        DropdownMenuItem(
            text = { Text("Reportar Grupo") },
            onClick = { navigateToReport() },
            leadingIcon = {
                Icon(imageVector = Icons.Filled.Flag, contentDescription = null)
            }
        )

    }
}

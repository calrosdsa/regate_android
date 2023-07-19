package app.regate.common.composes.components.item

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.regate.common.composes.components.images.AsyncImage
import app.regate.common.composes.components.images.DefaultImageUser
import app.regate.common.composes.components.images.ProfileImage
import app.regate.compoundmodels.UserProfileGrupo
import app.regate.data.dto.account.user.ProfileDto
import app.regate.common.resources.R

@Composable
fun ProfileItem(
    id:Long,
    photo:String?,
    nombre:String,
    apellido:String?,
    isCurrentUserAdmin:Boolean,
    modifier:Modifier = Modifier,
    is_admin:Boolean = false,
    isMe:Boolean = false,
    selectUser:()->Unit = {},
    navigateToProfile:(Long)->Unit= {}
) {
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = modifier
                .clickable { navigateToProfile(id) }
                .padding(vertical = 5.dp)
                .fillMaxWidth(0.7f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileImage(
                profileImage = photo,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(40.dp),
                contentDescription = nombre,
            )
            Spacer(modifier = Modifier.width(10.dp))
            if(isMe){
                Text(
                    text = stringResource(id = R.string.you), style = MaterialTheme.typography.labelLarge,
                )

            }else{
                Text(
                    text = "$nombre ${apellido ?: ""}", style = MaterialTheme.typography.labelLarge,
                    maxLines = 1, overflow = TextOverflow.Ellipsis
                )
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (is_admin) {
                Surface(
                    shape = MaterialTheme.shapes.small,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
//            border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = "admin",
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
            if (isCurrentUserAdmin && !isMe) {
                Box() {
                    IconButton(onClick = { selectUser() }) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                    }
                }
            }
        }
    }
}
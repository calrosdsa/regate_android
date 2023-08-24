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
import app.regate.common.composes.components.images.ProfileImage
import app.regate.common.resources.R

@Composable
fun ProfileItem (
    id:Long,
    photo:String?,
    nombre:String,
    apellido:String?,
    modifier:Modifier = Modifier,
    isMe:Boolean = false,
    navigateToProfile:(Long)->Unit= {}
) {
    Row(
        modifier = modifier
            .clickable { navigateToProfile(id) }
            .padding(5.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

            ProfileImage (
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
}
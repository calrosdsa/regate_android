package app.regate.common.composes.components.item

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import app.regate.common.composes.components.images.AsyncImage
import app.regate.common.composes.components.images.DefaultImageUser
import app.regate.common.composes.components.images.ProfileImage
import app.regate.compoundmodels.UserProfileGrupo
import app.regate.data.dto.account.user.ProfileDto

@Composable
fun ProfileItem(
    photo:String?,
    nombre:String,
    apellido:String?,
    modifier:Modifier = Modifier
){
    Row(modifier = modifier.padding(vertical = 5.dp),
    verticalAlignment = Alignment.CenterVertically) {
            ProfileImage(
                profileImage = photo,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(40.dp),
                contentDescription = nombre,
            )
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = "$nombre ${apellido?:""}",style = MaterialTheme.typography.labelLarge)
    }
}
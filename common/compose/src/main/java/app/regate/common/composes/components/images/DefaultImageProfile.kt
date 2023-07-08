package app.regate.common.composes.components.images

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import app.regate.common.resources.R

@Composable
fun DefaultImageUser(
    modifier:Modifier = Modifier,
    contentDescription:String = "profile"
){
    Image(painter = painterResource(id = R.drawable.user_profile),
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}
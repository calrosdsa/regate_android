package app.regate.common.composes.components.images

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale

@Composable
fun ProfileImage(
    modifier:Modifier = Modifier,
    profileImage:String? = null,
    contentDescription:String = "chat_image_profile"
){
    if(profileImage != null ){
        AsyncImage(
            model = profileImage,
            contentDescription = contentDescription,
            requestBuilder = { crossfade(true) },
            modifier = modifier,
            contentScale = ContentScale.Crop,
        )
    }else{
//        Image(painter = painterResource(id = R.drawable.user_profile),
//            contentDescription = contentDescription,
//            modifier = modifier,
//            contentScale = ContentScale.Crop
//        )
        DefaultImageUser(contentDescription = contentDescription,
            modifier = modifier)
    }
}
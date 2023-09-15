package app.regate.common.compose.components.images

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ProfileImage (
    modifier:Modifier = Modifier,
    profileImage:String? = null,
    contentDescription:String = "chat_image_profile",
    shape:Shape = CircleShape
){
    if(profileImage != null ){
        Card(modifier = modifier,
        shape = shape) {
        AsyncImage(
            model = profileImage,
            contentDescription = contentDescription,
            requestBuilder = { crossfade(true) },
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
        }
    }else{
//        Image(painter = painterResource(id = R.drawable.user_profile),
//            contentDescription = contentDescription,
//            modifier = modifier,
//            contentScale = ContentScale.Crop
//        )
        Card(modifier = modifier,
            shape = shape) {
            DefaultImageUser (
                contentDescription = contentDescription,
                modifier =Modifier.fillMaxSize()
            )
        }
    }
}

@Preview
@Composable
fun ProfileImagePreview(){
    ProfileImage()
}
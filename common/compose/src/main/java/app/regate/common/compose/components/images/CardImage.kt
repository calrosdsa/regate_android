package app.regate.common.compose.components.images


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import app.regate.common.resources.R

@Composable
fun CardImage (
    modifier:Modifier = Modifier,
    src:String? = null,
    contentDescription:String = "",
    shape:Shape = MaterialTheme.shapes.medium,
    contentScale:ContentScale = ContentScale.Crop
){
    if(!src.isNullOrBlank()){
        Card(modifier = modifier,
            shape = shape) {
            AsyncImage(
                model = src,
                contentDescription = contentDescription,
                requestBuilder = { crossfade(true) },
                modifier = Modifier.fillMaxSize(),
                contentScale = contentScale,
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
             Image(
                 painter = painterResource(id = R.drawable.default_image),
                contentDescription = contentDescription,
                modifier =Modifier.fillMaxSize()
            )
        }
    }
}

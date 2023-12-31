package app.regate.common.composes.component.images


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import app.regate.common.resources.R

@Composable
fun CardImage (
    modifier:Modifier = Modifier,
    isUser:Boolean = false,
    enabled:Boolean = true,
    src:String? = null,
    colorFilter: ColorFilter? = null,
    contentDescription:String = "",
    shape:Shape = MaterialTheme.shapes.medium,
    contentScale:ContentScale = ContentScale.Crop,
    onClick:()->Unit = {}
){
    if(!src.isNullOrBlank()){
        Surface(
            enabled = enabled,
            modifier = modifier,
            onClick = onClick,
            shape = shape) {
            AsyncImage(
                model = src,
                contentDescription = contentDescription,
                requestBuilder = { crossfade(true) },
                modifier = Modifier.fillMaxSize(),
                contentScale = contentScale,
                colorFilter = colorFilter
            )
        }
    }else{
//        Image(painter = painterResource(id = R.drawable.user_profile),
//            contentDescription = contentDescription,
//            modifier = modifier,
//            contentScale = ContentScale.Crop
//        )
        if(isUser){
            Card(modifier = modifier,
                shape = shape) {
                Image(
                    painter = painterResource(id = R.drawable.user_profile),
                    contentDescription = contentDescription,
                    modifier =Modifier.fillMaxSize(),
                    colorFilter = colorFilter
                )
            }
        }else{
            Card(
                modifier = modifier,
                shape = shape){
             Image(
                 painter = painterResource(id = R.drawable.default_image),
                contentDescription = contentDescription,
                modifier =Modifier.fillMaxSize().padding(10.dp),
                 colorFilter = colorFilter
            )
        }
        }

    }
}

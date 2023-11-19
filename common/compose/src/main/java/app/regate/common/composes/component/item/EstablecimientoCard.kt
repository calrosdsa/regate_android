package app.regate.common.composes.component.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.regate.common.composes.ui.PosterCardImageDark

@Composable
fun EstablecimientoCard(
    name:String,
    id:Long,
    photo:String?,
    onClick:(Long)->Unit,
    modifier:Modifier = Modifier,
){
    Box(modifier = modifier){
        PosterCardImageDark(
            model = photo,
            modifier = Modifier
                .fillMaxSize()
                .clickable { onClick(id) }
        )
        Text(text = name, style = MaterialTheme.typography.labelMedium,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(5.dp), maxLines = 1,color = Color.White)
    }
}
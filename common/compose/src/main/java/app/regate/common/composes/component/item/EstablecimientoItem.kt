package app.regate.common.composes.component.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.regate.common.composes.ui.PosterCardImage

@Composable
fun EstablecimientoItem (
    name:String,
    photo:String?,
    modifier:Modifier = Modifier,
    navigate:()->Unit = {}
){
    Row(modifier = modifier
        .fillMaxWidth()
        .clickable { navigate() }
        .padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
        PosterCardImage(
            model = photo, modifier = Modifier
                .width(100.dp)
                .height(70.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = name, style = MaterialTheme.typography.titleMedium)
    }
}
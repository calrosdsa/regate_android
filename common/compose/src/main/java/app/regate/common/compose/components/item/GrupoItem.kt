package app.regate.common.compose.components.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.regate.common.compose.ui.PosterCardImage

@Composable
fun GrupoItem(
    id:Long,
    photo:String?,
    name:String,
    modifier: Modifier = Modifier,
    navigate:(Long)->Unit,
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { navigate(id) }
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PosterCardImage(
            model = photo, modifier = Modifier
                .size(70.dp), shape = CircleShape
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = name, style = MaterialTheme.typography.titleMedium)
    }
}
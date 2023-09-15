package app.regate.common.compose.components.input

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import app.regate.common.compose.components.images.AsyncImage
import app.regate.models.Labels

@Composable
fun RowCheckSelect(
    selected:Boolean,
    onCheck:()->Unit,
    label:String,
    modifier: Modifier = Modifier
){
    Surface(onClick = {onCheck()}, modifier = modifier) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(text = label,style = MaterialTheme.typography.titleSmall)
            Checkbox(
                checked = selected, onCheckedChange = { onCheck()},
                modifier = Modifier.size(40.dp),
            )
        }
    }
}

@Composable
fun RowCheckSelectWithImage(
    selected:Boolean,
    onCheck:()->Unit,
    label:String,
    src:String?,
    modifier: Modifier = Modifier
){
    Surface(onClick = {onCheck()}, modifier = modifier) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = src,
                    contentDescription = src,
                    modifier = Modifier.size(25.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                )
                Spacer(modifier = Modifier.width(10.dp))
            Text(text = label,style = MaterialTheme.typography.titleSmall)
            }
            Checkbox(
                checked = selected, onCheckedChange = { onCheck()},
                modifier = Modifier.size(40.dp),
            )
        }
    }
}

@Composable
fun AmenityItem(
    amenity:Labels,
    modifier: Modifier=Modifier
) {
    Surface(
        shape = MaterialTheme.shapes.large,
        border = (BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .padding(vertical = 5.dp, horizontal = 8.dp)
        ) {
            AsyncImage(
                model = amenity.thumbnail,
                contentDescription = amenity.name,
                modifier = Modifier.size(16.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = amenity.name, style = MaterialTheme.typography.labelSmall)
        }
    }
}

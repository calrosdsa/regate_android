package app.regate.establecimiento.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.regate.common.compose.components.images.AsyncImage
import app.regate.compoundmodels.InstalacionCategoryCount

@Composable
fun InstalacionCategoryItem(
    item:InstalacionCategoryCount,
    navigateToReserva:(category:Long)->Unit,
    modifier:Modifier = Modifier
) {
    Surface(
        onClick = { item.category_id?.let { navigateToReserva(it.toLong()) } },
        modifier = modifier.padding(8.dp),
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.inverseOnSurface,
        tonalElevation = 10.dp,
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.inverseSurface)
//        contentColor = MaterialTheme.colorScheme.inverseOnSurface

    ) {
        Column(
            modifier = Modifier.padding(vertical = 3.dp, horizontal = 5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = item.thumbnail,
                contentDescription = item.name,
                modifier = Modifier.size(25.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
            )
            Text(
                text = item.name,
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center
            )
            Text(
                text = "${item.count} ${if (item.count > 1) "canchas" else "cancha"}",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.W400
            )
        }
    }
}




@Composable
fun Detail(
    it:String,
    label:String,
    icon:ImageVector,
    intent:()->Unit,
    modifier: Modifier = Modifier
){
    Row(modifier = modifier
        .padding(3.dp)
        .clickable { intent() }
        .fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically) {
    Column {
        Text(text = label,style = MaterialTheme.typography.titleSmall)
        Text(text = it,style = MaterialTheme.typography.labelMedium)
    }
        IconButton(onClick = { intent() }) {
            Icon(imageVector = icon, contentDescription = label)
        }
    }
}

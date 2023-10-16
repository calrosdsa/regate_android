package app.regate.common.composes.component.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.regate.common.composes.ui.PosterCardImage
import app.regate.data.common.AddressDevice
import app.regate.util.roundOffDecimal

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
                .height(70.dp),
            onClick = { navigate() }
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = name, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
fun EstablecimientoItemWithLocation (
    name:String,
    photo:String?,
    isAddressDevice: Boolean,
    modifier:Modifier = Modifier,
    distance:Double? = null,
    navigate:()->Unit = {}
) {
    Row(modifier = modifier
        .fillMaxWidth()
        .clickable { navigate() }
        .padding(10.dp)) {
        PosterCardImage(
            model = photo, modifier = Modifier
                .width(100.dp)
                .height(70.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column() {
            Text(
                text = name, style = MaterialTheme.typography.titleMedium,
                maxLines = 1
            )
            if (isAddressDevice) {
                distance?.div(1000)?.let {
                    roundOffDecimal(it)?.let { distance ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = Icons.Default.Place,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                            )
                            Text(
                                text = "A $distance Km de distancia",
                                style = MaterialTheme.typography.labelSmall,
                            )
                        }
                    }
                }
            }
        }
    }
}
package app.regate.common.composes.components.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.regate.common.composes.components.images.AsyncImage
import app.regate.models.Instalacion

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstalacionCard(
    instalacion:Instalacion,
    navigate: (id: Long) -> Unit,
    modifier: Modifier = Modifier,
    imageHeight:Dp = 110.dp,
    content:@Composable () (ColumnScope.() -> Unit) = {}
) {
    ElevatedCard(
        modifier = modifier,
        onClick = { navigate(instalacion.id) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight)
        ) {
            AsyncImage(
                model = instalacion.portada,
                requestBuilder = { crossfade(true) },
                contentDescription = instalacion.portada,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
            Surface(
                shape = RoundedCornerShape(topEnd = 10.dp),
                border = BorderStroke(1.dp, Color.White),
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .offset(y = 4.dp),
                color = MaterialTheme.colorScheme.primary
            ) {
                Box(modifier= Modifier.padding(vertical = 8.dp, horizontal = 12.dp)) {
                Text(
                    text = instalacion.precio_hora.toString(),
                    style = MaterialTheme.typography.titleMedium
                )
                }
            }
        }
        Column(modifier = Modifier.padding(5.dp)) {

        Text(
            text = instalacion.category_name.toString(),
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier
        )

        Text(
            text = instalacion.name, style = MaterialTheme.typography.titleSmall,
            modifier = Modifier
        )
            Spacer(modifier = Modifier.height(5.dp))
            content()
        }

    }
}


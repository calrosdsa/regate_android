package app.regate.create.sala.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.regate.common.composes.ui.PosterCardImage
import app.regate.compoundmodels.InstalacionCupos
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.minutes

@Composable
internal fun InstalacionSelected(
    instalacionCupos:InstalacionCupos?,
    formatDate:(Instant)->String,
    formatShortTime:(Instant)->String,
    modifier:Modifier = Modifier
){
    instalacionCupos?.let { item ->
        Row(modifier = modifier.padding(10.dp)) {
            PosterCardImage(
                model = item.instalacion.portada,
                modifier = Modifier
                    .width(150.dp)
                    .height(90.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = item.instalacion.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1
                )
                Text(
                    text = "Precio: ${item.cupos.sumOf { it.price }.toInt()}",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = "${formatDate(item.cupos.first().time)} ${formatShortTime(item.cupos.first().time)} a ${
                        formatShortTime(
                            item.cupos.last().time.plus(30.minutes)
                        )
                    }",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}
package app.regate.createsala.pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.regate.common.composes.components.card.InstalacionCard
import app.regate.common.composes.components.dialog.DialogConfirmation
import app.regate.common.composes.ui.PosterCardImage
import app.regate.compoundmodels.InstalacionCupos
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.minutes

@Composable
internal  fun Page2(
    reservarInstalacion:@Composable ()->Unit,
    formatShortTime:(time: Instant)->String,
    formatDate:(date: Instant)->String,
    instalacionCupos: InstalacionCupos? = null,
) {
    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 5.dp)
                .verticalScroll(rememberScrollState()),
//                .padding(bottom = 60.dp),
//            horizontalArrangement = Arrangement.SpaceBetween
        ) {
//            Spacer(modifier = Modifier.height(100.dp))
            reservarInstalacion()
            Divider(modifier = Modifier.padding(vertical = 5.dp))
            instalacionCupos?.let { item ->
//            item.cupos.map { cupo ->
//                Text(text = cupo.time.toString())
//            }
                Row(modifier = Modifier.padding(10.dp)) {
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
//            InstalacionCard(
//                instalacion = item.instalacion.copy(precio_hora = item.cupos.sumOf { it.price }.toInt()),
//                navigate ={},
//                modifier = Modifier
//                    .padding(horizontal = 25.dp)
////                    .height(205.dp)
//                    .clip(MaterialTheme.shapes.large)
//                    .fillMaxWidth(),
//                imageHeight = 150.dp
//            ){
//                Text("Fecha en la que se jugara",style = MaterialTheme.typography.labelMedium)
//                Text("4 Jul de 20:00pm a 22:00pm",style = MaterialTheme.typography.titleSmall)
//            }
            }
        }

    }
}
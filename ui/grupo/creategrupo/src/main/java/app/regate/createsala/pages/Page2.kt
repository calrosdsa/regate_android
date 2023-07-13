package app.regate.createsala.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import app.regate.common.composes.components.card.InstalacionCard
import app.regate.common.composes.components.dialog.DialogConfirmation
import app.regate.compoundmodels.InstalacionCupos

@Composable
internal  fun Page2(
    reservarInstalacion:@Composable ()->Unit,
    instalacionCupos: InstalacionCupos? = null,
){
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
        instalacionCupos?.let {item->
//            item.cupos.map { cupo ->
//                Text(text = cupo.time.toString())
//            }
            InstalacionCard(
                instalacion = item.instalacion.copy(precio_hora = item.cupos.sumOf { it.price }.toInt()),
                navigate ={},
                modifier = Modifier
                    .padding(horizontal = 25.dp)
//                    .height(205.dp)
                    .clip(MaterialTheme.shapes.large)
                    .fillMaxWidth(),
                imageHeight = 150.dp
            ){
                Text("Fecha en la que se jugara",style = MaterialTheme.typography.labelMedium)
                Text("4 Jul de 20:00pm a 22:00pm",style = MaterialTheme.typography.titleSmall)
            }
        }
        }

    }
}
package app.regate.common.composes.components.item

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.regate.data.dto.empresa.salas.SalaDto
import app.regate.data.dto.empresa.salas.SalaEstado
import app.regate.models.SalaEntity
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toInstant
import kotlin.time.Duration.Companion.minutes

@Composable
fun SalaItem(
    sala: SalaDto,
    formatDate:(String)->String,
    formatShortTime:(String,Long)->String,
    navigateToSala:(id:Long)->Unit,
    modifier:Modifier = Modifier
) {
    Surface(modifier = modifier
        .padding(5.dp)
        .height(65.dp),
        onClick = {
            navigateToSala(sala.id)
        }) {
//        LocalDateTime.parse()
        Column(
            modifier = Modifier
                .fillMaxWidth()
//            .background(MaterialTheme.colorScheme.surface)
                .padding(5.dp)
        ) {
            Text(text = sala.titulo, style = MaterialTheme.typography.labelLarge, maxLines = 1)
            Text(
                text = "Se jugara:${formatDate(sala.horas.first())} de ${
                    formatShortTime(
                        sala.horas.first(),
                        0
                    )
                } a ${
                    formatShortTime(sala.horas.last(), 30)
                }", style = MaterialTheme.typography.labelMedium
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${sala.users}/${sala.cupos}",
                        style = MaterialTheme.typography.labelSmall
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Icon(
                        imageVector = Icons.Default.Group,
                        contentDescription = sala.titulo,
                        modifier = Modifier.size(15.dp)
                    )
                }
                when(sala.estado){
                    SalaEstado.UNAVAILABLE.ordinal -> Text(text = "Sala no disponible",color = Color.Red,
                        style = MaterialTheme.typography.labelMedium)
                    SalaEstado.RESERVED.ordinal -> Text(text = "Sala reservada",color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelMedium)
                }
//                Text(text = )
            }
        }
    }
}



@Composable
fun SalaItem(
    sala: SalaEntity,
    formatDate:(String)->String,
    formatShortTime:(String,Long)->String,
    navigateToSala:(id:Long)->Unit,
    modifier:Modifier = Modifier
){
    Surface(modifier = modifier
        .padding(5.dp)
        .height(65.dp),
        onClick = {
            navigateToSala(sala.id)
        }) {
//        LocalDateTime.parse()
        Column(modifier = Modifier
            .fillMaxWidth()
//            .background(MaterialTheme.colorScheme.surface)
            .padding(5.dp)
        ) {
            Text(text = sala.titulo,style = MaterialTheme.typography.labelLarge, maxLines = 1)
            Text(text = "Se jugara:${formatDate(sala.horas.first())} de ${formatShortTime(sala.horas.first(),0)} a ${
                formatShortTime(sala.horas.last(), 30)
            }",style = MaterialTheme.typography.labelMedium)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "${sala.users}/${sala.cupos}",style = MaterialTheme.typography.labelSmall)
                Spacer(modifier = Modifier.width(5.dp))
                Icon(imageVector = Icons.Default.Group, contentDescription = sala.titulo,modifier = Modifier.size(15.dp))
            }
        }
    }
}
package app.regate.reservar.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.regate.data.dto.empresa.establecimiento.CupoEstablecimiento
import kotlinx.datetime.Instant

@Composable
fun FilterHeader(
    openDatePicker:()->Unit,
    currentDate:Instant,
    establecimientoCupos:List<CupoEstablecimiento>,
    formatShortTime:(time:Instant)->String,
    getInstalacionesAvailables:(start:Instant,cupos:Int)->Unit,
    modifier:Modifier = Modifier
){
    Column(modifier = modifier) {
        Text(text = "Decide cuándo quieres asistir",style = MaterialTheme.typography.titleMedium)
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(onClick={ openDatePicker() },
                modifier = Modifier) {
                Icon(imageVector = Icons.Default.CalendarToday, contentDescription = "calendar")
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = currentDate.toString()
                )
            }
            OutlinedButton(onClick = {}) {
                Icon(imageVector = Icons.Default.HourglassEmpty, contentDescription = "hourglass")
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "60m"
                )
            }
        }
        LazyRow(contentPadding = PaddingValues(10.dp)) {
            items(items = establecimientoCupos) {
                OutlinedButton(modifier = Modifier.padding(5.dp),
                    onClick = { getInstalacionesAvailables(it.start_time,it.cupos) }) {
                    Text(text = "${formatShortTime(it.start_time)} a ${formatShortTime(it.end_time)}",
                        style = MaterialTheme.typography.labelMedium)
                }
                Spacer(modifier = Modifier.width(5.dp))
            }
        }
    }
}



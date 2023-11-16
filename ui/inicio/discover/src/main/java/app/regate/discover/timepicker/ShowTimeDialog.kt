package app.regate.discover.timepicker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.datetime.LocalTime
import kotlinx.datetime.toJavaLocalTime
import app.regate.common.resources.R
import kotlinx.datetime.toKotlinLocalTime

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun ShowTimeDialog(
    show:Boolean,
    onDismiss:()->Unit,
    startTime:LocalTime,
    endTime:LocalTime,
    setTime:(LocalTime,LocalTime)->Unit
){
    var start by remember(startTime) {
        mutableStateOf(startTime)
    }
    var end by remember(endTime) {
        mutableStateOf(endTime)
    }

    fun applyChanges(){
        setTime(start,end)
        onDismiss()
    }
    fun resetChanges(){
        start = startTime
        end = endTime
        onDismiss()
    }

    if(show){
        Dialog(onDismissRequest = {resetChanges()}) {
            Column(modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surface)
                .padding(10.dp)
            ) {

            FlowRow(modifier = Modifier.fillMaxWidth()) {


           Column(horizontalAlignment = Alignment.CenterHorizontally) {
               Text(text = "Inicio",style = MaterialTheme.typography.labelLarge,
               color = MaterialTheme.colorScheme.primary)
            WheelTimePicker(
                timeFormat = TimeFormat.HOUR_24,
                startTime =start.toJavaLocalTime(),
                modifier = Modifier
                    .padding(10.dp),
                onSnappedTime = { start = it.toKotlinLocalTime() },
                maxTime = end.toJavaLocalTime().minusMinutes(30)
            )
           }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Fin",style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary)
                    WheelTimePicker(
                        timeFormat = TimeFormat.HOUR_24,
                        startTime =end.toJavaLocalTime(),
                        modifier = Modifier
                            .padding(10.dp),
                        onSnappedTime = { end = it.toKotlinLocalTime() },
                        minTime = start.toJavaLocalTime().plusMinutes(30)
                    )
                }
            }

                Row(modifier =Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = { resetChanges() }) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    TextButton(onClick = { applyChanges() }) {
                        Text(text = stringResource(id = R.string.save))
                    }
                }

            }
        }
    }
}
package app.regate.common.compose.components.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import app.regate.data.common.HoraIntervalo
import app.regate.data.dto.empresa.establecimiento.HorarioInterval

@Composable
fun DialogHour(
    showDialog:Boolean,
    dismiss:()->Unit,
    intervalos: List<HorarioInterval>?,
    setIntervalo:(minutes:Long)->Unit,
    minutes:Long,
    modifier: Modifier = Modifier
) {
    if (showDialog) {
        Dialog(onDismissRequest = { dismiss() }) {
            Column(
                modifier = modifier
                    .verticalScroll(rememberScrollState())
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(10.dp)
            ) {
                Text(
                    text = "¿Por cuánto tiempo te gustaría jugar?",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(15.dp))
                intervalos?.map {
                    TextButton(
                        onClick = {
                            setIntervalo(it.minutes)
                            dismiss()
                        }, modifier = Modifier.fillMaxWidth(),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = if (minutes == it.minutes) MaterialTheme.colorScheme.primary else Color.Transparent,
                            contentColor = if (minutes == it.minutes) Color.White else MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(text = "${it.minutes} minutos")
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
fun DialogHour2(
    showDialog:Boolean,
    dismiss:()->Unit,
    intervalos: List<HoraIntervalo>?,
    setIntervalo:(minutes:Long)->Unit,
    minutes:Long,
    modifier: Modifier = Modifier
) {
    if (showDialog) {
        Dialog(onDismissRequest = { dismiss() }) {
            Column(
                modifier = modifier
                    .verticalScroll(rememberScrollState())
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(10.dp)
            ) {
                Text(
                    text = "¿Por cuánto tiempo te gustaría jugar?",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(15.dp))
                intervalos?.map {
                    TextButton(
                        onClick = {
                            setIntervalo(it.value)
                            dismiss()
                        }, modifier = Modifier.fillMaxWidth(),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = if (minutes == it.value) MaterialTheme.colorScheme.primary else Color.Transparent,
                            contentColor = if (minutes == it.value) Color.White else MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(text = "${it.value} minutos")
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}
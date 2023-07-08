package app.regate.common.composes.components.dialog

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialogComponent(
    state:DatePickerState,
    show:Boolean,
    dismissDialog:()->Unit,
    modifier:Modifier = Modifier,
    dateValidator: (Long) -> Boolean = { true },
){
//    val now = Clock.System.now()
//    val datePickerFormatter = DatePickerFormatter()
    if(show) {
        DatePickerDialog(onDismissRequest = { dismissDialog() }, confirmButton = {
            Button(
                onClick = { dismissDialog() }) {
                Text(text = "Confirmar")
            }
        }) {
            DatePicker(
                state = state,
                modifier = modifier,
                dateValidator = dateValidator
//                    modifier = Modifier.align(Alignment.Center),

            )
        }
    }
}

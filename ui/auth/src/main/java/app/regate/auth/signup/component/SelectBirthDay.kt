package app.regate.auth.signup

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.regate.common.composes.LocalAppDateFormatter
import app.regate.common.composes.components.dialog.DatePickerDialogComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectBirthDay(
    modifier:Modifier = Modifier,
    navigateTab:(tab:Int)->Unit
) {
    val datePickerState = rememberDatePickerState()
    val showDialog = remember {
        mutableStateOf(false)
    }
    val formatter = LocalAppDateFormatter.current
//    val locale = ConfigurationCompat.getLocales(LocalConfiguration.current).get(0) ?: Locale.getDefault()
    Column(modifier) {
//        OutlinedTextInput(value = "", onChangeValue = {}, placeholder = "",modifier = Modifier.fillMaxWidth(),
//            icon = Icons.Outlined.CalendarMonth)
//        Column(modifier = Modifier.clickable { showDialog.value = true }) {

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDialog.value = true }
                .padding(horizontal = 10.dp)
        ) {
            if (datePickerState.selectedDateMillis == null) {
                Text(text = "")
            } else {
                Text(
                    text = formatter.formatWithSkeleton(
                        datePickerState.selectedDateMillis!!,
                        formatter.yearAbbrMonthDaySkeleton,
                    )
                )
            }
//                DatePicker(state = datePickerState)
            IconButton(onClick = { showDialog.value = true }) {
                Icon(
                    imageVector = Icons.Outlined.CalendarMonth,
                    contentDescription = "icon_birtday"
                )
            }
        }
        Canvas(
            modifier = Modifier
                .fillMaxWidth(1f)
                .height(1.dp)
//                    .padding(top = 10.dp)
        ) {

            drawLine(
                color = Color.LightGray,
                start = Offset(0f, 0f),
                end = Offset(size.width, 0f),
                strokeWidth = 0.7.dp.toPx()
            )
        }


        Spacer(modifier = Modifier.height(50.dp))
        Button(
            onClick = { navigateTab(2) }, modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = datePickerState.selectedDateMillis != null,
            shape = CircleShape
        ) {
            Text(text = "Siguiente")
        }
        Spacer(modifier = Modifier.height(15.dp))
        Button(
            onClick = { navigateTab(0) }, modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = CircleShape
        ) {
            Text(text = "Atras")
        }
    }

    DatePickerDialogComponent(
        show = showDialog.value,
        dismissDialog = { showDialog.value = false },
        state = datePickerState
    )

}

package app.regate.common.composes.component.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun SmallButton (
    text:String,
    onClick:()->Unit,
    modifier: Modifier = Modifier,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    shape: Shape  = MaterialTheme.shapes.medium,
){
    Button(onClick = { onClick() },
    contentPadding = PaddingValues(horizontal =  8.dp, vertical = 5.dp),
        modifier = modifier,
        shape = shape,
        colors = colors
    ) {
        Text(text = text,
        style = MaterialTheme.typography.labelMedium)
    }
}
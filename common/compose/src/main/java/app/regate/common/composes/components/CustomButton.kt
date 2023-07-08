package app.regate.common.composes.components

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.Button
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CustomButton(
    onClick:() -> Unit,
    modifier:Modifier = Modifier,
    shape: CornerBasedShape = ShapeDefaults.Small,
    content:@Composable () -> Unit,
){
    Button(onClick = { onClick() },modifier =modifier, shape = shape) {
        content()

    }
}
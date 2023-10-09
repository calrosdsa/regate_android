package app.regate.common.composes.component

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.Button
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CustomButton (
    onClick:() -> Unit,
    modifier:Modifier = Modifier,
    enabled:Boolean = true,
    shape: CornerBasedShape = ShapeDefaults.Small,
    content:@Composable () -> Unit,
){
    Button(onClick = { onClick() },modifier =modifier, shape = shape,
    enabled = enabled) {
        content()

    }
}


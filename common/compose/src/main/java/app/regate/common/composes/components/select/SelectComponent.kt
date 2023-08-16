package app.regate.common.composes.components.select

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SelectComponent(
    selected:Boolean,
    text:String,
    onSelect:(b:Boolean)->Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = { onSelect(!selected) },
        shape = MaterialTheme.shapes.medium, border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        modifier = modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth()
            .alpha(if (selected) 1f else 0.5f)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
//            RadioButton(selected = selected, onClick = { onSelect(!selected) })
            IconToggleButton(checked = selected, onCheckedChange = {

                onSelect(it) }) {
                Crossfade(targetState = selected) {
                 if(it) {
                      Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "check_icon")
                 }else{
                     Icon(imageVector = Icons.Outlined.Circle, contentDescription = "check_icon")
                 }
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = text)
        }
    }
}

@Preview
@Composable
fun SelectComponentPreview(){
    SelectComponent(selected = true, text = "Select Item", onSelect = {})
}
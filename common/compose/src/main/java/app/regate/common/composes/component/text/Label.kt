package app.regate.common.composes.component.text

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Label(
    text:String,
    modifier:Modifier = Modifier,
){
    Text(text = text,style =MaterialTheme.typography.labelLarge,modifier = modifier)
}
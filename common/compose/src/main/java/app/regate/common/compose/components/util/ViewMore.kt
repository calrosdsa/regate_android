package app.regate.common.compose.components.util

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import app.regate.common.resources.R

@Composable
fun ViewMore (
    label:String,
    modifier:Modifier= Modifier,
    styleText: TextStyle = MaterialTheme.typography.titleMedium,
    showTextButton:Boolean = true,
    onClick:() -> Unit,
){
    Row(modifier = modifier
        .fillMaxWidth()
        .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween){
        Text(text = label,style = styleText,modifier = Modifier.fillMaxWidth(0.7f),

            )

        if(showTextButton){
        TextButton(onClick = { onClick() }) {
            Text(text = stringResource(id = R.string.show_more),color = MaterialTheme.colorScheme.primary, textDecoration = TextDecoration.Underline,
                style = MaterialTheme.typography.labelLarge)
        }
        }
    }
}
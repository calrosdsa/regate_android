package app.regate.common.compose.components.text

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

@Composable
fun MultiStyleText(
    text1: String,
    color1: Color,
    text2: String,
    color2: Color,
    modifier:Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.labelLarge
) {
    Text(buildAnnotatedString {
        withStyle(style = SpanStyle(color = color1)) {
            append(text1)
        }
        withStyle(style = SpanStyle(color = color2)) {
            append(text2)
        }
    },modifier = modifier, style = style)
}
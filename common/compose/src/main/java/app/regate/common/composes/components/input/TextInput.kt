package app.regate.common.composes.components.input

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun CustomOutlinedTextInput(
    value:String,
    onValueChange:(value:String)->Unit,
    placeholder:String="",
    modifier: Modifier = Modifier,
    label:String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    icon:ImageVector? = null,
){
//    var email by remember { mutableStateOf("") }
      OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        maxLines = 1,
        label = { Text(text = label) },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        modifier = modifier.fillMaxWidth(),
//        shape = CircleShape,
        placeholder = { Text(text = placeholder)},
        trailingIcon = {
            if(icon != null){
            Icon(imageVector = icon, contentDescription = "Trailing icon")
            }
        }
    )
}

@Composable
fun TextInput(
    value:String,
    onChangeValue:(value:String)->Unit,
    placeholder:String,
    modifier: Modifier = Modifier,
    icon:ImageVector? = null,
){
//    var email by remember { mutableStateOf("") }
    TextField(
        value = value,
        onValueChange = { onChangeValue(it) },
        maxLines = 1,
        label = { Text(text = "Email") },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email
        ),
        modifier = modifier,

        shape = CircleShape,
        placeholder = { Text(text = placeholder)},
        trailingIcon = {
            if(icon != null){
                Icon(imageVector = icon, contentDescription = "Trailing icon")
            }
        },
    )
}




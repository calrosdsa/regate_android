package app.regate.common.composes.components.input

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

@SuppressLint("ComposeParameterOrder")
@Composable
fun CustomOutlinedTextInput(
    value:String,
    onValueChange:(value:String)->Unit,
    modifier: Modifier = Modifier,
    placeholder:String="",
    label:String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    icon:ImageVector? = null,
    supportText:String?= null,
    isError:Boolean = false,
    maxLines:Int = 1
){
//    var email by remember { mutableStateOf("") }
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        maxLines = maxLines,
        label = { Text(text = label) },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction

        ),
        keyboardActions =keyboardActions,
        modifier = modifier.fillMaxWidth(),
//        shape = CircleShape,
        placeholder = { Text(text = placeholder) },
        trailingIcon = {
            if (icon != null) {
                Icon(imageVector = icon, contentDescription = "Trailing icon")
            }
        },
        supportingText = {
            if (supportText != null) {
                Text(text = supportText)
            }
        },
        isError = isError,
    )
}

@Composable
fun InputForm(
    onValueChange:(value:String)->Unit,
    value:String,
//    maxCharacters:Int,
//    currentCharacters:Int,
    modifier: Modifier = Modifier,
    placeholder:String="",
    label:String = "",
    maxLines:Int = 1,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    icon:ImageVector? = null,
    content: @Composable() (BoxScope.() -> Unit) = {},
){
    Box() {

    content()
//    var email by remember { mutableStateOf("") }
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        maxLines = maxLines,
        label = { Text(text = label) },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        modifier = modifier.fillMaxWidth(),
//        shape = CircleShape,
        placeholder = { Text(text = placeholder)},
          trailingIcon = {
                    if(icon != null){
                        Icon(imageVector = icon, contentDescription = "Trailing icon")
            }
        },
        keyboardActions = keyboardActions,
    )
    }
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




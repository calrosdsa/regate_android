package app.regate.common.composes.component.input

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import app.regate.common.resources.R

@Composable
fun SearchInput (
    query:String,
    focusRequester: FocusRequester,
    interactionSource: MutableInteractionSource,
    onSearch:(String)->Unit,
    onChange:(String)->Unit,
    navigateUp:()->Unit,
    modifier: Modifier = Modifier,
    shouldShowResults:()->Unit ={},
){
    Row(
        modifier = modifier.padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { navigateUp() }) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = stringResource(id = R.string.back)
            )
        }
        BasicTextField(value = query,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearch(query)
                }
            ),
            modifier = Modifier.focusRequester(focusRequester),
            maxLines = 1,
            onValueChange = { onChange(it) },
            textStyle = TextStyle(color = MaterialTheme.colorScheme.onBackground),
            interactionSource = interactionSource,
            decorationBox = { innerTextField ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .padding(horizontal = 0.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.inverseOnSurface
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(5.dp)
                    ) {
                        Spacer(modifier = Modifier.width(5.dp))
                        Box(modifier = Modifier.fillMaxWidth(0.9f)) {
                            if (query.isBlank()) {
                                Text(
                                    text = stringResource(id = R.string.search),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.inverseSurface
                                )
                            }
                            innerTextField()
                        }
                        Crossfade(targetState = !query.isBlank()) {
                            if (it) {
                                IconButton(onClick = {
                                    onChange("")
                                    shouldShowResults()
                                    focusRequester.requestFocus()
                                }) {
                                    Icon(
                                        imageVector = Icons.Outlined.Close,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}
package app.regate.common.composes.component.input.emoji

import app.regate.common.resources.R
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.getTextAfterSelection
import androidx.compose.ui.text.input.getTextBeforeSelection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.regate.data.app.EmojisState

@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
fun EmojiLayout(
    emojis:EmojisState,
    message:TextFieldValue,
    updateMessage:(TextFieldValue)->Unit,
    modifier:Modifier = Modifier,
) {

    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 40.dp),
            modifier = modifier,
            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 5.dp),
        ) {
            item(span = {
                GridItemSpan(maxLineSpan)
            }) {
                EmojiHeader(text = stringResource(id = R.string.emileys_emotion))
            }
            items(
                items = emojis.emoticonos,
                key = { it.emoji }
            ) { emoji ->
                EmojiItem(emoji = emoji.emoji,onClick = { updateMessage(it)},
                textFieldValue = message)
            }

            item(span = {
                GridItemSpan(maxLineSpan)
            }) {
                EmojiHeader(text = stringResource(id = R.string.people))
            }
            items(
                items = emojis.people,
                key = { it.emoji }
            ) { emoji ->
                EmojiItem(emoji = emoji.emoji,onClick = { updateMessage(it)},
                textFieldValue = message)
            }

            item(span = {
                GridItemSpan(maxLineSpan)
            }) {
                EmojiHeader(text = stringResource(id = R.string.animals_and_nature))
            }
            items(
                items = emojis.animals_nature,
                key = { it.emoji }
            ) { emoji ->
                EmojiItem(emoji = emoji.emoji,onClick = { updateMessage(it)},
                textFieldValue = message)
            }

            item(span = {
                GridItemSpan(maxLineSpan)
            }) {
                EmojiHeader(text = stringResource(id = R.string.food_and_drink))
            }
            items(
                items = emojis.food_drink,
                key = { it.emoji }
            ) { emoji ->
                EmojiItem(emoji = emoji.emoji,onClick = { updateMessage(it)},
                textFieldValue = message)
            }

            item(span = {
                GridItemSpan(maxLineSpan)
            }) {
                EmojiHeader(text = stringResource(id = R.string.travel_and_places))
            }
            items(
                items = emojis.travel_places,
                key = { it.emoji }
            ) { emoji ->
                EmojiItem(emoji = emoji.emoji,onClick = { updateMessage(it)},
                textFieldValue = message)
            }

            item(span = {
                GridItemSpan(maxLineSpan)
            }) {
                EmojiHeader(text = stringResource(id = R.string.activities_and_event))
            }
            items(
                items = emojis.activities,
                key = { it.emoji }
            ) { emoji ->
                EmojiItem(emoji = emoji.emoji,onClick = { updateMessage(it)},
                textFieldValue = message)
            }

            item(span = {
                GridItemSpan(maxLineSpan)
            }) {
                EmojiHeader(text = stringResource(id = R.string.objects))
            }
            items(
                items = emojis.objects,
                key = { it.emoji }
            ) { emoji ->
                EmojiItem(emoji = emoji.emoji,onClick = { updateMessage(it)},
                textFieldValue = message)
            }

            item(span = {
                GridItemSpan(maxLineSpan)
            }) {
                EmojiHeader(text = stringResource(id = R.string.symbols))
            }
            items(
                items = emojis.symbols,
                key = { it.emoji }
            ) { emoji ->
                EmojiItem(emoji = emoji.emoji,onClick = { updateMessage(it)},
                textFieldValue = message)
            }

            item(span = {
                GridItemSpan(maxLineSpan)
            }) {
                EmojiHeader(text = stringResource(id = R.string.flags))
            }
            items(
                items = emojis.flags,
                key = { it.emoji }
            ) { emoji ->
                EmojiItem(emoji = emoji.emoji,onClick = { updateMessage(it)},
                textFieldValue = message)
            }

        }

//    Column(modifier = modifier
//        .padding(5.dp)
//        .verticalScroll(rememberScrollState())){
//        FlowRow() {
//       emojis.value.emoticonos.map {emoji: Emoji ->
//        Text(text = emoji.emoji, fontSize = 30.sp)
//       }
//        }
//    }
    }
}

@Composable
internal fun EmojiItem(
    emoji:String,
    textFieldValue: TextFieldValue,
    onClick: (TextFieldValue) -> Unit
){
    Text(
        text = emoji, fontSize = 28.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.clickable { onClick( insertText(textFieldValue, emoji)) }
    )

}

@Composable
internal fun EmojiHeader(
    text:String
){
    Text(text = text,
        style = MaterialTheme.typography.labelLarge,
        modifier = Modifier.padding(vertical = 5.dp))
}


private fun insertText(textFieldValue: TextFieldValue, insertText: String): TextFieldValue {
    val maxChars = textFieldValue.text.length
    val textBeforeSelection = textFieldValue.getTextBeforeSelection(maxChars)
    val textAfterSelection = textFieldValue.getTextAfterSelection(maxChars)
    val newText = "$textBeforeSelection$insertText$textAfterSelection"
    val newCursorPosition = textBeforeSelection.length + insertText.length
    return TextFieldValue(
        text = newText,
        selection = TextRange(newCursorPosition)
    )
}
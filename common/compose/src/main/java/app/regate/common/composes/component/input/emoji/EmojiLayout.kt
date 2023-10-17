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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoDelete
import androidx.compose.material.icons.filled.BorderClear
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.EmojiFlags
import androidx.compose.material.icons.filled.EmojiFoodBeverage
import androidx.compose.material.icons.filled.EmojiNature
import androidx.compose.material.icons.filled.EmojiObjects
import androidx.compose.material.icons.filled.EmojiPeople
import androidx.compose.material.icons.filled.EmojiSymbols
import androidx.compose.material.icons.filled.EmojiTransportation
import androidx.compose.material.icons.filled.FormatClear
import androidx.compose.material.icons.filled.LayersClear
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.getTextAfterSelection
import androidx.compose.ui.text.input.getTextBeforeSelection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.regate.data.app.EmojiCategory
import app.regate.data.app.EmojisState
import app.regate.models.Emoji
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
fun EmojiLayout(
    emojis:List<List<Emoji>>,
    message:TextFieldValue,
    coroutineScope: CoroutineScope,
    modifier:Modifier = Modifier,
    updateMessage:(TextFieldValue)->Unit,
) {
    val pagerState = rememberPagerState()
    Scaffold(modifier = Modifier.fillMaxSize(),
    bottomBar = {
        TabRow(selectedTabIndex = pagerState.currentPage) {
            Tab(selected = pagerState.currentPage == 0, onClick = {
               coroutineScope.launch {
                   launch { pagerState.scrollToPage(0) }
               }
            },
                icon = {
                    Icon(
                        imageVector = Icons.Default.EmojiEmotions,
                        contentDescription = null
                    )
                })
            Tab(selected = pagerState.currentPage == 1, onClick = {
                coroutineScope.launch {
                    launch { pagerState.scrollToPage(1) }
                }
            },
                icon = {
                    Icon(
                        imageVector = Icons.Default.EmojiPeople,
                        contentDescription = null
                    )
                })
            Tab(selected = pagerState.currentPage == 2, onClick = {
                coroutineScope.launch {
                    launch { pagerState.scrollToPage(2) }
                }
            },
                icon = {
                    Icon(
                        imageVector = Icons.Default.EmojiNature,
                        contentDescription = null
                    )
                })
            Tab(selected = pagerState.currentPage == 3, onClick = {
                coroutineScope.launch {
                    launch { pagerState.scrollToPage(3) }
                }
            },
                icon = {
                    Icon(
                        imageVector = Icons.Default.EmojiFoodBeverage,
                        contentDescription = null
                    )
                })
            Tab(selected = pagerState.currentPage == 4, onClick = {
                coroutineScope.launch {
                    launch { pagerState.scrollToPage(4) }
                }
            },
                icon = {
                    Icon(
                        imageVector = Icons.Default.EmojiTransportation,
                        contentDescription = null
                    )
                })
            Tab(selected = pagerState.currentPage == 5, onClick = {
                coroutineScope.launch {
                    launch { pagerState.scrollToPage(5) }
                }
            },
                icon = {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null
                    )
                })
            Tab(selected = pagerState.currentPage == 6, onClick = {
                coroutineScope.launch {
                    launch { pagerState.scrollToPage(6) }
                }
            },
                icon = {
                    Icon(
                        imageVector = Icons.Default.EmojiObjects,
                        contentDescription = null
                    )
                })
            Tab(selected = pagerState.currentPage == 7, onClick = {
                coroutineScope.launch {
                    launch { pagerState.scrollToPage(7) }
                }
            },
                icon = {
                    Icon(
                        imageVector = Icons.Default.EmojiSymbols,
                        contentDescription = null
                    )
                })
            Tab(selected = pagerState.currentPage == 8, onClick = {  coroutineScope.launch {
                launch { pagerState.scrollToPage(8) }
            } },
                icon = {
                    Icon(
                        imageVector = Icons.Default.EmojiFlags,
                        contentDescription = null,
                    )
                })
            Tab(selected = pagerState.currentPage == 9, onClick = {
                updateMessage(deleteText(message))
            },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.delete_icon),
                        contentDescription = null
                    )
                })
        }
    }) {paddingValues->
        if(emojis.isNotEmpty()){

        HorizontalPager(pageCount = emojis.size,
        state=pagerState) {page->

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = modifier.padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 5.dp),
        ) {
//            item(span = {
//                GridItemSpan(maxLineSpan)
//            }) {
//                EmojiHeader(text = stringResource(id = R.string.emileys_emotion))
//            }
            items(
                items = emojis[page],
                key = { it.id }
            ) { emoji ->
                EmojiItem(
                    emoji = emoji.emoji, onClick = { updateMessage(it) },
                    textFieldValue = message
                )
            }
        }
        }
        }
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

private fun deleteText(textFieldValue:TextFieldValue):TextFieldValue {
    val maxChars = textFieldValue.text.length
    val textBeforeSelection = textFieldValue.getTextBeforeSelection(maxChars)
    val textAfterSelection = textFieldValue.getTextAfterSelection(maxChars)
    val newText = "${textBeforeSelection.dropLast(1)}$textAfterSelection"
    val newCursorPosition = textBeforeSelection.length -1
    return TextFieldValue(
        text = newText,
        selection = TextRange(newCursorPosition)
    )
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
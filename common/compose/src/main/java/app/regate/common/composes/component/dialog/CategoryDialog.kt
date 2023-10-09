package app.regate.common.composes.component.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import app.regate.common.composes.component.images.AsyncImage
import app.regate.common.resources.R
import app.regate.models.Labels

@Composable
fun CategoryDialog(
    showDialog:Boolean,
    selectedCategory:Labels?,
    categories:List<Labels>,
    closeDialog:()->Unit,
    setCategory:(id:Long)->Unit,
    modifier: Modifier=Modifier,
){
    if (showDialog) {
        Dialog(onDismissRequest = { closeDialog() }) {
            Column(
                modifier = modifier
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = stringResource(id = R.string.what_would_you_like_to_play),
                    modifier = Modifier
                        .padding(10.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium
                )
                LazyVerticalGrid(columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(5.dp)
                ) {
                    items(
                        items = categories
                    ) { item ->
                        OutlinedButton(
                            onClick = { setCategory(item.id)  },
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (selectedCategory?.id == item.id) MaterialTheme.colorScheme.primary else Color.Transparent,
                                contentColor = if (selectedCategory?.id == item.id) Color.White else MaterialTheme.colorScheme.primary

                            ),
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.padding(5.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    AsyncImage(
                                        model = item.thumbnail,
                                        contentDescription = item.thumbnail,
                                        modifier = Modifier.size(25.dp),
                                        colorFilter = ColorFilter.tint(if (selectedCategory?.id == item.id) Color.White else MaterialTheme.colorScheme.onBackground)
                                    )
                                Text(
                                    text = item.name,
                                    style = MaterialTheme.typography.labelLarge,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
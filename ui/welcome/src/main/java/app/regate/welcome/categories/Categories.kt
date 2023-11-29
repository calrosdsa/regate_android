package app.regate.welcome.categories

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.regate.common.composes.component.images.AsyncImage
import app.regate.common.resources.R
import app.regate.models.Labels

@Composable
fun Categories (
    categories:List<Labels>,
    selectedCategories:List<Long>,
    addCategory:(Long)->Unit,
    modifier:Modifier = Modifier,
    navigateToPage:(Int)->Unit,
){
    Scaffold(
        bottomBar = {
            BottomAppBar() {
                Box(modifier = Modifier.fillMaxWidth()){
                Button(onClick = { navigateToPage(2) },modifier = Modifier.align(Alignment.CenterEnd),
                enabled = selectedCategories.isNotEmpty()) {
                    Text(text = stringResource(id = R.string.continuar))
                }
                }
            }
        },
        topBar = {
            Text(text = stringResource(id = R.string.what_kind_of_game_do_you_prefere),
                style = MaterialTheme.typography.headlineLarge, textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp))
        }
    ) {paddingValues->

        LazyVerticalGrid(columns = GridCells.Fixed(2),
            modifier = modifier.padding(10.dp).fillMaxHeight(0.6f).padding(paddingValues),
            content = {
            items(
                items = categories,
            ){ category->
                OutlinedButton(
                    onClick = { addCategory(category.id)  },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (selectedCategories.contains(category.id)) MaterialTheme.colorScheme.primary else Color.Transparent,
                    contentColor = if (selectedCategories.contains(category.id)) Color.White else MaterialTheme.colorScheme.primary

                ),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(5.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(5.dp)) {
                    AsyncImage(
                        model = category.thumbnail,
                        contentDescription = category.thumbnail,
                        modifier = Modifier.size(45.dp),
                        colorFilter = ColorFilter.tint(if (selectedCategories.contains(category.id)) Color.White else MaterialTheme.colorScheme.onBackground)
                    )
                        Text(
                            text = category.name,
                            style = MaterialTheme.typography.labelLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        })

    }
}

package app.regate.profile.categories

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import app.regate.common.composes.component.dialog.DialogConfirmation
import app.regate.common.composes.component.dialog.LoaderDialog
import app.regate.common.composes.component.images.AsyncImage
import app.regate.common.composes.component.input.AmenityItem
import app.regate.common.composes.viewModel
import app.regate.common.resources.R
import app.regate.models.Labels
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

typealias ProfileCategories = @Composable (
    navigateUp:()->Unit
        ) -> Unit

@Inject
@Composable
fun ProfileCategories(
    viewModelFactory:(SavedStateHandle) ->ProfileCategoriesViewModel,
    @Assisted navigateUp: () -> Unit
){

    ProfileCategories(
        viewModel = viewModel(factory = viewModelFactory),
        navigateUp = navigateUp
    )
}

@Composable
internal  fun ProfileCategories(
    viewModel: ProfileCategoriesViewModel,
    navigateUp: () -> Unit
){
    val state by viewModel.state.collectAsState()
    ProfileCategories(
        viewState = state,
        navigateUp = navigateUp,
        clearMessage = viewModel::clearMessage,
        add = viewModel::add,
        remove = viewModel::remove,
        save = viewModel::save
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun ProfileCategories(
    viewState:ProfileCategoriesState,
    clearMessage:(Long)->Unit,
    navigateUp: () -> Unit,
    save:()->Unit,
    add:(Long)->Unit,
    remove:(Long)->Unit
) {
    val selectedIds by remember(key1 = viewState.userCategories) {
        mutableStateOf(viewState.userCategories.map { it.id })
    }
//    var confirmationDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    viewState.message?.let { message ->
        LaunchedEffect(key1 = message, block = {
             snackbarHostState.showSnackbar(message.message)
            clearMessage(message.id)
        })
    }

    LoaderDialog(loading = viewState.loading)
//    DialogConfirmation(open = confirmationDialog, dismiss = { confirmationDialog = false },
//        confirm = { save() })

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState)},
        topBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(4.dp)
            ) {
                IconButton(onClick = { navigateUp() }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = null)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = stringResource(id = R.string.categories),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        },
        bottomBar = {
            BottomAppBar() {
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(onClick = {save() }) {
                        Text(text = stringResource(id = R.string.save))
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp)
                .padding(paddingValues)
        ) {
            LazyRow(
                modifier = Modifier
                    .padding(vertical = 15.dp), content = {
                    items(
                        items = viewState.userCategories
                    ) { item ->
                        CategoryItemUser(
                            category = item,
                            remove = remove
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                })
            Divider(modifier = Modifier.padding(vertical = 10.dp))

            FlowRow() {
                viewState.categories.map { item ->
                    AmenityItem(
                        amenity = item, isSelect = selectedIds.contains(item.id),
                        modifier = Modifier.padding(vertical = 3.dp, horizontal = 8.dp),
                        enabled = true,
                        onClick = {add(item.id)}
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    }
}


@Composable
internal fun CategoryItemUser(
    category: Labels,
    remove:(Long)->Unit,
    modifier: Modifier=Modifier
){
    Surface(
        shape = MaterialTheme.shapes.large,
//        border = (BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground)),
        contentColor = MaterialTheme.colorScheme.onPrimary,
        color = MaterialTheme.colorScheme.primary
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .padding(vertical = 8.dp, horizontal = 8.dp)
        ) {
            AsyncImage(
                model = category.thumbnail,
                contentDescription = category.name,
                modifier = Modifier.size(19.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = category.name, style = MaterialTheme.typography.labelSmall)
            Spacer(modifier = Modifier.width(5.dp))
            Icon(imageVector = Icons.Default.Close, contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .size(19.dp)
                    .clickable { remove(category.id) }
            )
        }
    }
}



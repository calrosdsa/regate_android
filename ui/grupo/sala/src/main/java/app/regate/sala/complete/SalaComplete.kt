package app.regate.sala.complete

import android.content.Context
import androidx.compose.foundation.clickable
import app.regate.common.resources.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import app.regate.common.composes.component.dialog.DialogConfirmation
import app.regate.common.composes.component.dialog.LoaderDialog
import app.regate.common.composes.component.images.ProfileImage
import app.regate.common.composes.ui.SimpleTopBar
import app.regate.common.composes.util.spacerLazyList
import app.regate.common.composes.viewModel
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

typealias SalaComplete = @Composable (
    navigateUp:()->Unit
 ) -> Unit

@Inject
@Composable
fun SalaComplete(
    viewModelFactory:(SavedStateHandle)->SalaCompleteViewModel,
    @Assisted navigateUp: () -> Unit,
){
    SalaComplete(viewModel = viewModel(factory = viewModelFactory),
    navigateUp = navigateUp)
}

@Composable
internal fun SalaComplete(
    viewModel: SalaCompleteViewModel,
    navigateUp: () -> Unit,
){
    val state by viewModel.state.collectAsState()
    SalaComplete(
        viewState = state, navigateUp = navigateUp,
        clearMessage = viewModel::clearMessage,
        completeSala = viewModel::completeSala,
        deleteCompleteSala = viewModel::deleteCompleteSala
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SalaComplete(
    viewState:SalaCompleteState,
    navigateUp: () -> Unit,
    clearMessage:(Long)->Unit,
    completeSala:(Context,String)->Unit,
    deleteCompleteSala:(Context,Double,Int)->Unit,
) {
    val context = LocalContext.current
    var amount by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    var showConfirmationDialog by remember { mutableStateOf(false) }

    viewState.message?.let { message ->
        LaunchedEffect(key1 = message, block = {
            snackbarHostState.showSnackbar(message.message)
            clearMessage(message.id)
        })
    }
    LoaderDialog(loading = viewState.loading)
    DialogConfirmation(open = showConfirmationDialog,
        dismiss = { showConfirmationDialog = false },
        confirm = {
            completeSala(context, amount)
            showConfirmationDialog = false
        })
    Scaffold(
        topBar = {
            SimpleTopBar(navigateUp = navigateUp)
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            LazyColumn(modifier = Modifier.padding(horizontal = 10.dp)) {
                viewState.salaCompleteDetail?.let { data ->
                    item{
                        Surface(modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                            shadowElevation = 10.dp,
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Row(modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(text = data.precio.toString(), style = MaterialTheme.typography.labelLarge)
                                    Text(text = stringResource(id = R.string.total_price),style=MaterialTheme.typography.labelMedium)
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(text = data.paid.toString(), style = MaterialTheme.typography.labelLarge)
                                    Text(text = stringResource(id = R.string.total_paid),style=MaterialTheme.typography.labelMedium)
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(text = data.rest.toString(), style = MaterialTheme.typography.labelLarge)
                                    Text(text = stringResource(id = R.string.total_remaining),style=MaterialTheme.typography.labelMedium)
                                }
                            }
                        }
                    }

                    spacerLazyList()
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = amount, onValueChange = {
                                    amount = it
                                }, modifier = Modifier.fillMaxWidth(0.5f),
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    keyboardType = KeyboardType.Decimal
                                )
                            )
                            Button(
                                enabled = (amount.isNotBlank()&& data.rest != 0.0),
                                onClick = { showConfirmationDialog = true }) {
                                Text(text = stringResource(id = R.string.complete))
                            }
                        }
                    }
                    spacerLazyList()
                    item {
                        Text(
                            text = stringResource(id = R.string.history),
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(top = 10.dp, bottom = 2.dp)
                        )
                    }
                    spacerLazyList()

                    items(
                        items = data.history
                    ) { item ->
                        CompleteSalaItem(
                            id = item.profile.profile_id,
                            nombre = item.profile.nombre,
                            apellido = item.profile.apellido,
                            photo = item.profile.profile_photo,
                            isMe = viewState.user?.profile_id == item.profile.profile_id,
                            amount = item.amount,
                            deleteCompleteSala = {deleteCompleteSala(context,item.amount,item.id)},
                        )
                    }
                }
            }
        }
    }
}


@Composable
internal fun CompleteSalaItem(
    id:Long,
    amount:Double,
    photo:String?,
    nombre:String,
    apellido:String?,
    modifier:Modifier = Modifier,
    isMe:Boolean = false,
    navigateToProfile:(Long)->Unit= {},
    deleteCompleteSala: () -> Unit
) {
    Row(modifier = modifier
        .clickable { navigateToProfile(id) }
        .padding(5.dp)
        .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

    Row(
        modifier =Modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {

        ProfileImage(
            profileImage = photo,
            modifier = Modifier
                .clip(CircleShape)
                .size(40.dp),
            contentDescription = nombre,
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column() {
        if(isMe){
            Text(
                text = stringResource(id = R.string.you), style = MaterialTheme.typography.labelLarge,
            )
        }else{
            Text(
                text = "$nombre ${apellido ?: ""}", style = MaterialTheme.typography.labelLarge,
                maxLines = 1, overflow = TextOverflow.Ellipsis
            )
        }
            Text(text = stringResource(id = R.string.contributed_amount,amount.toString()),
            style = MaterialTheme.typography.labelMedium)

        }
    }
        if(isMe){
        IconButton(onClick = { deleteCompleteSala() }) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = null)
        }
        }

    }
}
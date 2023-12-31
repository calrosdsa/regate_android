package app.regate.complejo.createreview

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import app.regate.common.resources.R
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import app.regate.common.composes.component.dialog.DialogConfirmation
import app.regate.common.composes.component.dialog.LoaderDialog
import app.regate.common.composes.component.input.InputForm
import app.regate.common.composes.ui.SimpleTopBar
import app.regate.common.composes.viewModel
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarStyle
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

typealias CreateReview= @Composable (
    navigateUp:()->Unit,
//    navigateToCreateSala: (id: Long) -> Unit,
   
) -> Unit


@Inject
@Composable
fun CreateReview(
    viewModelFactory:(SavedStateHandle)-> CreateReviewViewModel,
    @Assisted navigateUp: () -> Unit,

    ){
    CreateReview(
        viewModel = viewModel(factory = viewModelFactory),
        navigateUp = navigateUp,
       
    )

}

@Composable
internal fun CreateReview(
    viewModel: CreateReviewViewModel,
    navigateUp: () -> Unit,

    ) {
    val state by viewModel.state.collectAsState()
    LoaderDialog(loading = state.loading)
    CreateReview(
        viewState = state, navigateUp = navigateUp,
        clearMessage = viewModel::clearMessage,
        createReview = viewModel::createReview
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CreateReview(
    viewState: CreateReviewState,
    navigateUp: () -> Unit,
    createReview:(Int,String,Context)->Unit,
    clearMessage:(Long)->Unit
){
    val context = LocalContext.current
    var rating: Float by remember(viewState.review) {
        mutableStateOf(viewState.review?.score?.toFloat() ?: 0f)
    }
    var review by remember(viewState.review) {
        mutableStateOf(viewState.review?.review ?: "")
    }
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    var showConfirmationDialog by remember { mutableStateOf(false) }
    viewState.message?.let {message->
        LaunchedEffect(key1 = message, block = {
            snackbarHostState.showSnackbar(message.message)
            clearMessage(message.id)
        })
    }
        DialogConfirmation(
            open = showConfirmationDialog,
            dismiss = { showConfirmationDialog = false },
            confirm = {
                createReview(rating.toInt(), review, context)
                showConfirmationDialog = false
            })
    Scaffold(
        topBar = {
            SimpleTopBar(
                navigateUp = navigateUp,
                iconBack = Icons.Default.Close,
                title = stringResource(id = R.string.add_a_review),
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        bottomBar = {
            BottomAppBar() {
                Box(modifier = Modifier.fillMaxWidth()){
                    Button(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        onClick = { showConfirmationDialog = true }) {
                        Text(text = stringResource(id = R.string.post))
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(10.dp)
                .fillMaxSize()
        ) {
            Text(
                text = stringResource(id = R.string.rate_this_place),
                style = MaterialTheme.typography.labelLarge
            )
            Spacer(modifier = Modifier.height(10.dp))
            RatingBar(
                value = rating,
                style = RatingBarStyle.Fill(),
                onValueChange = {
                    rating = it
                },
                onRatingChanged = {
                    rating = it
                    Log.d("TAG", "onRatingChanged: $it")
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
            InputForm(
                value = review,
                onValueChange = {
                    if (it.length < 255) {
                        review = it
                    }
                },
//                placeholder = "Descripcion de la creacion del grupo",
                label = stringResource(id = R.string.review),
                modifier = Modifier.heightIn(min=160.dp),
                maxLines =  Int.MAX_VALUE,
                keyboardActions = KeyboardActions(
                    onDone = { showConfirmationDialog = true }
                ),
                imeAction = ImeAction.Done
//                maxCharacters = 255,
//                currentCharacters = review.length
            ){
                Text(text = "${review.length}/255",style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(2.dp))
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}
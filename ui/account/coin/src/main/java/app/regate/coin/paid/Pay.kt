package app.regate.coin.paid

import android.graphics.Bitmap
import app.regate.common.resources.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import app.regate.common.composes.LocalAppMedia
import app.regate.common.composes.ui.Loader
import app.regate.common.composes.ui.SimpleTopBar
import app.regate.common.composes.ui.Skeleton
import app.regate.common.composes.viewModel
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

typealias Pay = @Composable (
    navigateUp: ()->Unit
) -> Unit



@Inject
@Composable
fun Pay(
    viewModelFactory:(SavedStateHandle)-> PayViewModel,
    @Assisted navigateUp: () -> Unit,
){
    Pay(navigateUp = navigateUp,viewModel = viewModel(factory = viewModelFactory))
}


@Composable
internal fun Pay(
    viewModel: PayViewModel,
    navigateUp:()->Unit,
) {
    val state by viewModel.state.collectAsState()

    Pay(
        viewState = state,
        navigateUp = navigateUp,
        clearMessage = viewModel::clearMessage
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Pay(
    viewState:PayState,
    navigateUp: () -> Unit,
    clearMessage:(Long)->Unit
) {
    val appMedia = LocalAppMedia.current
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    viewState.message?.let { message ->
        LaunchedEffect(key1 = message, block = {
            snackbarHostState.showSnackbar(message.message)
            clearMessage(message.id)
        })
    }
    Scaffold(topBar = {
        SimpleTopBar(
            navigateUp = navigateUp,
            title = stringResource(id = R.string.qr_code_payment)
        )
    },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .padding(top = 15.dp)
                .fillMaxSize()
        ) {
            if (viewState.loading) {
                Loader(modifier = Modifier.align(Alignment.Center))
            } else {
                Column(
                    modifier = Modifier
                        .align(Alignment.TopCenter),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Image(
                            painter = painterResource(id = R.drawable.logo_app),
                            contentDescription = "logo_app", modifier = Modifier
                                .size(30.dp)
                                .align(
                                    Alignment.Center
                                )
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(modifier = Modifier.size(240.dp)) {
                        viewState.qr?.let {
                            ImageWithLoader(
                                model = it, loading = viewState.loading,
                                modifier = Modifier.fillMaxSize()
                            )
//                Image(bitmap = it.asImageBitmap(), contentDescription = null,

                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(onClick = { viewState.qr?.let { appMedia.saveImage(it,context) } }) {
                        Text(text = stringResource(id = R.string.save_image))
                    }

                    viewState.qrData?.let { qrData ->
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = stringResource(
                                id = R.string.expiration_date,
                                qrData.expirationDate
                            ), style = MaterialTheme.typography.labelMedium
                        )
                        Text(
                            text = stringResource(
                                id = R.string.amount,
                                "${qrData.amount}${qrData.currency}"
                            ), style = MaterialTheme.typography.labelMedium
                        )
                    }
                }

            }
        }
    }
}


@Composable
fun ImageWithLoader(
    model: Bitmap,
    loading:Boolean,
    modifier: Modifier = Modifier,
    shape: Shape = CardDefaults.shape,
    contentScale: ContentScale = ContentScale.Crop
){
    Card(modifier = modifier,
        shape = shape) {
        if(loading){
            Skeleton(modifier = Modifier.fillMaxSize())
        }else{
            Image(
                bitmap = model.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = contentScale,
            )
        }
    }
}

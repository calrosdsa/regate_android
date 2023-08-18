package app.regate.auth.signup.emailverification
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import app.regate.common.resources.R
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.regate.common.composes.components.dialog.LoaderDialog
import app.regate.common.composes.components.input.OtpTextField
import app.regate.common.composes.ui.SimpleTopBar
import app.regate.common.composes.viewModel
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

typealias EmailVerification = @Composable (
    navigateUp:()->Unit
        )->Unit

@Inject
@Composable
fun EmailVerification(
    viewModelFactory:()->EmailVerificationViewModel,
    @Assisted navigateUp: () -> Unit
){
    EmailVerification(
        viewModel = viewModel(factory = viewModelFactory),
        navigateUp = navigateUp
    )
}

@Composable
fun EmailVerification(
    viewModel:EmailVerificationViewModel,
    navigateUp: () -> Unit
){
    val state by viewModel.state.collectAsState()
    EmailVerification(
        viewState = state,
        navigateUp = navigateUp,
        clearMessage = viewModel::clearMessage,
        verifyEmail = viewModel::verifyEmail
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EmailVerification(
    viewState: EmailVerificationState,
    navigateUp: () -> Unit,
    clearMessage:(Long)->Unit,
    verifyEmail:(String,()->Unit,Context)->Unit
){
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    var otpValue by remember {
        mutableStateOf("")
    }
    var enableButton by remember {
        mutableStateOf(false)
    }
    viewState.message?.let {message->
        LaunchedEffect(key1 = message, block = {
            snackbarHostState.showSnackbar(message.message)
            clearMessage(message.id)
        })
    }
    LoaderDialog(loading = viewState.loading)
    Scaffold(topBar = {
        SimpleTopBar(navigateUp = navigateUp,
            title = stringResource(id = R.string.email_verification))
    },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState)}
    ) {paddingValues ->
        Column(modifier = Modifier
            .padding(10.dp)
            .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(
                id = R.string.email_verification_help_text,
                viewState.user?.email?:""),
                style = MaterialTheme.typography.labelMedium
            )
            Spacer(modifier = Modifier.height(20.dp))
            OtpTextField(
                otpText = otpValue,
                onOtpTextChange = { value, isOtpFilled ->
                    enableButton = isOtpFilled
                    otpValue = value
                },
                otpCount = 5,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = {
                focusManager.clearFocus(true)
                verifyEmail(otpValue,navigateUp,context)
                             },
            enabled = enableButton) {
                Text(text = stringResource(id = R.string.verify))
            }
        }
    }
}
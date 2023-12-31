package app.regate.auth.signup.emailverification
import android.content.Context
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
import androidx.compose.material3.TextButton
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
import app.regate.common.composes.component.dialog.DialogConfirmation
import app.regate.common.composes.component.dialog.LoaderDialog
import app.regate.common.composes.component.input.OtpTextField
import app.regate.common.composes.ui.SimpleTopBar
import app.regate.common.composes.viewModel
import kotlinx.coroutines.delay
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

typealias EmailVerification = @Composable (
    navigateUp:()->Unit,
    navigateToHomeScreen:()->Unit
        )->Unit

@Inject
@Composable
fun EmailVerification(
    viewModelFactory:()->EmailVerificationViewModel,
    @Assisted navigateUp: () -> Unit,
    @Assisted navigateToHomeScreen: () -> Unit,
){
    EmailVerification(
        viewModel = viewModel(factory = viewModelFactory),
        navigateUp = navigateUp,
        navigateToHomeScreen = navigateToHomeScreen
    )
}

@Composable
fun EmailVerification(
    viewModel:EmailVerificationViewModel,
    navigateUp: () -> Unit,
    navigateToHomeScreen: () -> Unit,
){
    val state by viewModel.state.collectAsState()
    EmailVerification(
        viewState = state,
        navigateUp = navigateUp,
        clearMessage = viewModel::clearMessage,
        verifyEmail = viewModel::verifyEmail,
        resendEmail = viewModel::resendEmail,
        navigateToHomeScreen = navigateToHomeScreen
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EmailVerification(
    viewState: EmailVerificationState,
    navigateUp: () -> Unit,
    clearMessage:(Long)->Unit,
    resendEmail:()->Unit,
    verifyEmail:(String,()->Unit,Context)->Unit,
    navigateToHomeScreen: () -> Unit
){
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    var openDialogResendEmail by remember{
        mutableStateOf(false)
    }
    var otpValue by remember {
        mutableStateOf("")
    }
    var enableButton by remember {
        mutableStateOf(false)
    }
    var fireTrigger by remember{ mutableStateOf(true) }
    var ticks by remember { mutableStateOf(10) }
    LaunchedEffect(key1 = fireTrigger, block = {
        while (ticks >= 1 ){
            delay(1000)
            ticks--
        }
    })
    viewState.message?.let {message->
        LaunchedEffect(key1 = message, block = {
            snackbarHostState.showSnackbar(message.message)
            clearMessage(message.id)
        })
    }
    DialogConfirmation(open = openDialogResendEmail,
        dismiss = { openDialogResendEmail = false }, confirm = {
            resendEmail()
            openDialogResendEmail = false
            ticks = 10
            fireTrigger = !fireTrigger
        },
    descripcion = stringResource(id = R.string.resend_email_verification,viewState.user?.email?:""))
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
                verifyEmail(otpValue,navigateToHomeScreen,context)
                             },
            enabled = enableButton) {
                Text(text = stringResource(id = R.string.verify))
            }
            
            TextButton(onClick = { openDialogResendEmail = true }, enabled = ticks == 0) {
                Text(text = stringResource(id = R.string.dont_receive_code))
            }
            if(ticks != 0){
            Text(text = stringResource(id = R.string.you_may_request_a_new_code,ticks),
            style =MaterialTheme.typography.labelMedium)
            }
        }
    }
}
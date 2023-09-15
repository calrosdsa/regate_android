package app.regate.bottom.auth


import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.regate.common.compose.util.Layout
import app.regate.common.compose.viewModel
import app.regate.common.resources.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject


typealias BottomAuth = @Composable (
    navigateUp:()->Unit,
    navigateToSignUp:()->Unit,
    navigateToLogin:()->Unit
) -> Unit

@Inject
@Composable
fun BottomAuth(
    viewModelFactory:()-> BottomAuthViewModel,
    @Assisted navigateUp: () -> Unit,
    @Assisted navigateToSignUp: () -> Unit,
    @Assisted navigateToLogin:()->Unit,
){
    BottomAuth(
        viewModel = viewModel(factory = viewModelFactory),
        navigateUp = navigateUp,
        navigateToLogin = navigateToLogin,
        navigateToSignUp = navigateToSignUp,
//        navigateToReserva = navigateToReserva
    )
}


@Composable
internal fun BottomAuth(
    viewModel: BottomAuthViewModel,
    navigateUp: () -> Unit,
    navigateToLogin: () -> Unit,
    navigateToSignUp: () -> Unit,
//    navigateUp: () -> Unit,
//    navigateToReserva: () -> Unit,
){
    val state by viewModel.state.collectAsState()
//    val formatter = LocalAppDateFormatter.current
    BottomAuth(
        viewState = state,
//        navigateUp = navigateUp,
        onMessageShown = viewModel::clearMessage,
        navigateToLogin = navigateToLogin,
        navigateToSignUp = navigateToSignUp,
        handleGoogleAuthResult = {viewModel.handleSignInResult(it,navigateUp)},
        googleSignInClient = viewModel.googleClient
//        openAuthDialog = openAuthDialog,
//        navigateToReserva = navigateToReserva,
//        openBottomSheet = { viewModel.openBottomSheet { navigateToReserva () } }
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun BottomAuth(
    viewState: AuthBottomState,
    onMessageShown:(id:Long) -> Unit,
    navigateToLogin: () -> Unit,
    navigateToSignUp: () -> Unit,
    handleGoogleAuthResult:(task:Task<GoogleSignInAccount>)->Unit,
    googleSignInClient: GoogleSignInClient
//    openAuthDialog: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val dismissSnackbarState = rememberDismissState(
        confirmValueChange = { value ->
            if (value != DismissValue.Default) {
                snackbarHostState.currentSnackbarData?.dismiss()
                true
            } else {
                false
            }
        },
    )
    val startForResult =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
//            if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            if (result.data != null) {
                val task: Task<GoogleSignInAccount> =
                    GoogleSignIn.getSignedInAccountFromIntent(intent)
                handleGoogleAuthResult(task)
            }
//            }
        }

    viewState.message?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message.message)
            // Notify the view model that the message has been dismissed
            onMessageShown(message.id)
        }
    }

    Box() {
        SnackbarHost(hostState = snackbarHostState) { data ->
            SwipeToDismiss(
                state = dismissSnackbarState,
                background = {},
                dismissContent = { Snackbar(snackbarData = data) },
                modifier = Modifier
                    .padding(horizontal = Layout.bodyMargin)
                    .fillMaxWidth(),
            )
        }
        Column(
            modifier = Modifier.padding(horizontal = 15.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.Center
        ) {

//            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_app),
                    contentDescription = "logo", modifier = Modifier.size(25.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "Leafboard", style = MaterialTheme.typography.titleMedium)
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Iniciar session/Registrarte para continuar",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(20.dp))

            TextButton(
                onClick = {
                    startForResult.launch(googleSignInClient.signInIntent)
                }, modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp),
                shape = CircleShape,
                border = BorderStroke(1.dp, Color.LightGray)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.icon_google),
                        contentDescription = "logo", modifier = Modifier.size(25.dp)
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(text = "Continuar con Google", color = MaterialTheme.colorScheme.primary)

                }
            }


            Spacer(modifier = Modifier.height(15.dp))

            TextButton(
                onClick = { navigateToLogin() }, modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp),
                shape = CircleShape,
                border = BorderStroke(1.dp, Color.LightGray)

            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
//                    Image(
//                        painter = painterResource(id = R.drawable.icon_mac),
//                        contentDescription = "logo", modifier = Modifier.size(25.dp)
//                    )
                    Icon(imageVector = Icons.Default.Email, contentDescription = null,
                    modifier = Modifier.size(25.dp))
                    Spacer(modifier = Modifier.width(15.dp))
                    Text(text = "Continuar con Email", color = MaterialTheme.colorScheme.primary)

                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            TextButton(onClick = { navigateToSignUp() }) {
                Text(
                    text = "Crear Cuenta", style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary, textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}





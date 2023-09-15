package app.regate.auth.signin

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.RemoveRedEye
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import app.regate.common.compose.components.input.CustomOutlinedTextInput
import app.regate.common.compose.viewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import app.regate.common.resources.R


typealias Login = @Composable (
    navigateToSignUpScreen:() -> Unit,
    navigateToHomeScreen:() ->Unit,
) -> Unit

//@OptIn(ExperimentalTextApi::class)
@Inject
@Composable
fun Login(
    viewModelFactory:()-> AuthViewModel,
    @Assisted navigateToSignUpScreen:()->Unit,
    @Assisted navigateToHomeScreen:()->Unit
) {
    Login(
        viewModel = viewModel(factory = viewModelFactory),
        navigateToSignUpScreen = navigateToSignUpScreen,
        navigateToHomeScreen = navigateToHomeScreen
    )
}

@Composable
internal fun Login(
    viewModel: AuthViewModel,
    navigateToSignUpScreen: () -> Unit,
    navigateToHomeScreen: () -> Unit
) {
    var email by remember { mutableStateOf("jorge@gmail.com") }
    var password by remember { mutableStateOf("201120") }
    val viewState by viewModel.state.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val focusManager = LocalFocusManager.current

    val startForResult =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
//            if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            if (result.data != null) {
                val task: Task<GoogleSignInAccount> =
                    GoogleSignIn.getSignedInAccountFromIntent(intent)
                viewModel.handleSignInResult(task, navigateToHomeScreen)
            }
//            }
        }
    if (viewState.loading) {
        Dialog(onDismissRequest = { /*TODO*/ }) {
//        Text(text = "Title")
            CircularProgressIndicator()
        }
    }
    viewState.message?.let { message->
        LaunchedEffect(key1 = message, block = {
            snackbarHostState.showSnackbar(message.message)
            viewModel.clearMessage(message.id)
        })
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) {padding->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
//                .padding(top = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = app.regate.common.resources.R.drawable.logo_app),
                    contentDescription = "logo", modifier = Modifier.size(25.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = stringResource(id = R.string.name), style = MaterialTheme.typography.titleLarge)
            }

            Text(
                text = "Reserva juega comparte", style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 20.dp)
            )


            Spacer(modifier = Modifier.height(25.dp))

            Column {
                Text(text = "Tu email", style = MaterialTheme.typography.labelLarge)
                Spacer(modifier = Modifier.height(5.dp))
                CustomOutlinedTextInput(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = "jan@gmail.com",
                    icon = Icons.Outlined.Email,
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Next)
                        }
                    ),
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Email
                )
            }

            Spacer(modifier = Modifier.height(25.dp))

            Column {

                Text(text = "Tu contrasena", style = MaterialTheme.typography.labelLarge)
                Spacer(modifier = Modifier.height(5.dp))
                CustomOutlinedTextInput(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = "*********",
                    icon = Icons.Outlined.RemoveRedEye,
                    keyboardActions = KeyboardActions(
                        onDone = {
                            viewModel.login(email, password, navigateToHomeScreen,context)
                        }
                    ),
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Password
                )
            }

//            TextButton(onClick = { /*TODO*/ }) {
//
//            }

            Spacer(modifier = Modifier.height(25.dp))

            Button(
                onClick = {
                    viewModel.login(email, password, navigateToHomeScreen,context)
                }, modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp)
                    .clip(shape = CircleShape)
            ) {
                Text(text = "Continuar")
            }
            Spacer(modifier = Modifier.height(25.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Canvas(
                    modifier = Modifier
                        .fillMaxWidth(0.47f)
                        .height(1.dp)
                        .padding(end = 10.dp)
                ) {

                    drawLine(
                        color = Color.LightGray,
                        start = Offset(0f, 0f),
                        end = Offset(size.width, 0f),
                        strokeWidth = 0.6.dp.toPx()
                    )


                }
                Text(text = "or", color = Color.LightGray)
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .height(1.dp)
                        .padding(start = 10.dp)
                ) {

                    drawLine(
                        color = Color.LightGray,
                        start = Offset(0f, 0f),
                        end = Offset(size.width, 0f),
                        strokeWidth = 0.6.dp.toPx()
                    )


                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            TextButton(
                onClick = {
                    startForResult.launch(viewModel.googleClient.signInIntent)
//                startForResult.launch(1)

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
                        painter = painterResource(id = app.regate.common.resources.R.drawable.icon_google),
                        contentDescription = "logo", modifier = Modifier.size(25.dp)
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(text = "Continuar con Google", color = MaterialTheme.colorScheme.primary)

                }
            }


            Spacer(modifier = Modifier.height(20.dp))

            Row() {
                Text(
                    text = "No te has registrado aun? ",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "Crear una cuenta ",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.clickable {
                        navigateToSignUpScreen()
                    }
                )
            }
        }
    }
}

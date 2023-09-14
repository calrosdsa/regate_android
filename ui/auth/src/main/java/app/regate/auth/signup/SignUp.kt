package app.regate.auth.signup

import android.content.Context
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import app.regate.common.composes.components.dialog.LoaderDialog
import app.regate.common.composes.components.input.CustomOutlinedTextInput
import app.regate.common.composes.ui.SimpleTopBar
import app.regate.common.resources.R
import app.regate.common.composes.viewModel
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

typealias SignUp= @Composable (
    onBack:()->Unit,
    navigateToVerificationEmail:()->Unit,
//    navigateToSignUpScreen:() -> Unit,
) -> Unit

@Inject
@Composable
fun SignUp(
    viewModelFactory:()-> SignUpViewModel,
    @Assisted onBack: () -> Unit,
    @Assisted navigateToVerificationEmail: () -> Unit
    ){
    SignUp(
        viewModel =viewModel(factory = viewModelFactory),
        onBack = {onBack()},
        navigateToVerificationEmail = navigateToVerificationEmail
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SignUp(
    viewModel: SignUpViewModel,
    onBack: () -> Unit,
    navigateToVerificationEmail: () -> Unit
) {
    val viewState by viewModel.state.collectAsState()
//    val pagerState = rememberPagerState()
//    val coroutine = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    viewState.message?.let { message ->
        LaunchedEffect(key1 = message, block = {
            snackbarHostState.showSnackbar(message.message)
            viewModel.clearMessage(message.id)
        })
    }
    
    LoaderDialog(loading = viewState.loading)

    Scaffold(
        topBar = {
                 SimpleTopBar(navigateUp = onBack)
        },
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {paddingValues->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)){
        SignUp(
            signUp = viewModel::signUp,
            navigateToVerificationEmail = navigateToVerificationEmail
        )

        }
    }

}

@Composable
fun ProfileSignup(
    title:String,
    modifier:Modifier =  Modifier,
    content:@Composable () -> Unit){
    Box(modifier = modifier
        .fillMaxSize()
        .padding(30.dp)) {
        Column() {

        Spacer(modifier = Modifier.height(50.dp))
        Image(
            painter = painterResource(id = R.drawable.logo_app),
            contentDescription = "logo", modifier = Modifier.size(25.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "Vamos a crear tu perfil",style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(40.dp))

            content()
        }


        Row(modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 20.dp), verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = R.drawable.logo_app),
            contentDescription = "logo", modifier = Modifier
                .size(25.dp)
        )
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "Regate", style = MaterialTheme.typography.titleLarge)
        }
//        }
//        }
    }
}

@Composable
fun SignUp(
    signUp:(username:String,email:String,password:String,context: Context,navigate:()->Unit)->Unit,
    navigateToVerificationEmail: () -> Unit,
    modifier :Modifier = Modifier
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    var username by remember { mutableStateOf("jorge") }
    var email by remember { mutableStateOf("carlosduram71@gmail.com") }
    var password by remember { mutableStateOf("12ab34cd56ef") }
    var confirmPassword by remember { mutableStateOf("12ab34cd56ef") }
    var passwordError by remember{ mutableStateOf(false) }
    var passwordErrorMessage by remember{ mutableStateOf<String?>(null) }

    LaunchedEffect(key1 = password,key2 = confirmPassword) {
        if(password.length <= 8){
//            passwordError = true
            passwordErrorMessage = context.getString(R.string.password_length)
            return@LaunchedEffect
        }
        if(password != confirmPassword && confirmPassword.length > 1){
//            showMessage(context.getString(R.string.password_dont_match))
            passwordError = true
            passwordErrorMessage = context.getString(R.string.password_dont_match)
        }else{
            passwordError = false
            passwordErrorMessage = null
        }
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
//            .background(Color.White)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.logo_app),
                contentDescription = "logo", modifier = Modifier.size(25.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "Leafboard", style = MaterialTheme.typography.titleLarge)
        }

        Text(
            text = "Work without limits", style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(vertical = 20.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Column {
            Text(text = "Nombre de Usuario", style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(1.dp))
            CustomOutlinedTextInput(
                value = username,
                onValueChange = { username = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = "username",
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Next)
                    }
                ),
                imeAction = ImeAction.Next
//                icon = Icons.Outlined.Email
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
        Column {
            Text(text = "Tu email", style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(1.dp))
            CustomOutlinedTextInput(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = "jan@gmail.com",
                icon = Icons.Outlined.Email,
                keyboardType = KeyboardType.Email,
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Next)
                    }
                ),
                imeAction = ImeAction.Next
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
        Column {
            Text(text = "Contraseña", style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(1.dp))
            CustomOutlinedTextInput(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = "*********",
                icon = Icons.Outlined.LockOpen,
                keyboardType = KeyboardType.Password,
                supportText = passwordErrorMessage,
                isError = passwordError,
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Next)
                    }
                ),
                imeAction = ImeAction.Next
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
        Column {
            Text(text = "Confirmar contraseña", style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(1.dp))
            CustomOutlinedTextInput(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = "*********",
                icon = Icons.Outlined.Lock,
                keyboardType = KeyboardType.Password,
                supportText = passwordErrorMessage,
                isError = passwordError,
                keyboardActions = KeyboardActions(
                    onDone = {
                        signUp(username, email, password, context,navigateToVerificationEmail)
                    }
                ),
                imeAction = ImeAction.Done
            )
        }

        Spacer(modifier = Modifier.height(25.dp))


        Button(
            onClick = {
                      signUp(username, email, password, context,navigateToVerificationEmail)
            }, modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .clip(shape = CircleShape),
            enabled = true
        ) {
            Text(text = "Continuar")
        }
    }
}
package app.regate.auth.signup

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import app.regate.common.composes.components.input.CustomOutlinedTextInput
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun SignUp(
    viewModel: SignUpViewModel,
    onBack: () -> Unit,
    navigateToVerificationEmail: () -> Unit
){
    val viewState by viewModel.state.collectAsState()
    val pagerState = rememberPagerState()
    val coroutine = rememberCoroutineScope()
//    val context = LocalContext.current
    BackHandler(enabled = true) {
        if(pagerState.currentPage == 0) {
//            (context as? Activity)?.finish()
            onBack()
        }
        coroutine.launch {
            pagerState.animateScrollToPage(pagerState.currentPage -1)
        }
    }
    Scaffold(modifier = Modifier.fillMaxSize()) {
//                  ProfileSignup()
        HorizontalPager(pageCount = 10,state=pagerState,
        userScrollEnabled = false, modifier = Modifier.fillMaxSize().padding(it)) {page ->
//            Column(modifier = Modifier.fillMaxSize()) {
                when(page){
                    0 -> SignUp(goToSecondPage = {
                        navigateToVerificationEmail()
                    }
//                        coroutine.launch {
//                            pagerState.animateScrollToPage(1)}
//                        }
                    )
                    1 -> ProfileSignup(title = "Fecha de Nacimiento"){
                        SelectBirthDay(navigateTab = {
                            coroutine.launch {
                                pagerState.animateScrollToPage(it)
                            }
                        })
                    }
                    2 -> ProfileSignup(title = "Selecciona tu Genero"){
                        SelectGenero(
                            genero = viewState.genero,
                            selectGenero = {viewModel.setGenero(it)},
                            navigateTab = {
                                coroutine.launch {
                                    pagerState.animateScrollToPage(it)
                                }
                            },
                            goHome = { onBack()}
                        )
                    }
                    else -> Text(text = "Default")
//                }
//                Text(text = "Hello $page")
//                SignUp()
            }
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
    goToSecondPage:()->Unit,
    modifier :Modifier = Modifier
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    LaunchedEffect(key1 = username, key2 = email, key3 = password) {
        Log.d("DEBUG_APP", "called")
    }


    Column(
        modifier = modifier
            .fillMaxSize()
//            .background(Color.White)
            .padding(horizontal = 20.dp)
            .padding(top = 40.dp),
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
        Spacer(modifier = Modifier.height(15.dp))
        Column {
            Text(text = "Nombre de Usuario", style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(5.dp))
            CustomOutlinedTextInput(
                value = username,
                onValueChange = { username = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = "username",
//                icon = Icons.Outlined.Email
            )
        }

        Spacer(modifier = Modifier.height(15.dp))
        Column {
            Text(text = "Tu email", style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(5.dp))
            CustomOutlinedTextInput(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = "jan@gmail.com",
                icon = Icons.Outlined.Email,
                keyboardType = KeyboardType.Email
            )
        }

        Spacer(modifier = Modifier.height(15.dp))
        Column {
            Text(text = "Contraseña", style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(5.dp))
            CustomOutlinedTextInput(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = "*********",
                icon = Icons.Outlined.LockOpen,
                keyboardType = KeyboardType.Password
            )
        }

        Spacer(modifier = Modifier.height(15.dp))
        Column {
            Text(text = "Confirmar contraseña", style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(5.dp))
            CustomOutlinedTextInput(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = "*********",
                icon = Icons.Outlined.Lock,
                keyboardType = KeyboardType.Password
            )
        }

        Spacer(modifier = Modifier.height(25.dp))


        Button(
            onClick = { goToSecondPage() }, modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .clip(shape = CircleShape),
            enabled = true
        ) {
            Text(text = "Continuar")
        }
    }
}
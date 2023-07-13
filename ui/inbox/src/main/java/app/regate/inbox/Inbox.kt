package app.regate.inbox

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import app.regate.common.composes.ui.BottomBar
import app.regate.common.composes.ui.TopBar
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

typealias Inbox = @Composable (
    navController: NavController,
//    navigateToReserva:(id:Long)->Unit,
//    navigateToSignUpScreen:() -> Unit,
) -> Unit

@Inject
@Composable
fun Inbox (
    @Assisted navController: NavController,
//    @Assisted navigateToReserva: (id:Long) -> Unit,
//    viewModelFactory:()->ReservasViewModel
){
    Scaffold(
//        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar()
        },
        bottomBar = {
            BottomBar(navController = navController)
        }
    ) {paddingValue->
        Box(modifier = Modifier.padding(paddingValue)){
            Text(text = "Servicios")
        }
    }
}
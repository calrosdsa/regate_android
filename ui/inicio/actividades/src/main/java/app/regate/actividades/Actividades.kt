package app.regate.actividades

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import app.regate.common.compose.ui.BottomBar
import app.regate.common.compose.ui.TopBar
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

typealias Actividades = @Composable (
    navController: NavController,
//    navigateToReserva:(id:Long)->Unit,
//    navigateToSignUpScreen:() -> Unit,
) -> Unit

@Inject
@Composable
fun Actividades (
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
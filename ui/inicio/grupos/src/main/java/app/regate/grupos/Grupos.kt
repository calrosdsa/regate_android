package app.regate.grupos

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import app.regate.common.composes.ui.BottomBar
import app.regate.common.composes.ui.PosterCardImage
import app.regate.common.composes.ui.TopBar
import app.regate.common.composes.viewModel
import app.regate.constant.Route
import app.regate.constant.id
import app.regate.models.Grupo
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

typealias Grupos = @Composable (
    navController: NavController,
//    navigateToReserva:(id:Long)->Unit,
//    navigateToSignUpScreen:() -> Unit,
) -> Unit

@Inject
@Composable
fun Grupos (
    viewModelFactory:()->GruposViewModel,
    @Assisted navController: NavController,
//    @Assisted navigateToReserva: (id:Long) -> Unit,
//    viewModelFactory:()->ReservasViewModel
){
    Grupos(navController = navController,
        viewModel = viewModel(factory = viewModelFactory)
    )
}


@Composable
internal fun Grupos(
    navController: NavController,
    viewModel:GruposViewModel
) {
    val viewState by viewModel.state.collectAsState()
    Scaffold(
//        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar()
        },
        bottomBar = {
            BottomBar(navController = navController)
        }
    ) { paddingValue ->
        Grupos(
            modifier = Modifier.padding(paddingValue),
            viewState = viewState,
            navigateToGrupoChat = {navController.navigate(Route.CHAT_SALA id  it)}
        )
    }
}

@Composable
internal fun Grupos(
    viewState:GruposState,
    navigateToGrupoChat:(id:Long)->Unit,
    modifier:Modifier = Modifier
){
    Column(modifier = modifier) {

        viewState.grupos.map{
            GrupoItem(grupo = it,navigateToChatGrupo = navigateToGrupoChat)
        }
    }
}

@Composable
internal fun GrupoItem(
    grupo:Grupo,
    navigateToChatGrupo: (id: Long) -> Unit,
    modifier:Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth().clickable { navigateToChatGrupo(grupo.id) }.padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PosterCardImage(
            model = grupo.photo, modifier = Modifier
                .size(70.dp), shape = CircleShape
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = grupo.name, style = MaterialTheme.typography.titleMedium)
    }
}

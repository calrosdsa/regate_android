package app.regate.favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import app.regate.common.composes.components.item.EstablecimientoItem
import app.regate.common.composes.ui.BottomBar
import app.regate.common.composes.ui.TopBar
import app.regate.common.composes.viewModel
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

typealias Favorites = @Composable (
    navigateUp:()->Unit,
    navigateToEstablecimiento:(Long)->Unit
//    navigateToReserva:(id:Long)->Unit,
//    navigateToSignUpScreen:() -> Unit,
) -> Unit



@Inject
@Composable
fun Favorites (
    viewModelFactory:()->FavoritesViewModel,
    @Assisted navigateUp: () -> Unit,
    @Assisted navigateToEstablecimiento: (Long) -> Unit
){
    Favorites(
        viewModel = viewModel(factory = viewModelFactory),
        navigateUp = navigateUp,
        navigateToEstablecimiento = navigateToEstablecimiento
    ) 
}

@Composable
internal fun Favorites(
    viewModel:FavoritesViewModel,
    navigateUp:()->Unit,
    navigateToEstablecimiento: (Long) -> Unit,
){
    val state by viewModel.state.collectAsState()
    Favorites(
        viewState = state,
        navigateUp = navigateUp,
        navigateToEstablecimiento = navigateToEstablecimiento
    ) 
}

@Composable
internal fun Favorites(
    viewState:FavoritesState,
    navigateUp: () -> Unit,
    navigateToEstablecimiento: (Long) -> Unit
){
    Scaffold(
        topBar = {
            IconButton(onClick = { navigateUp() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
        }
    ) {paddingValues ->  
        Box(modifier = Modifier.padding(paddingValues)){
            LazyColumn(){
             items(
                 items = viewState.establecimientos
             ){establecimiento->
            EstablecimientoItem(name = establecimiento.name, photo = establecimiento.photo){
                navigateToEstablecimiento(establecimiento.id)
            }
                 Divider()
             }
            }

        }
    }
}
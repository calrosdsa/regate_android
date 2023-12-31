package app.regate.favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import app.regate.common.composes.component.item.EstablecimientoItem
import app.regate.common.composes.ui.SimpleTopBar
import app.regate.common.composes.viewModel
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import app.regate.common.resources.R

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Favorites(
    viewState:FavoritesState,
    navigateUp: () -> Unit,
    navigateToEstablecimiento: (Long) -> Unit
){
    Scaffold(
        topBar = {
          SimpleTopBar(navigateUp = navigateUp,
          title = stringResource(id = R.string.favorites))
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
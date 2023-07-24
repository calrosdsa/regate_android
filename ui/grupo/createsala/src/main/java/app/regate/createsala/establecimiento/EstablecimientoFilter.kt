package app.regate.createsala.establecimiento

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import app.regate.common.composes.components.item.EstablecimientoItem
import app.regate.common.composes.ui.PosterCardImage
import app.regate.common.composes.viewModel
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

typealias EstablecimientoFilter = @Composable (
    navigateUp:()->Unit,
    navigateToCreateSala:(establecimientoId:Long,grupoId:Long)->Unit
) -> Unit

@Inject
@Composable
fun EstablecimientoFilter(
    viewModelFactory:(SavedStateHandle)->EstablecimientoFilterViewModel,
    @Assisted navigateUp: () -> Unit,
    @Assisted navigateToCreateSala: (establecimientoId: Long,grupoId:Long) -> Unit
){
    EstablecimientoFilter(
        viewModel = viewModel(factory = viewModelFactory),
        navigateToCreateSala = navigateToCreateSala,
        navigateUp = navigateUp
    )
}

@Composable
internal fun EstablecimientoFilter(
    viewModel: EstablecimientoFilterViewModel,
    navigateToCreateSala: (establecimientoId: Long,grupoId:Long) -> Unit,
    navigateUp: () -> Unit
) {
    val viewState by viewModel.state.collectAsState()

    EstablecimientoFilter(
        viewState = viewState,
        navigateUp = navigateUp,
        navigateToCreateSala = {
            val grupoId = viewModel.geIdGroup()
            if(grupoId != null){
            navigateToCreateSala(it,grupoId)
            }
        }
    )
}

@Composable
internal fun EstablecimientoFilter(
    viewState:EstablecimientoFilterState,
    navigateUp:()->Unit,
    navigateToCreateSala: (establecimientoId: Long) -> Unit
) {
    Scaffold(
        topBar = {
            IconButton(onClick = { navigateUp() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "arrow_back_")
            }
        }
    ) {paddingValues->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(
                items = viewState.establecimientos,
                key = { it.id }
            ) { establecimiento ->

//                Row(modifier = Modifier
//                    .fillMaxWidth()
//                    .clickable { navigateToCreateSala(establecimiento.id.toLong()) }
//                    .padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
//                    PosterCardImage(
//                        model = establecimiento.photo, modifier = Modifier
//                            .width(100.dp)
//                            .height(70.dp)
//                    )
//                    Spacer(modifier = Modifier.width(10.dp))
//                    Text(text = establecimiento.name, style = MaterialTheme.typography.titleMedium)
//                }
                EstablecimientoItem(name = establecimiento.name, photo = establecimiento.photo){
                    navigateToCreateSala(establecimiento.id.toLong())
                }
                Divider()
            }
        }
    }
}

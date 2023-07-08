package app.regate.entidad.salas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import app.regate.common.composes.LocalAppDateFormatter
import app.regate.common.composes.components.item.SalaItem
import app.regate.common.composes.viewModel
import app.regate.data.dto.empresa.salas.SalaDto
import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Assisted

import me.tatarka.inject.annotations.Inject

typealias Salas = @Composable (
    navigateToSala: (id:Long) ->Unit,
    crearSala:(id:Long)->Unit
) -> Unit

@Inject
@Composable
fun Salas(
    viewModelFactory:(SavedStateHandle)-> SalasViewModel,
    @Assisted navigateToSala: (id:Long) -> Unit,
    @Assisted crearSala: (id:Long) -> Unit
){
    Salas(
        viewModel = viewModel(factory = viewModelFactory),
        navigateToSala = navigateToSala,
        crearSala = crearSala
    )
}

@Composable
internal fun Salas(
    viewModel: SalasViewModel,
    navigateToSala: (id:Long) -> Unit,
    crearSala: (id:Long) -> Unit
){
    val viewState by viewModel.state.collectAsState()
    val formatter = LocalAppDateFormatter.current

    Salas(
        viewState = viewState,
        navigateToSala = navigateToSala,
        crearSala = {crearSala(viewModel.getEstablecimientoId())},
        formatShortTime = {formatter.formatShortTime(it)},
        formatDate = {formatter.formatWithSkeleton(it.toEpochMilliseconds(),formatter.monthDaySkeleton)},
  )
}

@Composable
internal fun Salas(
    viewState: SalasState,
    navigateToSala: (id:Long) -> Unit,
    formatShortTime:(time: Instant)->String,
    formatDate:(date: Instant)->String,
    crearSala: () -> Unit
){


    Scaffold(modifier = Modifier.padding(10.dp)) {paddingValues->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            TextButton(onClick = { crearSala() }) {
                Text(text = "Crear una sala",style = MaterialTheme.typography.titleSmall)
            }
            Spacer(modifier = Modifier.height(10.dp))
            LazyColumn(){
                items(items = viewState.salas,
                    key = { it.id}){sala->
                    SalaItem(
                        sala = sala,
                        navigateToSala = navigateToSala,
                        formatDate = formatDate,
                        formatShortTime = formatShortTime
                    )
                    Divider(modifier = Modifier.padding(vertical = 2.dp))
                }
            }
        }
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun SalaItem(
//    sala: SalaDto,
//    navigateToSala: (id:Long) -> Unit,
//    modifier:Modifier = Modifier,
//){
//    ElevatedCard(onClick = { navigateToSala(sala.id) }, modifier = modifier.padding(10.dp)) {
//        Column(modifier = Modifier.padding(10.dp)) {
////        AsyncImage(model = "", contentDescription = )
//            Text(text = sala.titulo,style = MaterialTheme.typography.titleSmall)
//            Text(text = sala.descripcion,style = MaterialTheme.typography.bodySmall,maxLines =2)
//            Spacer(modifier = Modifier.height(5.dp))
//            Text(text = "Cupos",style = MaterialTheme.typography.labelMedium)
//            Text(text = "4/${sala.cupos}",style = MaterialTheme.typography.labelSmall)
//        }
//
//    }
//}
package app.regate.entidad.salas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import app.regate.common.composes.LocalAppDateFormatter
import app.regate.common.composes.components.item.SalaItem
import app.regate.common.composes.components.skeleton.SalaItemSkeleton
import app.regate.common.composes.viewModel
import app.regate.data.dto.empresa.salas.SalaDto
import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Assisted

import me.tatarka.inject.annotations.Inject
import app.regate.common.resources.R

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
        getSalas = viewModel::getSalas
  )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun Salas(
    viewState: SalasState,
    navigateToSala: (id:Long) -> Unit,
    formatShortTime:(time: Instant)->String,
    formatDate:(date: Instant)->String,
    crearSala: () -> Unit,
    getSalas:()->Unit,
){
    val refreshState = rememberPullRefreshState(refreshing = false, onRefresh = { getSalas()})
    Scaffold(modifier = Modifier.padding(10.dp)) {paddingValues->
        Box(modifier = Modifier
            .pullRefresh(refreshState)
            .fillMaxSize()
            .padding(paddingValues)) {

            Spacer(modifier = Modifier.height(10.dp))
            LazyColumn(){
                item {
                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(text = stringResource(id = R.string.rooms),style = MaterialTheme.typography.titleLarge)
                        TextButton(onClick = { crearSala() }) {
                            Text(text = "Crear una sala",style = MaterialTheme.typography.titleSmall)
                        }
                    }
                }
                if(viewState.loading) {
                    items(5){
                        SalaItemSkeleton()
                        Divider()
                    }
                }else{
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
            PullRefreshIndicator(
                refreshing = viewState.loading,
                state = refreshState,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(paddingValues)
                    .padding(top = 20.dp),
                scale = true,
            )
        }
    }
}

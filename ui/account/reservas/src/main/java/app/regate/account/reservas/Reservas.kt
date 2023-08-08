package app.regate.account.reservas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.regate.common.composes.LocalAppDateFormatter
import app.regate.common.composes.ui.CommonTopBar
import app.regate.common.composes.ui.SimpleTopBar
import app.regate.common.composes.viewModel
import app.regate.data.dto.account.reserva.ReservaDto
import app.regate.discover.ReservasState
import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import app.regate.common.resources.R

typealias Reservas = @Composable (
    navigateUp:()->Unit,
    navigateToReserva:(id:Long)->Unit,
//    navigateToSignUpScreen:() -> Unit,
) -> Unit

@Inject
@Composable
fun Reservas (
    @Assisted navigateUp: () -> Unit,
    @Assisted navigateToReserva: (id:Long) -> Unit,
    viewModelFactory:()-> ReservasViewModel
){

        Reservas(
            viewModel = viewModel(factory = viewModelFactory),
            navigateToReserva = navigateToReserva,
            navigateUp = navigateUp

        )
}
@Composable
internal fun Reservas(
    viewModel: ReservasViewModel,
    navigateToReserva: (id: Long) -> Unit,
    navigateUp: () -> Unit
){
    val viewState by viewModel.state.collectAsState()
    val formatter = LocalAppDateFormatter.current
    Reservas(
        viewState = viewState,
        formatterDate = formatter::formatMediumDateTime,
        formatterDateReserva = {start,end->
            val date = formatter.formatShortDate(start)
            "$date de ${formatter.formatShortTime(start)} a ${formatter.formatShortTime(end)}"
        },
        navigateToReserva = navigateToReserva,
        navigateUp = navigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Reservas(
    viewState: ReservasState,
    formatterDate:(Instant)->String,
    formatterDateReserva:(start:Instant,end:Instant)->String,
    navigateToReserva: (id: Long) -> Unit,
    navigateUp: () -> Unit
    ) {
    Scaffold(
//        modifier = Modifier.fillMaxSize(),
        topBar = {
            SimpleTopBar(navigateUp = navigateUp, title = stringResource(id = R.string.bookings))
        },
    ) { paddingValue ->
        LazyColumn(modifier = Modifier.padding(paddingValue)) {
            items(
                items = viewState.reservas,
                key = { it.id }
            ) { reserva ->
                ReservaItem(
                    item = reserva,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navigateToReserva(reserva.id) }
                        .padding(10.dp),
                    formatterDate = formatterDate,
                    formatterDateReserva = formatterDateReserva
                )
                Divider()
            }
        }
    }
}

@Composable
fun ReservaItem(
    item:ReservaDto,
    formatterDate: (Instant) -> String,
    formatterDateReserva:(Instant,Instant)->String,
    modifier:Modifier =Modifier
) {
    Column(modifier = modifier) {
        item.instalacion_name?.let {
            Text(text = it, style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary)
        }
        Text(
            text = "Hora de la reserva",
            style = MaterialTheme.typography.labelLarge
        )
        Text(text = formatterDateReserva(item.start_date,item.end_date),style = MaterialTheme.typography.labelMedium)
        Row(verticalAlignment = Alignment.CenterVertically,modifier = Modifier
            .fillMaxWidth()
            .padding(top = 3.dp)) {
            Icon(imageVector = Icons.Default.Update, contentDescription = item.created_at.toString(),
            modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(5.dp))
        Text(text = formatterDate(item.created_at),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.inverseSurface
        )
        }
    }
}




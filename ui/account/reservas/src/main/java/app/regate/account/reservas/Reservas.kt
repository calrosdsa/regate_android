package app.regate.account.reservas

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import app.regate.common.composes.LocalAppDateFormatter
import app.regate.common.composes.component.text.DateTextWithIcon
import app.regate.common.composes.ui.PosterCardImage
import app.regate.common.composes.ui.SimpleTopBar
import app.regate.common.composes.viewModel
import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import app.regate.common.resources.R
import app.regate.models.Reserva

typealias Reservas = @Composable (
    navigateUp:()->Unit,
    navigateToReserva:(Long,Long,Long)->Unit,
//    navigateToSignUpScreen:() -> Unit,
) -> Unit

@Inject
@Composable
fun Reservas (
    @Assisted navigateUp: () -> Unit,
    @Assisted navigateToReserva: (Long,Long,Long) -> Unit,
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
    navigateToReserva: (Long,Long,Long) -> Unit,
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
        navigateUp = navigateUp,
        selectReserva = viewModel::selectReserva,
        cancelSelected = viewModel::cancelSelectedReservas,
        deleteReservas = viewModel::deleteReservas
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
internal fun Reservas(
    viewState: ReservasState,
    formatterDate:(Instant)->String,
    formatterDateReserva:(start:Instant,end:Instant)->String,
    selectReserva:(Long)->Unit,
    cancelSelected:()->Unit,
    navigateToReserva: (Long,Long,Long) -> Unit,
    navigateUp: () -> Unit,
    deleteReservas:()->Unit,
    ) {
    Scaffold(
//        modifier = Modifier.fillMaxSize(),
        topBar = {
            if(viewState.selectedReservas.isEmpty()){
            SimpleTopBar(navigateUp = navigateUp, title = stringResource(id = R.string.bookings))
            }else{
                TopAppBar(
                    navigationIcon = { IconButton(onClick = { cancelSelected() }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = null) }
                    } ,
                    title = { Text(text = viewState.selectedReservas.size.toString())},
                    actions = {
                        IconButton(onClick = { deleteReservas() }) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                        }
                    }
                )
            }
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
                        .combinedClickable(onLongClick = {
                            selectReserva(reserva.id)
                        }) {
                            if (viewState.selectedReservas.isEmpty()) {

                                navigateToReserva(
                                    reserva.id,
                                    reserva.establecimiento_id,
                                    reserva.instalacion_id
                                )
                            } else {
                                selectReserva(reserva.id)
                            }
                        }
                        .padding(10.dp),
                    formatterDate = formatterDate,
                    isSelected = viewState.selectedReservas.contains(reserva.id),
                    formatterDateReserva = formatterDateReserva
                )
                Divider()
            }
        }
    }
}

@Composable
fun ReservaItem(
    item: Reserva,
    formatterDate: (Instant) -> String,
    formatterDateReserva:(Instant,Instant)->String,
    isSelected:Boolean,
    modifier:Modifier =Modifier
) {
    Row(modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier
            .size(width = 120.dp, height = 75.dp)
        ){
            if(isSelected){
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .zIndex(1f)
                        .padding(5.dp),
                    color = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Default.Check, contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        PosterCardImage(
            model = item.instalacion_photo,
            modifier = Modifier.fillMaxSize(),
            enabled = false
        )
        }

     Spacer(modifier = Modifier.width(5.dp))
    Column() {
        Text(text = item.instalacion_name, style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary, maxLines = 1)
        Text(
            text = "Fecha de la reserva",
            style = MaterialTheme.typography.labelLarge
        )
        Text(text = formatterDateReserva(item.start_date,item.end_date),style = MaterialTheme.typography.labelMedium)
        DateTextWithIcon(date =  formatterDate(item.created_at))
    }
    }
}




package app.regate.bottom.reserva

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DismissValue
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import app.regate.common.composes.components.CustomButton
import app.regate.common.composes.components.dialog.DialogConfirmation
import app.regate.common.composes.util.Layout
import app.regate.common.composes.viewModel
import app.regate.data.auth.AppAuthState
import app.regate.models.Cupo
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import androidx.compose.ui.window.Dialog
import app.regate.common.composes.ui.PosterCardImage
import app.regate.common.composes.ui.PosterCardImageDark
import app.regate.data.dto.empresa.establecimiento.PaidTypeEnum
import kotlinx.datetime.Instant
import app.regate.common.resources.R


typealias BottomReserva = @Composable (
    openAuthDialog:()->Unit,
    navigateUp:()->Unit,
//    navigateToSignUpScreen:() -> Unit,
) -> Unit

@Inject
@Composable
fun BottomReserva(
    viewModelFactory:(SavedStateHandle)-> BottomReservaViewModel,
    @Assisted openAuthDialog: () -> Unit,
    @Assisted navigateUp: () -> Unit
//    @Assisted navigateToReserva:()->Unit,
    ){
    BottomReserva(
        viewModel = viewModel(factory = viewModelFactory),
        openAuthDialog = openAuthDialog,
        navigateUp = navigateUp
//        navigateToReserva = navigateToReserva
    )
}


@Composable
internal fun BottomReserva(
    viewModel: BottomReservaViewModel,
    openAuthDialog: () -> Unit,
    navigateUp: () -> Unit,
//    navigateToReserva: () -> Unit,
){
    val state by viewModel.state.collectAsState()
//    val formatter = LocalAppDateFormatter.current
    BottomReserva(
        viewState = state,
//        navigateUp = navigateUp,
        onMessageShown = viewModel::clearMessage,
        openAuthDialog = openAuthDialog,
//        formatterDate = { formatter.formatMediumDateTime(it.toInstant())},
//        formatterDateReserva = { formatter.formatShortDateTime(it)},
        confirmarReservas = viewModel::confirmarReservas,
        navigateUp = navigateUp,
//        navigateToReserva = navigateToReserva,
//        openBottomSheet = { viewModel.openBottomSheet { navigateToReserva () } }
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun BottomReserva(
    viewState: BottomReservaState,
    onMessageShown:(id:Long) -> Unit,
    openAuthDialog: () -> Unit,
    confirmarReservas:()->Unit,
    navigateUp: () -> Unit,
//    formatterDate:(date:String)->String,
//    formatterDateReserva:(date:Instant)->String,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val dismissSnackbarState = rememberDismissState(
        confirmValueChange = { value ->
            if (value != DismissValue.Default) {
                snackbarHostState.currentSnackbarData?.dismiss()
                true
            } else {
                false
            }
        },
    )
    val confirmate = remember {
        mutableStateOf(false)
    }
//    val price =  viewState.cupos.reduce{sum,element->
//        sum.price+element.price
//    }.price

    if (viewState.loading) {
        Dialog(onDismissRequest = { /*TODO*/ }) {
//        Text(text = "Title")
            CircularProgressIndicator()
        }
    }
    DialogConfirmation(open = confirmate.value,
        descripcion = "Al confirmar se le descontaran ${viewState.totalPrice} monedas de su cuenta",
        dismiss = { confirmate.value = false },
        confirm = {
            confirmarReservas()
            confirmate.value = false
        })


    viewState.message?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message.message)
            // Notify the view model that the message has been dismissed
            onMessageShown(message.id)
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.padding(bottom = 60.dp)) {
            IconButton(onClick = { navigateUp() }, modifier = Modifier.fillMaxWidth()) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "arrow_down"
                )
            }
            Column(modifier = Modifier.padding(horizontal = 10.dp,vertical= 5.dp)) {
                Text(
                    text = "Puede proceder con el pago, por la reserva de ${viewState.cupos.size}" +
                            "cupos para estas instalaciones",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = "Precio Total:${viewState.totalPrice}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top=10.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if(viewState.setting?.paid_type?.list?.contains(PaidTypeEnum.DEFERRED_PAYMENT.ordinal) == true){
                        CustomButton(onClick = {
                            if (viewState.authState == AppAuthState.LOGGED_IN) {
                                confirmate.value = true
                            } else {
                                openAuthDialog()
                            }
                        }) {
                            Text(text = "Reserva por:${
                                viewState.totalPrice?.divideToPercent(viewState.setting.payment_for_reservation)
                            }")
                        }
                    }
                    CustomButton(onClick = {
                        if (viewState.authState == AppAuthState.LOGGED_IN) {
                            confirmate.value = true
                        } else {
                            openAuthDialog()
                        }
                    }) {
                        Text(text = "Proceder")
                    }
                }
                Divider()
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { }, verticalAlignment = Alignment.CenterVertically) {
            PosterCardImage(model = viewState.establecimiento?.photo?:"",modifier = Modifier
                .size(55.dp), shape = CircleShape)
                    Spacer(modifier = Modifier.width(10.dp))
                Text(text = viewState.establecimiento?.name?:"",modifier = Modifier
                    .padding(5.dp),
                style = MaterialTheme.typography.titleMedium)
            }
                Divider(modifier = Modifier.padding(vertical = 5.dp))
                Text(text = stringResource(id = R.string.where_will_it_be_played),
                style = MaterialTheme.typography.labelLarge)
                Box(modifier = Modifier
                    .clickable {  }
                    .height(110.dp)
                    .padding(vertical = 5.dp)
                    .fillMaxWidth()){
                    PosterCardImageDark(model = viewState.instalacion?.portada?:"")
                    Text(text = viewState.instalacion?.name?:"",modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(5.dp),
                        style = MaterialTheme.typography.labelLarge,color = Color.White)
                }
                PosterCardImage(model = stringResource(id = R.string.location_static_url),
                modifier = Modifier
                    .clickable {  }
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(10.dp))
                Text(text = viewState.establecimiento?.address ?: "",
                style = MaterialTheme.typography.titleSmall)
            }


        }
    SnackbarHost(hostState = snackbarHostState) { data ->
        SwipeToDismiss(
            state = dismissSnackbarState,
            background = {},
            dismissContent = { Snackbar(snackbarData = data) },
            modifier = Modifier
                .padding(horizontal = Layout.bodyMargin)
                .fillMaxWidth(),
        )
    }
    }
}


@Composable
fun CupoItem(
    item: Cupo,
//    formatterDate: (date: String) -> String,
    formatterDateReserva:(date:Instant)->String,
    modifier:Modifier =Modifier
) {
    Column(modifier = modifier) {
//        item.instalacion_name?.let {
        Text(text = "Instalacion1", style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary)
//        }
        Text(
            text = "Hora de la reserva: ${item.time.let { formatterDateReserva(it) }}",
            style = MaterialTheme.typography.labelMedium
        )
//        Text(text = "Hora en la que se reservo: ${formatterDate(item.created_at)}",
//            style = MaterialTheme.typography.labelMedium)
    }
}

fun Int.divideToPercent(divideTo: Int): Double {
    return if (divideTo == 0) 0.0
    else ((this * divideTo) / 100).toDouble()
}



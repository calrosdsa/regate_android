package app.regate.bottom.reserva

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import app.regate.common.composes.component.CustomButton
import app.regate.common.composes.component.dialog.DialogConfirmation
import app.regate.common.composes.viewModel
import app.regate.data.auth.AppAuthState
import app.regate.models.Cupo
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import app.regate.common.composes.LocalAppDateFormatter
import app.regate.common.composes.LocalAppUtil
import app.regate.common.composes.ui.PosterCardImage
import app.regate.common.composes.ui.PosterCardImageDark
import app.regate.common.composes.ui.SimpleTopBar
import app.regate.data.dto.empresa.establecimiento.PaidTypeEnum
import kotlinx.datetime.Instant
import app.regate.common.resources.R
import app.regate.constant.Route
import app.regate.constant.id
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.minutes


typealias Reservar = @Composable (
    openAuthDialog:()->Unit,
    navigateUp:()->Unit,
    navigateToEstablecimiento:(Long)->Unit,
    navigateToConversation:(Long,Long)->Unit,
    navigateToCreateSala:(Long)->Unit,
    navigateToSelectGroup:(String)->Unit,
    navController:NavController
//    navigateToSignUpScreen:() -> Unit,
) -> Unit

@Inject
@Composable
fun Reservar(
    viewModelFactory:(SavedStateHandle)-> BottomReservaViewModel,
    @Assisted openAuthDialog: () -> Unit,
    @Assisted navigateUp: () -> Unit,
    @Assisted navigateToEstablecimiento: (Long) -> Unit,
    @Assisted navigateToConversation: (Long,Long) -> Unit,
    @Assisted navigateToCreateSala: (Long) -> Unit,
    @Assisted navigateToSelectGroup: (String) -> Unit,
    @Assisted navController: NavController
//    @Assisted navigateToReserva:()->Unit,
    ){
    Reservar(
        viewModel = viewModel(factory = viewModelFactory),
        openAuthDialog = openAuthDialog,
        navigateUp = navigateUp,
        navigateToEstablecimiento=navigateToEstablecimiento,
        navigateToConversation = navigateToConversation,
        navigateToCreateSala = navigateToCreateSala,
        navigateToSelectGroup = navigateToSelectGroup,
        navigateToInstalacion = {navController.navigate(Route.INSTALACION id  it)},
        navigateToReservas = { navController.navigate(Route.RESERVAS)
        },
        navigateToRecarga = {navController.navigate(Route.RECARGAR)}

//        navigateToReserva = navigateToReserva
    )
}


@Composable
internal fun Reservar(
    viewModel: BottomReservaViewModel,
    openAuthDialog: () -> Unit,
    navigateUp: () -> Unit,
    navigateToEstablecimiento: (Long) -> Unit,
    navigateToConversation: (Long,Long) -> Unit,
    navigateToCreateSala: (Long) -> Unit,
    navigateToSelectGroup: (String) -> Unit,
    navigateToInstalacion:(Long)->Unit,
    navigateToReservas: () -> Unit,
    navigateToRecarga:()->Unit,
){
    val state by viewModel.state.collectAsState()
    val formatter = LocalAppDateFormatter.current
    val appUtil = LocalAppUtil.current
    Reservar(
        viewState = state,
//        navigateUp = navigateUp,
        onMessageShown = viewModel::clearMessage,
        openAuthDialog = openAuthDialog,
//        formatterDate = { formatter.formatMediumDateTime(it.toInstant())},
//        formatterDateReserva = { formatter.formatShortDateTime(it)},
        confirmarReservas = viewModel::confirmarReservas,
        navigateUp = navigateUp,
        navigateToEstablecimiento = navigateToEstablecimiento,
        navigateToConversation ={
            viewModel.navigateToConversationE(navigateToConversation)

        },
        formatShortTime = {formatter.formatShortTime(it)},
        formatDate = {formatter.formatWithSkeleton(it.toEpochMilliseconds(),formatter.monthDaySkeleton)},
        openMap = appUtil::openMap,
        navigateToCreateSala = navigateToCreateSala,
        navigateToSelectGroup = {
            viewModel.navigateSelect(navigateToSelectGroup)
        },
        navigateToInstalacion = navigateToInstalacion,
        navigateToRecarga = navigateToRecarga,
        navigateToReservas = navigateToReservas
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Reservar(
    viewState: BottomReservaState,
    onMessageShown:(id:Long) -> Unit,
    openAuthDialog: () -> Unit,
    confirmarReservas:()->Unit,
    navigateUp: () -> Unit,
    navigateToEstablecimiento: (Long) -> Unit,
    navigateToConversation: () -> Unit,
    formatShortTime:(time:Instant)->String,
    formatDate:(date:Instant)->String,
    openMap:(lng:String?,lat:String?,label:String?)->Unit,
    navigateToCreateSala: (Long) -> Unit,
    navigateToSelectGroup: () -> Unit,
    navigateToInstalacion:(Long)->Unit,
    navigateToReservas: () -> Unit,
    navigateToRecarga:()->Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val confirmate = remember {
        mutableStateOf(false)
    }
    var expanded by remember {
        mutableStateOf(false)
    }
    val currentTime =Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).toInstant(
        TimeZone.UTC)

    if (viewState.loading) {
        Dialog(onDismissRequest = { /*TODO*/ }) {
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
            when (message.type) {
                ReservarMessageType.MONTO_INSUFICIENTE.ordinal -> {
                    val result = snackbarHostState.showSnackbar(
                        message.message, actionLabel = "Recargar",duration = SnackbarDuration.Short,
                    )
                    when(result){
                        SnackbarResult.ActionPerformed -> { navigateToRecarga() }
                        else ->{}
                    }
                }
                ReservarMessageType.VER_RESERVA.ordinal -> {
                    val result = snackbarHostState.showSnackbar(
                        message.message, actionLabel = "Ver",duration = SnackbarDuration.Short,
                    )
                    when(result){
                        SnackbarResult.ActionPerformed -> { navigateToReservas()}
                        else ->{}
                    }
                }
               else -> {
                    snackbarHostState.showSnackbar(message.message)
                }
            }
            // Notify the view model that the message has been dismissed
            onMessageShown(message.id)
        }
    }
    Scaffold(
        snackbarHost = {SnackbarHost(hostState = snackbarHostState)},
        topBar = {
            SimpleTopBar(navigateUp = { navigateUp()},title = viewState.instalacion?.name,
            actions = {
                Box(){
                IconButton(onClick = { expanded = true }) {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = "menu")
                }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                            DropdownMenuItem(
                                text = { Text(text = stringResource(id = R.string.create_sala)) },
                                onClick = { viewState.instalacion?.let {
                                if(viewState.authState == AppAuthState.LOGGED_IN){
                                    navigateToCreateSala(it.establecimiento_id)
                                }else {
                                    openAuthDialog()
                                }
                                }  }
                            )
                        DropdownMenuItem(
                            text = { Text(text = stringResource(id = R.string.send_to_a_chat)) },
                            onClick = {
                            if(viewState.authState == AppAuthState.LOGGED_IN){
                                navigateToSelectGroup()
                            }else{
                                openAuthDialog()
                            }
                            }
                        )
                    }
                }
            })
//            IconButton(onClick = { navigateUp() }) {
//                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
//            }
        },
        bottomBar = {
            BottomAppBar(modifier = Modifier.height(110.dp)) {
                Column(modifier = Modifier.padding(horizontal = 10.dp)) {
                    Text(
                        text = "Precio Total:${viewState.totalPrice}",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (viewState.setting?.paid_type?.list?.contains(PaidTypeEnum.DEFERRED_PAYMENT.ordinal) == true) {
                            CustomButton(onClick = {
                                if (viewState.authState == AppAuthState.LOGGED_IN) {
                                    confirmate.value = true
                                } else {
                                    openAuthDialog()
                                }
                            }) {
                                viewState.setting.payment_for_reservation?.let {
                                Text(
                                    text = "Reserva por:${
                                            viewState.totalPrice?.divideToPercent(
                                                it
                                            )
                                    }"
                                ) }
                            }
                        }
//                            viewState.cupos.map{
//                                Text(text = it.time.toString())
//                            }
                        CustomButton(onClick = {
                            if (viewState.authState == AppAuthState.LOGGED_IN) {
                                confirmate.value = true
                            } else {
                                openAuthDialog()
                            }
                        }, enabled = if(viewState.cupos.isNotEmpty()) currentTime < viewState.cupos.first().time else true
                        ) {
                            Text(text = stringResource(id = R.string.full_payment))
                        }
                    }

                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(paddingValues)
            .fillMaxSize()) {

                Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)) {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        , verticalAlignment = Alignment.CenterVertically
                    ) {
                        PosterCardImage(
                            model = viewState.establecimiento?.photo ?: "", modifier = Modifier
                                .clickable {
                                    viewState.establecimiento?.let {
                                        navigateToEstablecimiento(
                                            it.id
                                        )
                                    }
                                }
                                .size(55.dp), shape = CircleShape
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column() {
                        Text(
                            text = viewState.establecimiento?.name ?: "", modifier = Modifier
                                .clickable {
                                    viewState.establecimiento?.let {
                                        navigateToEstablecimiento(
                                            it.id
                                        )
                                    }
                                }
                                .padding(5.dp),
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                            Row(verticalAlignment = Alignment.CenterVertically,modifier = Modifier
                                .clickable { viewState.establecimiento?.let {
                                    if (viewState.authState == AppAuthState.LOGGED_IN) {
                                        navigateToConversation()
                                    }else{
                                        openAuthDialog()
                                    }
                                }
                                }) {
                                Icon(
                                    imageVector = Icons.Default.Chat,
                                    contentDescription = null,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                            Text(text = stringResource(id = R.string.inbox_to_establecimiento),
                            style = MaterialTheme.typography.labelLarge)
                            }
                        }
                    }
                    Divider(modifier = Modifier.padding(vertical = 5.dp))

                    if(viewState.cupos.isNotEmpty()){

                    Text(
                        text = "Hora de la reserva",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        text = formatDate(viewState.cupos.first().time) +
                                " ${formatShortTime(viewState.cupos.first().time)} a ${
                            formatShortTime(
                                viewState.cupos.last().time.plus(30.minutes)
                            )
                        }",
                        style = MaterialTheme.typography.titleSmall
                    )

                    Divider(modifier = Modifier.padding(vertical = 5.dp))
                    }

                    Text(
                        text = stringResource(id = R.string.where_will_it_be_played),
                        style = MaterialTheme.typography.labelLarge
                    )

                    viewState.instalacion?.let {instalacion->
                    Box(modifier = Modifier
                        .clickable {
                            navigateToInstalacion(viewState.instalacion.id)
                        }
                        .height(130.dp)
                        .padding(vertical = 5.dp)
                        .fillMaxWidth()) {
                        PosterCardImageDark(
                            model = viewState.instalacion.portada,
                        )
                        Text(
                            text = viewState.instalacion.name, modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(5.dp),
                            style = MaterialTheme.typography.labelLarge, color = Color.White
                        )
                    }
                        Spacer(modifier = Modifier.height(5.dp))
                        instalacion.description?.let { Text(text = it,style = MaterialTheme.typography.bodySmall) }
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    PosterCardImage(model = viewState.establecimiento?.address_photo,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        onClick = {
                            openMap(
                                viewState.establecimiento?.longitud,
                                viewState.establecimiento?.latidud,
                                viewState.establecimiento?.name,
                            )
                        }
                    )

                    Text(
                        text = viewState.establecimiento?.address ?: "",
                        style = MaterialTheme.typography.titleSmall
                    )
                }

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



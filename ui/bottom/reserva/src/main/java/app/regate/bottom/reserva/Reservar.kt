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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DismissValue
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import app.regate.common.compose.components.CustomButton
import app.regate.common.compose.components.dialog.DialogConfirmation
import app.regate.common.compose.util.Layout
import app.regate.common.compose.viewModel
import app.regate.data.auth.AppAuthState
import app.regate.models.Cupo
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import androidx.compose.ui.window.Dialog
import app.regate.common.compose.LocalAppDateFormatter
import app.regate.common.compose.LocalAppUtil
import app.regate.common.compose.ui.PosterCardImage
import app.regate.common.compose.ui.PosterCardImageDark
import app.regate.common.compose.ui.SimpleTopBar
import app.regate.data.dto.empresa.establecimiento.PaidTypeEnum
import kotlinx.datetime.Instant
import app.regate.common.resources.R
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
//    navigateToReserva: () -> Unit,
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
    var expanded by remember {
        mutableStateOf(false)
    }
    val currentTime =Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).toInstant(
        TimeZone.UTC)
//    val currentTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).toInstant(
//        TimeZone.currentSystemDefault())
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
    Scaffold(
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
                            text = { Text(text = stringResource(id = R.string.send_to_a_group)) },
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
                        text = "Hora en la que se jugara",
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
                        .clickable { }
                        .height(120.dp)
                        .padding(vertical = 5.dp)
                        .fillMaxWidth()) {
                        PosterCardImageDark(model = viewState.instalacion.portada)
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
                            .clickable {
                                openMap(
                                    viewState.establecimiento?.longitud,
                                    viewState.establecimiento?.latidud,
                                    viewState.establecimiento?.name,
                                )
                            }
                            .fillMaxWidth()
                            .height(200.dp))

                    Text(
                        text = viewState.establecimiento?.address ?: "",
                        style = MaterialTheme.typography.titleSmall
                    )
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



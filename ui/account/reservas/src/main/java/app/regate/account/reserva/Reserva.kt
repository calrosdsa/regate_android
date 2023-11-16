package app.regate.account.reserva

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import app.regate.common.composes.LocalAppDateFormatter
import app.regate.common.composes.LocalAppUtil
import app.regate.common.composes.component.dialog.DialogConfirmation
import app.regate.common.composes.component.text.Label
import app.regate.common.composes.ui.PosterCardImage
import app.regate.common.composes.ui.PosterCardImageDark
import app.regate.common.composes.ui.SimpleTopBar
import app.regate.common.composes.viewModel
import app.regate.common.resources.R
import app.regate.data.auth.AppAuthState
import com.dokar.sheets.rememberBottomSheetState
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import kotlin.time.Duration.Companion.minutes

typealias Reserva = @Composable (
    navigateUp:()->Unit,
    navigateToEstablecimiento:(Long)->Unit,
    navigateToConversation:(Long,Long)->Unit,
    openAuthDialog:()->Unit,
    navigateToMyChats:()->Unit,
//    navigateToSignUpScreen:() -> Unit,
) -> Unit

@Inject
@Composable
fun Reserva (
    @Assisted navigateUp: () -> Unit,
    @Assisted navigateToEstablecimiento:(Long)->Unit,
    @Assisted navigateToConversation: (Long,Long) -> Unit,
    @Assisted openAuthDialog:()->Unit,
    @Assisted navigateToMyChats: () -> Unit,
    viewModelFactory:(SavedStateHandle)-> ReservaViewModel
){

    Reserva(
        viewModel = viewModel(factory = viewModelFactory),
        navigateUp = navigateUp,
        navigateToEstablecimiento = navigateToEstablecimiento,
        navigateToConversation = navigateToConversation,
        navigateToMyChats = navigateToMyChats,
        openAuthDialog = openAuthDialog
    )
}

@Composable
internal fun Reserva(
    viewModel: ReservaViewModel,
    navigateUp: () -> Unit,
    navigateToEstablecimiento: (Long) -> Unit,
    navigateToConversation: (Long,Long) -> Unit,
    navigateToMyChats: () -> Unit,
    openAuthDialog: () -> Unit
){
    val state  by viewModel.state.collectAsState()
    val formatter = LocalAppDateFormatter.current
    val appUtil = LocalAppUtil.current
    Reserva(
        viewState = state,
        navigateUp = navigateUp,
        navigateToEstablecimiento = navigateToEstablecimiento,
        navigateToConversation = {
            viewModel.navigateToConversationE(navigateToConversation)
        },
        formatShortTime = {formatter.formatShortTime(it)},
        formatDate = {formatter.formatWithSkeleton(it.toEpochMilliseconds(),formatter.monthDaySkeleton)},
        openMap = appUtil::openMap,
        openAuthDialog = openAuthDialog,
        navigateToMyChats = navigateToMyChats,
        deleteReserva = viewModel::deleteReserva,
        updateDescription = viewModel::updateDescription
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Reserva(
    viewState:ReservaState,
    openMap:(lng:String?,lat:String?,label:String?)->Unit,
    formatShortTime:(time: Instant)->String,
    formatDate:(date: Instant)->String,
    deleteReserva:()->Unit,
    updateDescription:(String) ->Unit,
    navigateUp: () -> Unit,
    navigateToEstablecimiento:(Long)->Unit,
    navigateToConversation: () -> Unit,
    navigateToMyChats: () -> Unit,
    openAuthDialog:()->Unit,
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    var deleteReservaConfirmation by remember {
        mutableStateOf(false)
    }
    val sheetState = rememberBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    AddDescriptionBottomSheen(
        state = sheetState,
        description = viewState.data?.reserva?.description ?: "",
        save = {description->
            coroutineScope.launch {
                updateDescription(description)
                sheetState.collapse()
            }
        }
    )


    DialogConfirmation(open = deleteReservaConfirmation,
        dismiss = { deleteReservaConfirmation = false },
        confirm = {
            deleteReserva()
            navigateUp()
        })
    Scaffold(
        topBar = {
            SimpleTopBar(navigateUp = { navigateUp() },
                actions = {
                    Box() {
                        IconButton(onClick = { expanded = true }) {
                            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "menu")
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                        ) {

                            DropdownMenuItem(
                                text = { Text(text = stringResource(id = R.string.send_to_a_chat)) },
                                onClick = {
                                    if (viewState.authState == AppAuthState.LOGGED_IN) {
                                        navigateToMyChats()
                                    } else {
                                        openAuthDialog()
                                    }
                                    expanded = false
                                }
                            )

                            DropdownMenuItem(
                                text = { Text(text = stringResource(id = R.string.delete_reservation)) },
                                onClick = {
                                    deleteReservaConfirmation = true
                                    expanded = false
                                }
                            )

                            DropdownMenuItem(
                                text = { Text(text = if (viewState.data?.reserva?.description == null) "Agregar descripción" else "Editar descripción") },
                                onClick = { coroutineScope.launch {
                                    sheetState.expand()
                                    expanded = false

                                } }
                            )

                        }
                    }
                })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            viewState.data?.let { data ->


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp), verticalAlignment = Alignment.CenterVertically
                ) {
                    PosterCardImage(
                        model = data.establecimiento?.photo ?: "", modifier = Modifier
                            .clickable {
                                data.establecimiento?.id?.let {
                                    navigateToEstablecimiento(
                                        it
                                    )
                                }
                            }
                            .size(55.dp), shape = CircleShape
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column() {
                        Text(
                            text = data.establecimiento?.name ?: "", modifier = Modifier
                                .clickable {
                                    data.establecimiento?.let {
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
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                            .clickable { data.establecimiento?.id?.let { navigateToConversation() } }) {
                            Icon(
                                imageVector = Icons.Default.Chat,
                                contentDescription = null,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = stringResource(id = R.string.inbox_to_establecimiento),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
                Divider(modifier = Modifier.padding(vertical = 5.dp))

                viewState.data.reserva.description?.let {description->

                Label(text = stringResource(id = R.string.description))

                Text(text = description,style=MaterialTheme.typography.bodyMedium)
                Divider(modifier = Modifier.padding(vertical = 5.dp))
                }



                Row() {
                    Column(modifier = Modifier.fillMaxWidth(0.5f)) {

                        Text(
                            text = stringResource(id = R.string.reservation_date),
                            style = MaterialTheme.typography.labelLarge
                        )
                        Text(
                            text = formatDate(data.reserva.start_date) +
                                    " ${formatShortTime(data.reserva.start_date)} a ${
                                        formatShortTime(
                                            data.reserva.end_date.plus(30.minutes)
                                        )
                                    }",
                            style = MaterialTheme.typography.titleSmall
                        )
                    }

                    Column(modifier = Modifier.fillMaxWidth()) {

                        Text(
                            text = "Total",
                            style = MaterialTheme.typography.labelLarge
                        )
                        Text(
                            text = data.reserva.pagado.toString() + " BOB",
                            style = MaterialTheme.typography.titleSmall
                        )
                    }


                }

                Divider(modifier = Modifier.padding(vertical = 5.dp))



                Text(
                    text = stringResource(id = R.string.where_will_it_be_played),
                    style = MaterialTheme.typography.labelLarge
                )
                Box(modifier = Modifier
                    .clickable { }
                    .height(110.dp)
                    .padding(vertical = 5.dp)
                    .fillMaxWidth()) {
                    PosterCardImageDark(model = data.instalacion?.portada)
                    Text(
                        text = data.instalacion?.name ?: "", modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(5.dp),
                        style = MaterialTheme.typography.labelLarge, color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
                data.instalacion?.description?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                PosterCardImage(model = data.establecimiento?.address_photo,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    onClick = {
                        openMap(
                            data.establecimiento?.longitud,
                            data.establecimiento?.latidud,
                            data.establecimiento?.name,
                        )
                    }
                )

                Text(
                    text = data.establecimiento?.address ?: "",
                    style = MaterialTheme.typography.titleSmall
                )
            }

        }
    }
}

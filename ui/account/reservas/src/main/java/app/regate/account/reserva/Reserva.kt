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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import app.regate.common.composes.LocalAppDateFormatter
import app.regate.common.composes.LocalAppUtil
import app.regate.common.composes.ui.PosterCardImage
import app.regate.common.composes.ui.PosterCardImageDark
import app.regate.common.composes.ui.SimpleTopBar
import app.regate.common.composes.viewModel
import app.regate.common.resources.R
import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import kotlin.time.Duration.Companion.minutes

typealias Reserva = @Composable (
    navigateUp:()->Unit,
    navigateToEstablecimiento:(Long)->Unit,
    navigateToConversation:(Long,Long)->Unit,
//    navigateToSignUpScreen:() -> Unit,
) -> Unit

@Inject
@Composable
fun Reserva (
    @Assisted navigateUp: () -> Unit,
    @Assisted navigateToEstablecimiento:(Long)->Unit,
    @Assisted navigateToConversation: (Long,Long) -> Unit,
    viewModelFactory:(SavedStateHandle)-> ReservaViewModel
){

    Reserva(
        viewModel = viewModel(factory = viewModelFactory),
        navigateUp = navigateUp,
        navigateToEstablecimiento = navigateToEstablecimiento,
        navigateToConversation = navigateToConversation
    )
}

@Composable
internal fun Reserva(
    viewModel: ReservaViewModel,
    navigateUp: () -> Unit,
    navigateToEstablecimiento: (Long) -> Unit,
    navigateToConversation: (Long,Long) -> Unit
){
    val state  by viewModel.state.collectAsState()
    val formatter = LocalAppDateFormatter.current
    val appUtil = LocalAppUtil.current
    Reserva(
        viewState = state,
        navigateUp = navigateUp,
        navigateToEstablecimiento = navigateToEstablecimiento,
        navigateToConversation = navigateToConversation,
        formatShortTime = {formatter.formatShortTime(it)},
        formatDate = {formatter.formatWithSkeleton(it.toEpochMilliseconds(),formatter.monthDaySkeleton)},
        openMap = appUtil::openMap
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Reserva(
    viewState:ReservaState,
    openMap:(lng:String?,lat:String?,label:String?)->Unit,
    formatShortTime:(time: Instant)->String,
    formatDate:(date: Instant)->String,
    navigateUp: () -> Unit,
    navigateToEstablecimiento:(Long)->Unit,
    navigateToConversation: (Long,Long) -> Unit
) {
    Scaffold(
        topBar = {
            SimpleTopBar(navigateUp = { navigateUp() })
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
                            text = data.establecimiento?.name?:"", modifier = Modifier
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
                            .clickable { data.establecimiento?.id?.let { navigateToConversation(it,0) } }) {
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
                            text = "${data.reserva.paid} BOB",
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
                        text = data.instalacion?.name?:"", modifier = Modifier
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
                        .height(200.dp)
                        .clickable {
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

package app.regate.instalacion

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DismissValue
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import app.regate.common.composes.LocalAppDateFormatter
import app.regate.common.composes.components.CustomButton
import app.regate.common.composes.ui.CommonTopBar
import app.regate.common.composes.util.Layout
import app.regate.common.composes.viewModel
import app.regate.data.dto.empresa.establecimiento.CupoInstaDto
import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject


typealias InstalacionDetail = @Composable (
    navigateUp:()->Unit,
//    navigateToSignUpScreen:() -> Unit,
) -> Unit

@Inject
@Composable
fun InstalacionDetail(
    viewModelFactory:(SavedStateHandle)-> InstalacionViewModel,
    @Assisted navigateUp: () -> Unit,
    ){
    InstalacionDetail(
        viewModel = viewModel(factory = viewModelFactory),
        navigateUp = navigateUp,
    )
}


@Composable
internal fun InstalacionDetail(
    viewModel: InstalacionViewModel,
    navigateUp: () -> Unit,

){
    val state by viewModel.state.collectAsState()
//    val formatter = LocalAppDateFormatter.current
    InstalacionDetail(
        viewState = state,
        navigateUp = navigateUp,
        onMessageShown = viewModel::clearMessage,
//        formatDate = { formatter.formatShortDateTime(it) },
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class
)
@Composable
internal fun InstalacionDetail(
    viewState: InstalacionState,
    navigateUp: () -> Unit,
    onMessageShown:(id:Long) -> Unit,
//    formatDate:(date:Instant) -> String,

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
    val showCheckBox = remember {
        mutableStateOf(false)
    }


    viewState.message?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message.message)
            // Notify the view model that the message has been dismissed
            onMessageShown(message.id)
        }
    }
    Scaffold(
        snackbarHost = {
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
        },

        floatingActionButtonPosition = FabPosition.Center,
    ) { padding ->
        Box(
            modifier = Modifier

                .padding(vertical = padding.calculateTopPadding(), horizontal = 5.dp)
                .fillMaxSize()
        ) {
            LazyColumn() {
                stickyHeader {
                    CommonTopBar(onBack = navigateUp)
                    Divider()
                }
                viewState.instalacion?.let { instalacion ->
                    item {
                        Text(text = instalacion.name, style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                    item {
                        instalacion.description?.let { it1 ->
                            Text(
                                text = it1,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Cupos", style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                            CustomButton(onClick = { showCheckBox.value = !showCheckBox.value }) {
                                Text(text = "Reservar")
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                }
            }

        }
    }
}


@Composable
fun Cupos(
    item:CupoInstaDto,
    showCheckBox:Boolean,
    formatDate: (date: Instant) -> String,
    selected:Boolean,
    addCupo: (cupo: CupoInstaDto) -> Unit,
    isAvailable:Boolean,
    modifier: Modifier = Modifier,
) {
//    val isLongPressActive = remember { mutableStateOf(false) }
    Row(
        modifier = modifier, horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column() {
            Text(text = "Reservar por una hora", style = MaterialTheme.typography.titleSmall)
//            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = formatDate(item.time),
                modifier = Modifier,
                style = MaterialTheme.typography.labelMedium
            )
            if(item.reserva_id != null){
                Text(
                    text ="Ha sido reservado",
                    modifier = Modifier,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }else if(isAvailable){
            Text(
                text ="Disponible",
                modifier = Modifier,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
            }else{
                Text(
                    text ="No disponible",
                    modifier = Modifier,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Red
                )
            }
        }
        if(showCheckBox){
        Checkbox(
            checked = selected, onCheckedChange = { addCupo(item) },
            modifier = Modifier.size(40.dp),
            enabled = isAvailable && item.reserva_id == null
        )
        }

    }
}

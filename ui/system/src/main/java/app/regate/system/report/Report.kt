package app.regate.system.report

import android.content.pm.PackageManager.PackageInfoFlags
import android.os.Build
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.SavedStateHandle
import app.regate.common.composes.components.dialog.DialogConfirmation
import app.regate.common.composes.components.dialog.LoaderDialog
import app.regate.common.composes.components.input.InputForm
import app.regate.common.composes.components.select.SelectComponent
import app.regate.common.composes.ui.BottomBar
import app.regate.common.composes.ui.SimpleTopBar
import app.regate.common.composes.viewModel
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import app.regate.common.resources.R
import app.regate.data.auth.AppAuthState
import app.regate.settings.AppPreferences

typealias Report = @Composable (
    navigateUp:()->Unit
        ) -> Unit

@Inject
@Composable
fun Report(
    viewModelFactory:(SavedStateHandle)-> ReportViewModel,
    @Assisted navigateUp: () -> Unit
){
    Report(
        viewModel = viewModel(factory = viewModelFactory),
        navigateUp = navigateUp
    )
}

@Composable
internal fun Report(
    viewModel: ReportViewModel,
    navigateUp: () -> Unit
){
    val viewState by viewModel.state.collectAsState()
    Report(
        viewState = viewState,
        navigateUp = navigateUp,
        sendReport = viewModel::sendReport,
        clearMessage = viewModel::clearMessage
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Report(
    viewState: ReportState,
    navigateUp: () -> Unit,
    sendReport:(String)->Unit,
    clearMessage:(Long)->Unit,
) {

    var detail by remember { mutableStateOf("") }
    var showDetail by remember { mutableStateOf(false) }
    var currenOption by remember { mutableStateOf(0) }
    var showConfirmatioDialog by remember {
        mutableStateOf(false)
    }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val options = arrayListOf(
        Option(stringResource(id = R.string.inappropriate_material), 5),
        Option(stringResource(id = R.string.nudes), 1),
        Option(stringResource(id = R.string.threats_of_violence), 2),
        Option(stringResource(id = R.string.spam_scam), 4),
        Option(stringResource(id = R.string.other), 6),
        )
    val snackbarHostState = remember {
        SnackbarHostState()
    }


    LaunchedEffect(key1 = showDetail, block = {
        if(showDetail){
            detail = ""
        }else {
            focusManager.clearFocus(true)
        }
    })

    viewState.message?.let { message->
        LaunchedEffect(key1 = message,block = {
            snackbarHostState.showSnackbar(message.message)
            clearMessage(message.id )
        })
    }

    DialogConfirmation(open = showConfirmatioDialog,
        dismiss = { showConfirmatioDialog = false },
        confirm = {
            sendReport(detail)
            showConfirmatioDialog = false
        })
    LoaderDialog(loading = viewState.loading)

    Scaffold(modifier = Modifier
        .fillMaxSize(),
        topBar = {
            SimpleTopBar(navigateUp = navigateUp,title =  stringResource(id = R.string.report))
        },
        bottomBar = {
            BottomAppBar() {
                Box(modifier = Modifier.fillMaxWidth()){
                Button(onClick = { showConfirmatioDialog = true },modifier = Modifier.align(Alignment.CenterEnd)) {
                    Text(text = stringResource(id = R.string.send))
                }
                }
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState)}
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                options.map { item ->
                    SelectComponent(
                        selected = currenOption == item.id,
                        text = item.text,
                        onSelect = {
                            currenOption = item.id
                            detail = item.text
                            showDetail = currenOption == 6
                        })
                }
                Spacer(modifier = Modifier.height(10.dp))
                AnimatedVisibility(visible = showDetail) {
                InputForm(
                    onValueChange = {if(it.length <= 255) detail = it },
                    value = detail,
                    modifier = Modifier
                        .height(120.dp)
                        .focusRequester(focusRequester),
                    maxLines = 4,
                    keyboardActions = KeyboardActions(
                        onDone = {
                            showConfirmatioDialog = true
                        }
                    ),
                    imeAction = ImeAction.Done
                ){
                    Text(text = "${detail.length}/255",style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(2.dp))
                }
                }
                }
        }
    }
}

//@Composable
//internal fun Motivo(){
//    R
//}
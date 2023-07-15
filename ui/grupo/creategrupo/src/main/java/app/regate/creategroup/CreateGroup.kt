package app.regate.creategroup

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import app.regate.common.composes.components.dialog.DialogConfirmation
import app.regate.common.composes.components.dialog.LoaderDialog
import app.regate.common.composes.ui.CommonTopBar
import app.regate.common.composes.viewModel
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import app.regate.common.resources.R

typealias CreateGroup = @Composable (
    navigateUp:()->Unit,
    navigateToGroup:(Long)->Unit,
//    groupId:Long,
//    navigateToChat:(id:Long)->Unit,
//    openAuthBottomSheet:()->Unit
        ) -> Unit

@Inject
@Composable
fun CreateGroup(
    viewModelFactory:(SavedStateHandle)-> CreateGroupViewModel,
    @Assisted navigateUp: () -> Unit,
    @Assisted navigateToGroup: (Long) -> Unit
//    @Assisted navigateToChat: (id:Long) -> Unit,
//    @Assisted openAuthBottomSheet: () -> Unit
){
    CreateGroup(
        viewModel = viewModel(factory = viewModelFactory),
        navigateUp = navigateUp,
        navigateToGroup = navigateToGroup
//        navigateToChat= navigateToChat,
//        openAuthBottomSheet = openAuthBottomSheet
    )
}

@Composable
internal fun CreateGroup(
    viewModel: CreateGroupViewModel,
    navigateUp: () -> Unit,
    navigateToGroup: (Long) -> Unit
//    navigateToChat: (id:Long) -> Unit,
//    openAuthBottomSheet: () -> Unit
){
    val viewState by viewModel.state.collectAsState()
    LoaderDialog(loading = viewState.loading)
    CreateGroup(
        viewState = viewState,
        navigateUp = navigateUp,
        createGroup = {name,description,visibility ->
            viewModel.createGroup(name,description,visibility, navigateToGroup)
        },
        clearMessage = viewModel::clearMessage,
        uploadImage = viewModel::uploadImage,
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun CreateGroup(
    viewState: CreateGroupState,
    navigateUp: () -> Unit,
    createGroup:(String,String,String)->Unit,
    clearMessage:(id:Long)->Unit,
    uploadImage:(String,String,ByteArray)->Unit,
//    onChangeAsunto:(v:String)->Unit,
//    onChangeDescription:(v:String)->Unit,
//    onChangeCupos:(v:String)->Unit,
) {
    var name by remember{ mutableStateOf("Sala de juegos") }
    var description by remember{ mutableStateOf("Armemos 2 equipos de 10 ") }
    var visibility by remember{ mutableStateOf("15") }
    val pagerState = rememberPagerState(initialPage = 0)
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    viewState.message?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message.message)
            // Notify the view model that the message has been dismissed
            clearMessage(message.id)
        }
    }

    val showConfirmationDialog = remember{
        mutableStateOf(false)
    }
//    val isLastPage by remember{
//        derivedStateOf{
//            pagerState.currentPage == 1
//        }
//    }
    DialogConfirmation(open = showConfirmationDialog.value,
        dismiss = { showConfirmationDialog.value = false },
        confirm = {
            createGroup(name,description,visibility)
            showConfirmationDialog.value = false
        })

    Scaffold(
        topBar = {
            CommonTopBar(onBack = navigateUp)
        },
        bottomBar = {
            BottomAppBar() {
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
                ){
                if(pagerState.currentPage > 0){
                Button(modifier = Modifier.align(Alignment.CenterStart),onClick = { coroutineScope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage -1)
                } } ) {
                    Text(text = "Volver")
                }
                }
                Button(modifier = Modifier.align(Alignment.CenterEnd),onClick = {
//                    if(isLastPage){
                    showConfirmationDialog.value = true
//                    }else{
//                        coroutineScope.launch { pagerState.animateScrollToPage(pagerState.currentPage +1) }
//                    }
                }) {
                    Text(text = stringResource(id = R.string.create_group))
                }
            }
            }
        },
        modifier = Modifier
            .fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
//            .padding(10.dp)
    ) { paddingValues ->
        Page1(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 10.dp),
            asunto = name,
            description = description,
            cupos = visibility,
            onChangeCupos = {visibility = it},
            onChangeDescription = {description = it},
            onChangeAsunto = { name=it},
            uploadImage = uploadImage
        )
    }
}




package app.regate.creategroup

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import app.regate.data.dto.empresa.grupo.GroupVisibility

typealias CreateGroup = @Composable (
    navigateUp:()->Unit,
    navigateToGroup:(Long)->Unit,
//    groupId:Long,
//    navigateToChat:(id:Long)->Unit,
    openAuthBottomSheet:()->Unit
        ) -> Unit

@Inject
@Composable
fun CreateGroup(
    viewModelFactory:(SavedStateHandle)-> CreateGroupViewModel,
    @Assisted navigateUp: () -> Unit,
    @Assisted navigateToGroup: (Long) -> Unit,
    @Assisted openAuthBottomSheet: () -> Unit
){
    CreateGroup(
        viewModel = viewModel(factory = viewModelFactory),
        navigateUp = navigateUp,
        navigateToGroup = navigateToGroup,
        openAuthBottomSheet = openAuthBottomSheet
//        navigateToChat= navigateToChat,
//        openAuthBottomSheet = openAuthBottomSheet
    )
}

@Composable
internal fun CreateGroup(
    viewModel: CreateGroupViewModel,
    navigateUp: () -> Unit,
    navigateToGroup: (Long) -> Unit,
    openAuthBottomSheet: () -> Unit
//    navigateToChat: (id:Long) -> Unit,
//    openAuthBottomSheet: () -> Unit
){
    val viewState by viewModel.state.collectAsState()
    CreateGroup(
        viewState = viewState,
        navigateUp = navigateUp,
        createGroup = {name,description,visibility,removeLoader ->
            viewModel.createGroup(name,description,visibility, navigateToGroup,openAuthBottomSheet,removeLoader)
        },
        clearMessage = viewModel::clearMessage,
        uploadImage = viewModel::uploadImage,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun CreateGroup(
    viewState: CreateGroupState,
    navigateUp: () -> Unit,
    createGroup:(String,String,GroupVisibility,()->Unit)->Unit,
    clearMessage:(id:Long)->Unit,
    uploadImage:(String,String,ByteArray)->Unit,
//    onChangeAsunto:(v:String)->Unit,
//    onChangeDescription:(v:String)->Unit,
//    onChangeCupos:(v:String)->Unit,
) {
    var name by remember(viewState.group){ mutableStateOf(viewState.group?.name?:"") }
    var description by remember(viewState.group){ mutableStateOf(viewState.group?.description?:"") }
    var visibility by remember{ mutableStateOf(GroupVisibility.PUBLIC) }
    val pagerState = rememberPagerState(initialPage = 0)
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val loading = remember {
        mutableStateOf(false)
    }

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
    LoaderDialog(loading = loading.value)
    DialogConfirmation(open = showConfirmationDialog.value,
        dismiss = { showConfirmationDialog.value = false },
        confirm = {
            loading.value = true
            createGroup(name,description,visibility) { loading.value = false }
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
                    if(viewState.group != null){
                        Text(text = stringResource(id = R.string.save))
                    }else {
                        Text(text = stringResource(id = R.string.create_group))
                    }
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
        MainPage(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 10.dp),
            asunto = name,
            description = description,
            visibility = visibility,
            onChangeVisibility = {visibility = it},
            onChangeDescription = {description = it},
            onChangeAsunto = { name=it},
            uploadImage = uploadImage,
//            group = viewState.group
        )
    }
}




package app.regate.createsala

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import app.regate.common.composes.LocalAppDateFormatter
import app.regate.common.composes.components.dialog.DialogConfirmation
import app.regate.common.composes.components.dialog.LoaderDialog
import app.regate.common.composes.ui.CommonTopBar
import app.regate.common.composes.util.Layout
import app.regate.common.composes.viewModel
import app.regate.createsala.pages.Page1
import app.regate.createsala.pages.Page2
import app.regate.createsala.pages.SelectGroup
import app.regate.data.dto.empresa.grupo.GroupVisibility
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

typealias CreateSala = @Composable (
    navigateUp:()->Unit,
    reservarInstalacion: @Composable () -> Unit,
    navigateToCreateGroup:()->Unit,
    navigateToGroup:(Long)->Unit
//    navigateToChat:(id:Long)->Unit,
//    openAuthBottomSheet:()->Unit
        ) -> Unit

@Inject
@Composable
fun CreateSala(
    viewModelFactory:(SavedStateHandle)-> CreateSalaViewModel,
    @Assisted navigateUp: () -> Unit,
    @Assisted reservarInstalacion:@Composable () -> Unit,
    @Assisted navigateToCreateGroup: () -> Unit,
    @Assisted navigateToGroup: (Long) -> Unit
//    @Assisted navigateToChat: (id:Long) -> Unit,
//    @Assisted openAuthBottomSheet: () -> Unit
){
    CreateSala(
        viewModel = viewModel(factory = viewModelFactory),
        navigateUp = navigateUp,
        reservarInstalacion = reservarInstalacion,
        navigateToCreateGroup = navigateToCreateGroup,
        navigateToGroup = navigateToGroup
//        navigateToChat= navigateToChat,
//        openAuthBottomSheet = openAuthBottomSheet
    )
}

@Composable
internal fun CreateSala(
    viewModel: CreateSalaViewModel,
    navigateUp: () -> Unit,
    reservarInstalacion:@Composable () -> Unit,
    navigateToCreateGroup: () -> Unit,
    navigateToGroup: (Long) -> Unit
//    navigateToChat: (id:Long) -> Unit,
//    openAuthBottomSheet: () -> Unit
){
    val viewState by viewModel.state.collectAsState()
    val formatter = LocalAppDateFormatter.current
    LoaderDialog(loading = viewState.loadingDialog)
    CreateSala(
        viewState = viewState,
        navigateUp = navigateUp,
        reservarInstalacion = reservarInstalacion,
        createSala = {asunto,descripcion,cupos->
            viewModel.createSala(asunto,descripcion,cupos,navigateToGroup)
        },
        clearMessage = viewModel::clearMessage,
        formatShortTime = {formatter.formatShortTime(it)},
        formatDate = {formatter.formatWithSkeleton(it.toEpochMilliseconds(),formatter.monthDaySkeleton)},
        groupExist = viewModel.isGroupExist(),
        getGroupsUser = viewModel::getGroupsUser,
        selectGroup = viewModel::updateSelectedGroup,
        enableButton = viewModel::enableButton,
        navigateToCreateGroup = navigateToCreateGroup
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun CreateSala(
    viewState: CreateSalaState,
    navigateUp: () -> Unit,
    reservarInstalacion:@Composable () -> Unit,
    createSala:(String,String,String)->Unit,
    clearMessage:(id:Long)->Unit,
    formatShortTime:(time: Instant)->String,
    formatDate:(date: Instant)->String,
    groupExist:Boolean,
    getGroupsUser:()->Unit,
    selectGroup:(Long)->Unit,
    enableButton:()->Unit,
    navigateToCreateGroup: () -> Unit
//    onChangeAsunto:(v:String)->Unit,
//    onChangeDescription:(v:String)->Unit,
//    onChangeCupos:(v:String)->Unit,
) {
    var asunto by remember{ mutableStateOf("Sala de juegos") }
    var description by remember{ mutableStateOf("Armemos 2 equipos de 10 ") }
    var cupos by remember{ mutableStateOf("15") }
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
    LaunchedEffect(key1 = pagerState.currentPage, block = {
        when(pagerState.currentPage){
            1 -> enableButton()
            2 -> getGroupsUser()
        }
    })
    BackHandler(enabled = true) {
        when(pagerState.currentPage){
            0 -> navigateUp()
            1 -> coroutineScope.launch { pagerState.animateScrollToPage(0) }
            2 -> coroutineScope.launch { pagerState.animateScrollToPage(1) }

        }
    }

    val showConfirmationDialog = remember{
        mutableStateOf(false)
    }
    val isLastPage by remember{
        derivedStateOf{
            pagerState.currentPage == 1 && groupExist
        }
    }
    DialogConfirmation(open = showConfirmationDialog.value,
        dismiss = { showConfirmationDialog.value = false },
        confirm = {
            createSala(asunto,description,cupos)
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
                Button(modifier = Modifier.align(Alignment.CenterEnd),
                    enabled = viewState.enableToContinue,onClick = {
                    if(isLastPage){
                        showConfirmationDialog.value = true
                    }else if(pagerState.currentPage == 2) {
                        showConfirmationDialog.value = true
                    }else{
                        coroutineScope.launch { pagerState.animateScrollToPage(pagerState.currentPage +1) }
                    }
                }) {
                    Text(text =if(isLastPage) "Crear Sala" else if(pagerState.currentPage == 2) "Crear Sala" else "Continuar")
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
        HorizontalPager(
            pageCount = 3, modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            userScrollEnabled = false,
            state = pagerState
        ) { page ->
            when (page) {

                0 -> Page2(
                    reservarInstalacion = reservarInstalacion,
//                    navigateToPage = { coroutineScope.launch { pagerState.animateScrollToPage(it) } },
                    instalacionCupos = viewState.instalacionCupos,
                    formatDate = formatDate,
                    formatShortTime = formatShortTime,
//                    createSala = { createSala(asunto,description,cupos)}
                )
                1 -> Page1(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    asunto = asunto,
                    description = description,
                    cupos = cupos,
                    onChangeCupos = {cupos = it},
                    onChangeDescription = {description = it},
                    onChangeAsunto = { asunto=it},
                    instalacionCupos = viewState.instalacionCupos,
                    formatDate = formatDate,
                    formatShortTime = formatShortTime,
                )
                2 -> SelectGroup(
                    grupos = viewState.grupos,
                    selectedGroupId = viewState.selectedGroup,
                    selectGroup = selectGroup,
                    loading = viewState.loading,
                    navigateToCreateGroup = navigateToCreateGroup
                )
            }
        }
    }
}




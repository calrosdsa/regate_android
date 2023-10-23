package app.regate.grupos

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import app.regate.common.composes.component.item.GrupoItem
import app.regate.common.composes.util.itemsCustom
import app.regate.common.composes.viewModel
import app.regate.constant.Route
import app.regate.data.auth.AppAuthState
import app.regate.data.dto.empresa.grupo.GrupoDto
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

typealias FilterGroups = @Composable (
    navigateToGroup:(id:Long)->Unit,
    navigateToInfoGrupo:(id:Long) ->Unit,
    openAuthBottomSheet:()->Unit,
//    navigateToSignUpScreen:() -> Unit,

) -> Unit

@Inject
@Composable
fun FilterGroups (
    viewModelFactory:()-> GruposViewModel,
    @Assisted navigateToGroup: (id: Long) -> Unit,
    @Assisted navigateToInfoGrupo: (id: Long) -> Unit,
    @Assisted openAuthBottomSheet: () -> Unit,
//    @Assisted navigateToReserva: (id:Long) -> Unit,
//    viewModelFactory:()->ReservasViewModel
){
    FilterGroups(
        viewModel = viewModel(factory = viewModelFactory),
        navigateToGroup = navigateToGroup,
        navigateToInfoGrupo = navigateToInfoGrupo,
        openAuthBottomSheet = openAuthBottomSheet,
    )
}


@Composable
internal fun FilterGroups (
    viewModel: GruposViewModel,
    navigateToGroup: (id: Long) -> Unit,
    navigateToInfoGrupo: (id: Long) -> Unit,
    openAuthBottomSheet: () -> Unit

){
    val state by viewModel.state.collectAsState()

    FilterGroups(
        viewState = state,
        navigateToGroup = navigateToGroup,
        lazyPagingItems = viewModel.pagingList.collectAsLazyPagingItems(),
        clearMessage = viewModel::clearMessage,
        joinToGroup = viewModel::joinToGroup,
        navigateToInfoGrupo = navigateToInfoGrupo,
        openAuthBottomSheet = openAuthBottomSheet
    )
}

@Composable
internal fun FilterGroups(
    viewState:GruposState,
    navigateToGroup:(id:Long)->Unit,
    lazyPagingItems: LazyPagingItems<GrupoDto>,
    clearMessage:(id:Long)->Unit,
    joinToGroup: (Long,Int)->Unit,
    navigateToInfoGrupo: (id: Long) -> Unit,
    openAuthBottomSheet: () -> Unit
//    modifier: Modifier = Modifier
){
    val snackbarHostState = remember { SnackbarHostState() }
    viewState.message?.let { messageSnack ->
        LaunchedEffect(messageSnack) {
            snackbarHostState.showSnackbar(messageSnack.message)
            clearMessage(messageSnack.id)
        }
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) {paddingValues->
    LazyColumn(modifier = Modifier.padding(paddingValues)){
        itemsCustom(
            items = lazyPagingItems,
        ) { result ->
            if (result != null) {
                val grupoU = viewState.userGroups.find { it.group_id == result.id }

                if(grupoU != null){
                    GrupoItem(
                        navigate = navigateToGroup,
                        grupo = result.copy(
                            grupo_request_estado = grupoU.request_estado.ordinal
                        ),
                        joinToGroup = {it1,it2->
                            if(viewState.authState == AppAuthState.LOGGED_IN) {
                                joinToGroup(it1, it2)
                            }else{
                                openAuthBottomSheet()
                            }
                        },
                        navigateToInfoGrupo = navigateToInfoGrupo
                    )
                }else{
                GrupoItem(
                    navigate = navigateToInfoGrupo,
                    navigateToInfoGrupo = navigateToInfoGrupo,
                    grupo = result,
                    joinToGroup = {it1,it2->
                        if(viewState.authState == AppAuthState.LOGGED_IN){
                        joinToGroup(it1,it2)
                        }else{
                            openAuthBottomSheet()
                        }
                    }
                )
                }

            }
        }
    }
    }
}

//@Composable
//fun GrupoItemDto(
//    grupo: GrupoDto,
//    navigateToChatGrupo: (id: Long) -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Row(
//        modifier = modifier
//            .fillMaxWidth()
//            .clickable { navigateToChatGrupo(grupo.id) }
//            .padding(10.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        PosterCardImage(
//            model = grupo.photo, modifier = Modifier
//                .size(70.dp), shape = CircleShape
//        )
//        Spacer(modifier = Modifier.width(10.dp))
//        Text(text = grupo.name, style = MaterialTheme.typography.titleMedium)
//    }
//}


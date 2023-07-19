package app.regate.grupos

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import app.regate.common.composes.ui.PosterCardImage
import app.regate.common.composes.util.itemsCustom
import app.regate.common.composes.viewModel
import app.regate.compoundmodels.MessageProfile
import app.regate.data.dto.empresa.grupo.GrupoDto
import app.regate.usergroups.UserGroupsViewModel
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

typealias FilterGroups = @Composable (
    navigateToGroup:(id:Long)->Unit,
//    navigateToSignUpScreen:() -> Unit,

) -> Unit

@Inject
@Composable
fun FilterGroups (
    viewModelFactory:()-> GruposViewModel,
    @Assisted navigateToGroup: (id: Long) -> Unit,
//    @Assisted navigateToReserva: (id:Long) -> Unit,
//    viewModelFactory:()->ReservasViewModel
){
    FilterGroups(
        viewModel = viewModel(factory = viewModelFactory),
        navigateToGroup = navigateToGroup
    )
}


@Composable
internal fun FilterGroups (
    viewModel: GruposViewModel,
    navigateToGroup: (id: Long) -> Unit
){
    val state by viewModel.state.collectAsState()

    FilterGroups(
        viewState = state,
        navigateToGroup = navigateToGroup,
        lazyPagingItems = viewModel.pagingList.collectAsLazyPagingItems(),
        clearMessage = viewModel::clearMessage
    )
}

@Composable
internal fun FilterGroups(
    viewState:GruposState,
    navigateToGroup:(id:Long)->Unit,
    lazyPagingItems: LazyPagingItems<GrupoDto>,
    clearMessage:(id:Long)->Unit
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
            key = { it.id }
        ){result->
            if (result != null) {
                GrupoItemDto(grupo = result, navigateToChatGrupo = navigateToGroup)
            }
        }
    }
    }
}

@Composable
fun GrupoItemDto(
    grupo: GrupoDto,
    navigateToChatGrupo: (id: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { navigateToChatGrupo(grupo.id) }
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PosterCardImage(
            model = grupo.photo, modifier = Modifier
                .size(70.dp), shape = CircleShape
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = grupo.name, style = MaterialTheme.typography.titleMedium)
    }
}


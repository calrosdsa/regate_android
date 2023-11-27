package app.regate.grupos.userinvitations

import InvitationEstado
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import app.regate.common.composes.ui.PosterCardImage
import app.regate.common.composes.ui.SimpleTopBar
import app.regate.common.composes.util.itemsCustom
import app.regate.common.composes.viewModel
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import app.regate.common.resources.R
import app.regate.compoundmodels.grupo.UserInvitation
import app.regate.data.dto.empresa.grupo.UserGrupoRequesEstado
import app.regate.data.dto.empresa.grupo.UserGrupoRequestDto

typealias UserInvitations= @Composable (
    navigateUp:() ->Unit,
) -> Unit

@Inject
@Composable
fun UserInvitations(
    viewModelFactory:() -> UserInvitationsViewModel,
    @Assisted navigateUp: () -> Unit
){
    UserInvitations(
        viewModel = viewModel(factory =  viewModelFactory),
        navigateUp = navigateUp
    )
}

@Composable
internal fun UserInvitations(
    viewModel: UserInvitationsViewModel,
    navigateUp: () -> Unit
){
    val state by viewModel.state.collectAsState()
    UserInvitations(
        navigateUp = navigateUp,
        viewState = state,
        clearMessage = viewModel::clearMessage,
        pagingItems = viewModel.pagedList.collectAsLazyPagingItems(),
        declineInvitation = viewModel::declineInvitation,
        acceptInvitation = viewModel::acceptInvitation
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun UserInvitations(
    viewState: UserInvitationsState,
    pagingItems: LazyPagingItems<UserInvitation>,
    clearMessage:(Long)->Unit,
    navigateUp: () -> Unit,
    acceptInvitation:(Long,Long)->Unit,
    declineInvitation:(Long,Long)->Unit
){
    val snackbarHostState = remember { SnackbarHostState() }
    viewState.message?.let { message->
        LaunchedEffect(key1 = message, block = {
            snackbarHostState.showSnackbar(message.message)
            clearMessage(message.id)
        })
    }
    Scaffold(
        topBar = { SimpleTopBar(navigateUp = navigateUp, title = stringResource(id = R.string.invitations))},
        snackbarHost = { SnackbarHost(hostState = snackbarHostState)}
    ) {paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            content = {
            itemsCustom(
                items = pagingItems
            ){item->
                if(item != null){
                    UserGrupoInvitationItem(
                        item = item,
                        acceptInvitation = acceptInvitation,
                        declineInvitation = declineInvitation
                    )
                }

            }
        })
    }
}

@Composable
internal fun UserGrupoInvitationItem(
    item: UserInvitation,
    declineInvitation: (Long, Long) -> Unit,
    acceptInvitation: (Long, Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
//            .clickable { navigate(item.id) }
            .padding(10.dp),
    ) {
        PosterCardImage(
            model = item.grupo?.photo, modifier = Modifier
                .size(70.dp), shape = CircleShape
        )
        Spacer(modifier = Modifier.width(10.dp))

        Column() {
            Text(text = item.grupo?.name ?: "", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(5.dp))
            when(item.invitation.estado){
                InvitationEstado.NONE.ordinal ->{
                    Text(text = "Invitacion declinada", style = MaterialTheme.typography.titleMedium)
                }
                InvitationEstado.ACEPTADO.ordinal ->{
                    Text(text = "Invitacion aceptada", style = MaterialTheme.typography.titleMedium)
                }
                InvitationEstado.PENDIENTE.ordinal ->{
            Row(horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = { declineInvitation(item.invitation.profile_id,item.invitation.grupo_id) },
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.fillMaxWidth(0.4f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.inverseOnSurface,
                        contentColor = MaterialTheme.colorScheme.inverseSurface
                    )
                ) {
                    Text(text = stringResource(id = R.string.decline),
                    maxLines =1)
                }
                Spacer(modifier = Modifier.width(20.dp))
                Button(
                    onClick = { acceptInvitation(item.invitation.profile_id,item.invitation.grupo_id) },
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.fillMaxWidth(0.9f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                ) {
                    Text(text = stringResource(id = R.string.confirm),
                    maxLines = 1)
                }

            }

        }
            }
        }



    }
}

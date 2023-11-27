package app.regate.grupos.user_requests

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import app.regate.common.composes.ui.PosterCardImage
import app.regate.common.composes.ui.SimpleTopBar
import app.regate.common.composes.util.itemsCustom
import app.regate.common.composes.viewModel
import app.regate.common.resources.R
import app.regate.data.dto.empresa.grupo.UserGrupoRequesEstado
import app.regate.data.dto.empresa.grupo.UserGrupoRequestDto
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

typealias UserGrupoRequests = @Composable (
    navigateUp:()->Unit
) ->Unit

@Inject
@Composable
fun UserGrupoRequests(
    viewModelFactory:()->UserGrupoRequestsViewModel,
    @Assisted navigateUp: () -> Unit
){
    UserGrupoRequests(
        viewModel = viewModel(factory = viewModelFactory),
        navigateUp = navigateUp
    )
}

@Composable
internal fun UserGrupoRequests(
    viewModel: UserGrupoRequestsViewModel,
    navigateUp: () -> Unit
){
    val state by viewModel.state.collectAsState()
    UserGrupoRequests(
        viewState =state,
        navigateUp = navigateUp,
        lazyPagingItems = viewModel.pagedList.collectAsLazyPagingItems(),
        declineRequest = viewModel::cancelRequest,
//        confirmPendingRequest = viewModel::confirmRequest
    ) 
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun UserGrupoRequests(
    viewState: UserGrupoRequestsState,
    lazyPagingItems:LazyPagingItems<UserGrupoRequestDto>,
    navigateUp: () -> Unit,
    declineRequest:(Int,Int)->Unit,
//    confirmPendingRequest:(Int)->Unit
){
    Scaffold(
        topBar = {
            SimpleTopBar(
                navigateUp =navigateUp,
                title = stringResource(id = R.string.pending_requests)
            )
        }
    ) {paddingValues ->  
        Box(modifier = Modifier.padding(paddingValues)){
            LazyColumn(content = {
                itemsCustom(
                    items = lazyPagingItems
                ){item->
                    if(item != null){
                        UserGrupoRequestItem(
                            item = item,
                            declineRequest = declineRequest,
                            isDeclined = viewState.declineRequestIds.toList().contains(item.grupo_id),
                        )
//                   PendingRequestUserItem(
//                       item = item,
//                       isDeclined = viewState.declineRequestIds.toList().contains(item.profile_id),
//                       isAccepted = viewState.confirmRequetsIds.toList().contains(item.profile_id),
//                       declineRequest = {declineRequest(it,item.grupo_id)},
//                   )
                    }
                }
            })
        }
    }
}

@Composable
internal fun UserGrupoRequestItem(
    item: UserGrupoRequestDto,
    isDeclined:Boolean,
//    navigate:(Long)->Unit,
    modifier: Modifier = Modifier,
    declineRequest: (profileId:Int, grupoId:Int) -> Unit,
//    navigateToInfoGrupo:(Long)->Unit={}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
//            .clickable { navigate(item.id) }
            .padding(10.dp),
    ) {
        PosterCardImage(
            model = item.grupo_photo, modifier = Modifier
                .size(70.dp), shape = CircleShape
        )
        Spacer(modifier = Modifier.width(10.dp))

        Column() {
            Text(text = item.grupo_name, style = MaterialTheme.typography.titleMedium)
            when (item.estado) {
                UserGrupoRequesEstado.PENDING.ordinal -> {

                    if (isDeclined) {
                        Text(
                            text = stringResource(id = R.string.request_removed),
                            style = MaterialTheme.typography.titleMedium
                        )
                    } else {
                        Button(
                            onClick = { declineRequest(item.profile_id, item.grupo_id) },
                            shape = MaterialTheme.shapes.small,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.primary
                            ),
                        ) {
                            Text(text = stringResource(id = R.string.cancel_request))
                        }
                    }

                }
                UserGrupoRequesEstado.DECLINED.ordinal -> {
                    Text(
                        text = stringResource(id = R.string.request_declined),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                UserGrupoRequesEstado.ACCEPTED.ordinal -> {
                    Text(
                        text = stringResource(id = R.string.request_accepted),
                        style = MaterialTheme.typography.titleMedium
                    )
                }

            }
        }
    }
}

//@Composable
//internal fun PendingRequestUserItem(
//    item:PendingRequestUser,
//    isDeclined:Boolean,
//    isAccepted:Boolean,
//    declineRequest: (Int) -> Unit
//){
//    Row(
//        modifier = Modifier
//            .clickable {}
//            .padding(10.dp)
//            .fillMaxWidth(),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//
//        ProfileImage (
//            profileImage = item.profile_photo,
//            modifier = Modifier
//                .clip(CircleShape)
//                .size(70.dp),
//            contentDescription = null,
//        )
//        Spacer(modifier = Modifier.width(10.dp))
//        Column(modifier = Modifier.padding(horizontal = 10.dp)) {
//            Text(
//                text = "${item.nombre} ${item.apellido ?: ""}", style = MaterialTheme.typography.titleMedium,
//                maxLines = 1, overflow = TextOverflow.Ellipsis,
//            )
//            Spacer(modifier = Modifier.height(5.dp))
//
//            if(!isDeclined && !isAccepted){
//            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
////                Button(onClick = { confirmPendingRequest(item.profile_id) },
////                shape = MaterialTheme.shapes.small) {
////                    Text(text = stringResource(id = R.string.confirm))
////                }
//                Button(onClick = { declineRequest(item.profile_id) },
//                    shape = MaterialTheme.shapes.small,
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = MaterialTheme.colorScheme.inverseOnSurface,
//                        contentColor = MaterialTheme.colorScheme.inverseSurface
//                    )
//                ) {
//                    Text(text = stringResource(id = R.string.decline))
//                }
//            }
//            }
//
//            if(isDeclined){
//                Text(text = stringResource(id = R.string.request_removed),
//                style = MaterialTheme.typography.titleMedium)
//            }
//
//            if(isAccepted){
//                Text(text = stringResource(id = R.string.request_accepted),
//                    style = MaterialTheme.typography.titleMedium)
//            }
//
//
//        }
//    }
//}
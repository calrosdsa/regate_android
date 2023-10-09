package app.regate.grupo.pending

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import app.regate.common.composes.component.images.ProfileImage
import app.regate.common.composes.ui.CommonTopBar
import app.regate.common.composes.ui.SimpleTopBar
import app.regate.common.composes.util.itemsCustom
import app.regate.common.composes.viewModel
import app.regate.common.resources.R
import app.regate.data.dto.empresa.grupo.PendingRequestUser
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

typealias PendingRequests = @Composable (
    navigateUp:()->Unit
) ->Unit

@Inject
@Composable
fun PendingRequests(
    viewModelFactory:(SavedStateHandle)->PendingRequestsViewModel,
    @Assisted navigateUp: () -> Unit
){
    PendingRequests(
        viewModel = viewModel(factory = viewModelFactory),
        navigateUp = navigateUp
    )
}

@Composable
internal fun PendingRequests(
    viewModel: PendingRequestsViewModel,
    navigateUp: () -> Unit
){
    val state by viewModel.state.collectAsState()
    PendingRequests(
        viewState =state,
        navigateUp = navigateUp,
        lazyPagingItems = viewModel.pagedList.collectAsLazyPagingItems(),
        declineRequest = viewModel::declineRequest,
        confirmPendingRequest = viewModel::confirmRequest
    ) 
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PendingRequests(
    viewState: PendingRequestsState,
    lazyPagingItems:LazyPagingItems<PendingRequestUser>,
    navigateUp: () -> Unit,
    declineRequest:(Int)->Unit,
    confirmPendingRequest:(Int)->Unit
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

                   PendingRequestUserItem(
                       item = item,
                       isDeclined = viewState.declineRequestIds.toList().contains(item.profile_id),
                       isAccepted = viewState.confirmRequetsIds.toList().contains(item.profile_id),
                       declineRequest = declineRequest,
                       confirmPendingRequest = confirmPendingRequest
                   )
                    }
                }
            })
        }
    }
}

@Composable
internal fun PendingRequestUserItem(
    item:PendingRequestUser,
    isDeclined:Boolean,
    isAccepted:Boolean,
    confirmPendingRequest: (Int) -> Unit,
    declineRequest: (Int) -> Unit
){
    Row(
        modifier = Modifier
            .clickable {}
            .padding(10.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        ProfileImage (
            profileImage = item.profile_photo,
            modifier = Modifier
                .clip(CircleShape)
                .size(70.dp),
            contentDescription = null,
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.padding(horizontal = 10.dp)) {
            Text(
                text = "${item.nombre} ${item.apellido ?: ""}", style = MaterialTheme.typography.titleMedium,
                maxLines = 1, overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(5.dp))

            if(!isDeclined && !isAccepted){
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Button(onClick = { confirmPendingRequest(item.profile_id) },
                shape = MaterialTheme.shapes.small) {
                    Text(text = stringResource(id = R.string.confirm))
                }
                Button(onClick = { declineRequest(item.profile_id) },
                    shape = MaterialTheme.shapes.small,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.inverseOnSurface,
                        contentColor = MaterialTheme.colorScheme.inverseSurface
                    )
                ) {
                    Text(text = stringResource(id = R.string.decline))
                }
            }
            }
            
            if(isDeclined){
                Text(text = stringResource(id = R.string.request_removed),
                style = MaterialTheme.typography.titleMedium)
            }

            if(isAccepted){
                Text(text = stringResource(id = R.string.request_accepted),
                    style = MaterialTheme.typography.titleMedium)
            }


        }
    }
}
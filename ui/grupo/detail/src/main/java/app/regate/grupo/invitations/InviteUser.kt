package app.regate.grupo.invitations

import InvitationEstado
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import app.regate.common.composes.component.button.SmallButton
import app.regate.common.composes.component.images.ProfileImage
import app.regate.common.composes.component.input.SearchInput
import app.regate.common.composes.ui.Loader
import app.regate.common.composes.util.itemsCustom
import app.regate.common.composes.viewModel
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import app.regate.common.resources.R
import app.regate.compoundmodels.grupo.UserInvitationGrupo
import app.regate.data.dto.account.user.ProfileDto

typealias InviteUser = @Composable (
    navigateUp:()->Unit,
    navigateToProfile:(Long)->Unit
        ) -> Unit

@Inject
@Composable
fun InviteUser(
    viewModelFactory:(SavedStateHandle)->InviteUserViewModel,
    @Assisted navigateUp: () -> Unit,
    @Assisted navigateToProfile: (Long) -> Unit
) {
    InviteUser(
        viewModel = viewModel(factory = viewModelFactory),
        navigateUp = navigateUp,
        navigateToProfile = navigateToProfile
    )
}

@Composable
internal fun InviteUser(
    viewModel: InviteUserViewModel,
    navigateUp: () -> Unit,
    navigateToProfile: (Long) -> Unit,
){
    val state by viewModel.state.collectAsState()
    InviteUser(
        viewState = state,
        navigateUp = navigateUp,
        lazyPagingItems = viewModel.pagedList.collectAsLazyPagingItems(),
        lazyPagingItemsInvitation = viewModel.pagedListInvitations.collectAsLazyPagingItems(),
        clearMessage = viewModel::clearMessage,
        navigateToProfile = navigateToProfile,
        search = viewModel::search,
        sendInvitation = viewModel::sendInvitation,
        cancelInvitation = viewModel::cancelInvitation,
//        addValue = viewModel::addValue,
    )
}

@Composable
internal fun InviteUser(
    viewState: InviteUserState,
    lazyPagingItems: LazyPagingItems<ProfileDto>,
    lazyPagingItemsInvitation: LazyPagingItems<UserInvitationGrupo>,
    clearMessage:(Long)->Unit,
    search:(String)->Unit,
//    addValue:(Long,Int)->Unit,
    sendInvitation: (Long,()->Unit) -> Unit,
    cancelInvitation: (Long,()->Unit) -> Unit,
    navigateUp: () -> Unit,
    navigateToProfile: (Long) -> Unit,
){
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    var query by remember { mutableStateOf("") }
    val interactionSource = remember { MutableInteractionSource() }
    val invitationIds = remember {
        mutableStateMapOf<Long,Int>()
    }
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(key1 = viewState.filterData, block = {
        if(viewState.filterData.query.isNotBlank()){
            lazyPagingItems.refresh()
        }
    })
    LaunchedEffect(key1 = lazyPagingItemsInvitation.itemSnapshotList,block = {
        lazyPagingItemsInvitation.itemSnapshotList.items.map {
            if(!it.profile?.let { it1 -> invitationIds.contains(it1.id) }!!){
//                addValue(it.profile!!.id,it.invitation.estado)
                invitationIds[it.profile!!.id] = it.invitation.estado
            }
        }
    })
    viewState.message?.let {message->
        LaunchedEffect(key1 = message, block = {
            snackbarHostState.showSnackbar(message.message)
            clearMessage(message.id)
        })
    }
    var shouldShowResults by rememberSaveable {
        mutableStateOf(false)
    }
    fun onSearch(searchQuery: String) {
        search(searchQuery)
        focusManager.clearFocus(true)
        shouldShowResults = true
    }
    Scaffold(
        topBar = {
                 SearchInput(
                     query = query,
                     focusRequester = focusRequester,
                     interactionSource = interactionSource,
                     onSearch = {onSearch(it)},
                     onChange = {query = it},
                     navigateUp = navigateUp,
                     shouldShowResults = {shouldShowResults = false}
                 )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState)}
    ) {paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)){
        if (shouldShowResults) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                if (lazyPagingItems.loadState.refresh == LoadState.Loading) {
                    item {
                        Loader()
                    }
                } else {
                    if (lazyPagingItems.itemCount == 0) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 40.dp)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.no_results_for_saerch),
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .fillMaxWidth(0.8f),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
                itemsCustom(
                    items = lazyPagingItems
                ) { item ->
                    if (item != null) {
                        ProfileInviteUser(
                            id = item.profile_id,
                            photo = item.profile_photo,
                            nombre = item.nombre,
                            apellido = item.apellido,
                            navigateToProfile = navigateToProfile,
                            sendInvitation = {sendInvitation(it) {invitationIds[it] = InvitationEstado.PENDIENTE.ordinal } },
                            estado = invitationIds[item.profile_id] ?: 0,
                            cancelInvitation = { id ->
                                cancelInvitation(id) { invitationIds.remove(id) }
                            }
                        )
                    }
                }

                item {
                    if (lazyPagingItems.loadState.append == LoadState.Loading) {
                        Loader()
                    }
                }
            }
        }else{
            LazyColumn(content = {
                itemsCustom(
                    items = lazyPagingItemsInvitation,
                ){item ->
                    if(item!= null){
                        item.profile?.let {profile->
                            ProfileInviteUser(
                                id = profile.id,
                                photo =profile.profile_photo ,
                                nombre = profile.nombre,
                                apellido = profile.apellido,
                                sendInvitation = { sendInvitation(it,{})},
                                navigateToProfile = {},
                                estado = item.invitation.estado,
                                cancelInvitation = {cancelInvitation(it,{})}
                            )
                        }
                    }
                }
            })
        }
    }
    }

}

@Composable
internal fun ProfileInviteUser(
    id:Long,
    photo:String?,
    nombre:String,
    apellido:String?,
    estado:Int,
    sendInvitation:(Long)->Unit,
    cancelInvitation:(Long)->Unit,
    navigateToProfile:(Long)->Unit,
    modifier:Modifier = Modifier,
    isMe:Boolean = false,
) {
    Row(
        modifier = modifier
            .clickable { navigateToProfile(id) }
            .padding(vertical = 10.dp, horizontal = 5.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(0.8f)) {
            ProfileImage(
                profileImage = photo,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(40.dp),
                contentDescription = nombre,
            )
            Spacer(modifier = Modifier.width(10.dp))
            if (isMe) {
                Text(
                    text = stringResource(id = R.string.you),
                    style = MaterialTheme.typography.labelLarge,
                )
            } else {
                Text(
                    text = "$nombre ${apellido ?: ""}", style = MaterialTheme.typography.labelLarge,
                    maxLines = 1, overflow = TextOverflow.Ellipsis
                )
            }
        }

        when(estado){
            InvitationEstado.PENDIENTE.ordinal ->{
            SmallButton(
            text = "Cancelar",
            onClick = {cancelInvitation(id)},
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.inverseOnSurface,
                    contentColor = MaterialTheme.colorScheme.inverseSurface
                )
        )
            }
            InvitationEstado.NONE.ordinal -> {
                SmallButton(
                    text = "Invitar",
                    onClick = {sendInvitation(id)}
                )
            }
        }

    }
}

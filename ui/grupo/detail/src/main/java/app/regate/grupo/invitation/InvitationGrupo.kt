package app.regate.grupo.invitation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import app.regate.common.composes.ui.CommonTopBar
import app.regate.common.composes.ui.Loader
import app.regate.common.composes.ui.PosterCardImage
import app.regate.common.composes.viewModel
import app.regate.common.resources.R
import app.regate.data.auth.AppAuthState
import app.regate.data.common.encodeMediaData
import app.regate.data.dto.empresa.grupo.GrupoRequestEstado
import app.regate.data.dto.empresa.grupo.GrupoVisibility
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

typealias InvitationGrupo = @Composable (
    navigateUp:()->Unit,
    navigateToPhoto:(String)->Unit,
    openAuthBottomSheet:()->Unit,
    navigateToGroup: (Long) -> Unit,
    ) -> Unit

@Inject
@Composable
fun InvitationGrupo(
    viewModelFactory:(SavedStateHandle)-> InvitationGrupoViewModel,
    @Assisted navigateUp: () -> Unit,
    @Assisted navigateToPhoto: (String) -> Unit,
    @Assisted openAuthBottomSheet:()->Unit,
    @Assisted navigateToGroup: (Long) -> Unit,
    ){
    InvitationGrupo(
        viewModel = viewModel(factory = viewModelFactory),
        navigateUp = navigateUp,
        navigateToPhoto = navigateToPhoto,
        openAuthBottomSheet = openAuthBottomSheet,
        navigateToGroup = navigateToGroup,
    )
}

@Composable
internal fun InvitationGrupo(
    viewModel: InvitationGrupoViewModel,
    navigateUp: () -> Unit,
    navigateToPhoto: (String) -> Unit,
    openAuthBottomSheet: () -> Unit,
    navigateToGroup: (Long) -> Unit,
){
    val state by viewModel.state.collectAsState()
    val loading by viewModel.loadingCounter.observable.collectAsState(initial = false)
    InvitationGrupo(
        viewState = state,
        navigateUp = navigateUp,
        navigateToPhoto = navigateToPhoto,
//        cancelRequest = viewModel::cancelRequest,
        joinToGroup = viewModel::joinToGroup,
        openAuthBottomSheet = openAuthBottomSheet,
        loading = loading,
        navigateToGroup = navigateToGroup
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun InvitationGrupo(
    viewState: InvitationGrupoState,
    loading:Boolean,
    navigateUp: () -> Unit,
    navigateToPhoto: (String) -> Unit,
//    cancelRequest:(Long)->Unit,
    joinToGroup:(Int)->Unit,
    openAuthBottomSheet: () -> Unit,
    navigateToGroup: (Long) -> Unit,
) {
    Scaffold(modifier = Modifier
        .fillMaxHeight(0.5f)
        .padding(horizontal = 10.dp)
        .padding(bottom = 10.dp),
        bottomBar = {
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = {
                    if (viewState.authState == AppAuthState.LOGGED_IN) {

                        if (viewState.myGroup == null) {
                            viewState.grupo?.let { joinToGroup(GrupoVisibility.PUBLIC.ordinal) }
                        } else {
                            when (viewState.myGroup.request_estado) {
                                GrupoRequestEstado.JOINED -> viewState.grupo?.let {
                                    navigateToGroup(
                                        it.id)
                                }
                                GrupoRequestEstado.PENDING -> viewState.grupo?.let { joinToGroup(GrupoVisibility.PUBLIC.ordinal) }
                                GrupoRequestEstado.NONE -> {}
                            }
                        }
                    } else {
                        openAuthBottomSheet()
                    }
                },
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary
                ),
            ) {
                if (viewState.myGroup == null) {
                    Text(text = stringResource(id = R.string.join_a_group))
                } else {
                    when (viewState.myGroup.request_estado) {
                        GrupoRequestEstado.JOINED -> {
                            Text(text = stringResource(id = R.string.visit))
                        }

//                        GrupoRequestEstado.PENDING -> {
//                            Text(text = stringResource(id = R.string.cancel_request))
//                        }

                        else -> {
                            Text(text = stringResource(id = R.string.join_a_group))
                        }
                    }
                }
            }
        }
    ) { paddingValues ->

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            if (loading) {
                Loader(modifier = Modifier.align(Alignment.Center))
            }
            Column(modifier = Modifier) {

                Box(modifier = Modifier.fillMaxWidth()) {
                    BottomSheetDefaults.DragHandle(modifier = Modifier
                        .clickable {
                            navigateUp()
                        }
                        .align(Alignment.Center))
                }

                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {


                    viewState.grupo?.let { grupo ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            PosterCardImage(model = grupo.photo,
                                modifier = Modifier
                                    .size(45.dp),
                                shape = CircleShape,
                                onClick = {
                                    val payload =
                                        Json.encodeToString(encodeMediaData(listOf(grupo.photo.toString())))
                                    navigateToPhoto(payload)
                                }
                            )

                            Column(modifier = Modifier.padding(horizontal = 15.dp)) {
                                Spacer(modifier = Modifier.height(0.dp))
                                Text(
                                    text = grupo.name,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    when (grupo.visibility) {
                                        GrupoVisibility.PUBLIC.ordinal -> {
                                            Icon(
                                                imageVector = Icons.Filled.Public,
                                                contentDescription = null,
                                                modifier = Modifier.size(15.dp)
                                            )
                                            Spacer(modifier = Modifier.width(3.dp))
                                            Text(
                                                text = stringResource(id = R.string.publico),
                                                style = MaterialTheme.typography.labelMedium
                                            )
                                            Spacer(modifier = Modifier.width(3.dp))
                                            Text(
                                                text = stringResource(
                                                    id = R.string.members,
                                                    grupo.members
                                                ),
                                                style = MaterialTheme.typography.labelMedium
                                            )
                                        }

                                        GrupoVisibility.PRIVATE.ordinal -> {
                                            Icon(
                                                imageVector = Icons.Filled.Lock,
                                                contentDescription = null,
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Spacer(modifier = Modifier.width(3.dp))
                                            Text(
                                                text = stringResource(id = R.string.privado),
                                                style = MaterialTheme.typography.labelMedium
                                            )
                                            Spacer(modifier = Modifier.width(3.dp))
                                            Text(
                                                text = stringResource(
                                                    id = R.string.members,
                                                    grupo.members
                                                ),
                                                style = MaterialTheme.typography.labelMedium
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        grupo.descripcion?.let {
                            Spacer(modifier = Modifier.height(15.dp))
                            Text(
                                text = stringResource(id = R.string.description),
                                style = MaterialTheme.typography.labelLarge
                            )
                            Text(text = it, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }


            }
        }
    }
}

//@Composable
//internal fun GrupoInfo(
//    grupo:GrupoDto,
//){
//
//}

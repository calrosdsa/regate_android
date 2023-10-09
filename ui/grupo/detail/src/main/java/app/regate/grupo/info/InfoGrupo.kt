package app.regate.grupo.info

import android.graphics.drawable.shapes.Shape
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.paging.LoadState
import app.regate.common.composes.ui.CommonTopBar
import app.regate.common.composes.ui.PosterCardImage
import app.regate.common.composes.viewModel
import app.regate.common.resources.R
import app.regate.data.common.encodeMediaData
import app.regate.data.dto.empresa.grupo.GrupoRequestEstado
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

typealias InfoGrupo = @Composable (
    navigateUp:()->Unit,
    navigateToPhoto:(String)->Unit
        ) -> Unit

@Inject
@Composable
fun InfoGrupo(
    viewModelFactory:(SavedStateHandle)->InfoGrupoViewModel,
    @Assisted navigateUp: () -> Unit,
    @Assisted navigateToPhoto: (String) -> Unit
){
    InfoGrupo(
        viewModel = viewModel(factory = viewModelFactory),
        navigateUp = navigateUp,
        navigateToPhoto = navigateToPhoto
    )
}

@Composable
internal fun InfoGrupo(
    viewModel: InfoGrupoViewModel,
    navigateUp: () -> Unit,
    navigateToPhoto: (String) -> Unit
){
    val state by viewModel.state.collectAsState()
    InfoGrupo(
        viewState = state,
        navigateUp = navigateUp,
        refresh = viewModel::getData,
        navigateToPhoto = navigateToPhoto,
        cancelRequest = viewModel::cancelRequest,
        joinToGroup = viewModel::joinToGroup

    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun InfoGrupo(
    viewState:InfoGrupoState,
    navigateUp: () -> Unit,
    refresh:()->Unit,
    navigateToPhoto: (String) -> Unit,
    cancelRequest:()->Unit,
    joinToGroup:(Int)->Unit
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = viewState.loading,
        onRefresh = { refresh() }
    )
    Scaffold(
        topBar = {
            CommonTopBar(
                onBack = navigateUp
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .pullRefresh(pullRefreshState)
        ) {
            Column() {
//                Text(text = viewState.grupo?.name.toString())
                viewState.grupo?.let { grupo ->
                    PosterCardImage(model = grupo.photo,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(230.dp),
                        shape = RoundedCornerShape(0.dp),
                        onClick = {
                            val payload = Json.encodeToString(encodeMediaData(listOf(grupo.photo.toString())))
                            navigateToPhoto(payload)
                        }
                    )
                    Column(modifier = Modifier.padding(horizontal = 15.dp)) {

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = grupo.name,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        ))
                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            if(viewState.myGroup == null){
                                joinToGroup(grupo.visibility)
                            }else{
                           when(viewState.myGroup.request_estado){
                               GrupoRequestEstado.JOINED->joinToGroup(grupo.visibility)
                               GrupoRequestEstado.PENDING-> cancelRequest()
                               GrupoRequestEstado.NONE->{}
                           }
                            }
                        },
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                    ) {
                        if(viewState.myGroup == null){
                        Text(text = stringResource(id = R.string.join_a_group))
                        }else{
                        when(viewState.myGroup.request_estado){
                            GrupoRequestEstado.JOINED -> {
                                Text(text = stringResource(id = R.string.visit))
                            }
                            GrupoRequestEstado.PENDING -> {
                                Text(text = stringResource(id = R.string.cancel_request))
                            }
                            GrupoRequestEstado.NONE-> {
                                Text(text = stringResource(id = R.string.join_a_group))
                            }
                        }
                        }

                    }
                    }


                }
            }

            PullRefreshIndicator(
                refreshing = viewState.loading,
                state = pullRefreshState,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(paddingValues),
                scale = true
            )

        }


    }
}

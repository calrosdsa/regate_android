package app.regate.usergroups.mygroups

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import app.regate.common.composes.ui.PosterCardImage
import app.regate.common.composes.ui.SimpleTopBar
import app.regate.common.composes.viewModel
import app.regate.grupos.GrupoItem
import app.regate.models.Grupo
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

typealias MyGroups = @Composable (
    navigateUp:()->Unit,
    navigateToChatGrupo:(Long,String)->Unit
)-> Unit

@Inject
@Composable
fun MyGroups(
    viewModelFactory:(SavedStateHandle)->MyGroupsViewModel,
    @Assisted navigateUp: () -> Unit,
    @Assisted navigateToChatGrupo: (Long, String) -> Unit
){
    MyGroups(viewModel = viewModel(factory = viewModelFactory),
        navigateUp = navigateUp,
        navigateToChatGrupo = navigateToChatGrupo)
}

@Composable
internal fun MyGroups(
    viewModel: MyGroupsViewModel,
    navigateUp: () -> Unit,
    navigateToChatGrupo: (Long, String) -> Unit
){
    val state by viewModel.state.collectAsState()
    MyGroups(
        viewState = state,
        navigateToChat = {
            viewModel.navigateToGroupChat(it,navigateToChatGrupo)
        },
        navigateUp = navigateUp
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MyGroups(
    viewState:MyGroupsState,
    navigateToChat: (id: Long) -> Unit,
    navigateUp: () -> Unit
){
    Scaffold(
        topBar = {
            SimpleTopBar(navigateUp = navigateUp)
        }
    ) {paddingValues->
    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)){
        items(items = viewState.grupos, key = {it.id}){item->
            GrupoItem(grupo = item, navigateToChatGrupo = navigateToChat)
        }
    }
    }
}


@Composable
internal fun GrupoItem(
    grupo: Grupo,
    navigateToChatGrupo: (id: Long) -> Unit,
    modifier:Modifier = Modifier
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
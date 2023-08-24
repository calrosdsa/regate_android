package app.regate.usergroups

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import app.regate.common.composes.viewModel
import app.regate.grupos.GrupoItem
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

//typealias UserGroups = @Composable (
//    navigateToChat:(id:Long)->Unit,
////    navigateToSignUpScreen:() -> Unit,
//
//) -> Unit
//
//@Inject
//@Composable
//fun UserGroups (
//    viewModelFactory:()-> UserGroupsViewModel,
//    @Assisted navigateToChat: (id: Long) -> Unit,
////    @Assisted navigateToReserva: (id:Long) -> Unit,
////    viewModelFactory:()->ReservasViewModel
//){
//    UserGroups(
//        viewModel = viewModel(factory = viewModelFactory),
//        navigateToChat = navigateToChat
//    )
//}


//@Composable
//internal fun UserGroups (
//    viewModel: UserGroupsViewModel,
//    navigateToChat: (id: Long) -> Unit
//){
//    val state by viewModel.state.collectAsState()
//    UserGroups(viewState = state, navigateToChat = navigateToChat)
//}


@Composable
internal fun UserGroups(
    viewState:UserGroupsState,
    navigateToChat: (id: Long) -> Unit
){
    LazyColumn(modifier = Modifier.fillMaxSize()){
        items(items = viewState.grupos, key = {it.id}){item->
            GrupoItem(grupo = item, navigateToChatGrupo = navigateToChat)
        }
    }
}
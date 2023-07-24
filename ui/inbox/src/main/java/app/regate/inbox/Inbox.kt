package app.regate.inbox

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import app.regate.common.composes.viewModel
import app.regate.conversation.ConversationState
import app.regate.conversation.ConversationViewModel
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

typealias Inbox = @Composable (
    navigateUp:()->Unit
//    navigateToReserva:(id:Long)->Unit,
//    navigateToSignUpScreen:() -> Unit,
) -> Unit

@Inject
@Composable
fun Inbox (
    viewModelFactory:()-> InboxViewModel,
    @Assisted navigateUp: () -> Unit,
//    @Assisted navigateToReserva: (id:Long) -> Unit,
//    viewModelFactory:()->ReservasViewModel
){
    Inbox(
        viewModel = viewModel(factory = viewModelFactory),
        navigateUp = navigateUp
    )


}

@Composable
internal fun Inbox(
    viewModel: InboxViewModel,
    navigateUp: () -> Unit
){
    val state by viewModel.state.collectAsState()
    Inbox(
        viewState = state,
        navigateUp = navigateUp
    )
}

@Composable
internal fun Inbox(
    viewState: ConversationState,
    navigateUp: () -> Unit
){
    Scaffold(
        topBar = {
            Row() {
                IconButton(onClick = { navigateUp() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            }
        }
    ) {paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)){
        Text(text = viewState.loading.toString())
        }
    }
}
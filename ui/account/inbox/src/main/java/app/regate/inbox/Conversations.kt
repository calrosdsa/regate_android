package app.regate.inbox

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.regate.common.composes.ui.PosterCardImage
import app.regate.common.composes.ui.SimpleTopBar
import app.regate.common.composes.viewModel
import app.regate.common.resources.R
import app.regate.data.dto.empresa.conversation.Conversation
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

typealias Conversations= @Composable (
    navigateUp:()->Unit,
    navigateToConversation:(Long)->Unit
//    navigateToReserva:(id:Long)->Unit,
//    navigateToSignUpScreen:() -> Unit,
) -> Unit

@Inject
@Composable
fun Conversations (
    viewModelFactory:()-> ConversationsViewModel,
    @Assisted navigateUp: () -> Unit,
    @Assisted navigateToConversation: (Long) -> Unit,
//    @Assisted navigateToReserva: (id:Long) -> Unit,
//    viewModelFactory:()->ReservasViewModel
){
    Conversations(
        viewModel = viewModel(factory = viewModelFactory),
        navigateUp = navigateUp,
        navigateToConversation = navigateToConversation
    )
}

@Composable
internal fun Conversations(
    viewModel: ConversationsViewModel,
    navigateUp: () -> Unit,
    navigateToConversation: (Long) -> Unit,
){
    val state by viewModel.state.collectAsState()
    Conversations(
        viewState = state,
        navigateUp = navigateUp,
        navigateToConversation = navigateToConversation
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Conversations(
    viewState: ConversationsState,
    navigateUp: () -> Unit,
    navigateToConversation: (Long) -> Unit,
){
    Scaffold(
        topBar = {
            SimpleTopBar(navigateUp =  navigateUp, title = stringResource(id = R.string.inbox))
        }
    ) {paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues)
        ){
            items(
                items = viewState.conversations,
            ){item->
                ConversationItem(item = item, onClick = { navigateToConversation(item.id) })
                Divider()
            }
        }
    }
}

@Composable
fun ConversationItem(
    item:Conversation,
    onClick:()->Unit,
    modifier:Modifier = Modifier
){
    Row(modifier = modifier
        .fillMaxWidth()
        .clickable { onClick() }
        .padding(5.dp), verticalAlignment = Alignment.CenterVertically) {
        PosterCardImage(
            model = item.establecimiento_photo,modifier = Modifier.size(65.dp),
            shape = CircleShape
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = item.establecimiento_name,style = MaterialTheme.typography.labelLarge)
    }
}
package app.regate.inbox

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.regate.common.compose.LocalAppDateFormatter
import app.regate.common.compose.ui.PosterCardImage
import app.regate.common.compose.ui.SimpleTopBar
import app.regate.common.compose.viewModel
import app.regate.common.resources.R
import app.regate.data.dto.empresa.conversation.Conversation
import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

typealias Conversations= @Composable (
    navigateUp:()->Unit,
    navigateToConversation:(Long,Long)->Unit
//    navigateToReserva:(id:Long)->Unit,
//    navigateToSignUpScreen:() -> Unit,
) -> Unit

@Inject
@Composable
fun Conversations (
    viewModelFactory:()-> ConversationsViewModel,
    @Assisted navigateUp: () -> Unit,
    @Assisted navigateToConversation: (Long,Long) -> Unit,
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
    navigateToConversation: (Long,Long) -> Unit,
){
    val state by viewModel.state.collectAsState()
    val formatter = LocalAppDateFormatter.current
    Conversations(
        viewState = state,
        navigateUp = navigateUp,
        navigateToConversation = navigateToConversation,
        formatShortRelativeTime = formatter::formatShortRelativeTime
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Conversations(
    viewState: ConversationsState,
    navigateUp: () -> Unit,
    formatShortRelativeTime:(Instant)->String,
    navigateToConversation: (Long,Long) -> Unit,
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
                ConversationItem(item = item, onClick = { navigateToConversation(item.id,item.establecimiento_id) },
                formatShortRelativeTime = formatShortRelativeTime)
                Divider()
            }
        }
    }
}

@Composable
fun ConversationItem(
    item:Conversation,
    formatShortRelativeTime: (Instant) -> String,
    onClick:()->Unit,
    modifier:Modifier = Modifier
){
    Row(modifier = modifier
        .fillMaxWidth()
        .clickable { onClick() }
        .padding(5.dp), verticalAlignment = Alignment.CenterVertically) {
        PosterCardImage(
            model = item.establecimiento_photo,modifier = Modifier.size(50.dp),
            shape = CircleShape
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column() {
            Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
                Text(text = item.establecimiento_name + "",style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.fillMaxWidth(0.7f), overflow = TextOverflow.Ellipsis)
                Text(text = formatShortRelativeTime(item.last_message_created),
                    style = MaterialTheme.typography.labelSmall)
            }
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = item.last_message,style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Normal
            ), overflow = TextOverflow.Ellipsis, maxLines = 1)
        }

    }
}
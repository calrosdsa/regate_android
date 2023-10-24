package app.regate.usergroups

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import app.regate.common.composes.ui.PosterCardImage
import app.regate.common.composes.util.itemsCustom
import app.regate.compoundmodels.GrupoWithMessage
import app.regate.models.chat.Chat
import kotlinx.datetime.Instant

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
    lazyPagingItems: LazyPagingItems<Chat>,
    formatShortRelativeTime:(Instant)->String,
    navigateToChat: (id: Long) -> Unit
){
    LazyColumn(modifier = Modifier.fillMaxSize()){
        itemsCustom(items = lazyPagingItems, key = {it.id}){item->
            if(item!= null){
                GrupoItemWithMessage(grupo = item, navigateToChatGrupo = navigateToChat,
                    formatShortRelativeTime = formatShortRelativeTime)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GrupoItemWithMessage(
//    grupo: GrupoWithMessage,
    grupo: Chat,
    navigateToChatGrupo: (id: Long) -> Unit,
    formatShortRelativeTime:(Instant)->String,
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
                .size(60.dp), shape = CircleShape
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column() {
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
                Text(text = grupo.name ,style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.fillMaxWidth(0.65f), overflow = TextOverflow.Ellipsis)
                if(grupo.last_message_created != null){
                    Text(text = formatShortRelativeTime(grupo.last_message_created!!),
                        style = MaterialTheme.typography.labelSmall, maxLines = 1,
                        overflow = TextOverflow.Ellipsis)
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween){

            if(grupo.last_message?.isNotBlank() == true){
                grupo.last_message?.let {
                    Text(text = it,style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Normal
                    ), overflow = TextOverflow.Ellipsis, maxLines = 1,
                        modifier = Modifier.fillMaxWidth(0.88f))
                }
            }
                if(grupo.messages_count > 0){
                    Badge(
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        containerColor = MaterialTheme.colorScheme.primary
                    ){
                  Text(text = (grupo.messages_count).toString(),
                  style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }

    }
}

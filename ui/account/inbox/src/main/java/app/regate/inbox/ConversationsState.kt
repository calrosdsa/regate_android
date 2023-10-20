package app.regate.inbox

import androidx.compose.runtime.Immutable
import app.regate.api.UiMessage
import app.regate.data.dto.empresa.conversation.Conversation
import app.regate.models.Establecimiento
import app.regate.models.MessageInbox
import app.regate.models.account.User

@Immutable
data class ConversationsState (
    val loading:Boolean = false,
    val message:UiMessage? = null,
    val conversations:List<Conversation> = emptyList(),
    val user: User? = null,
){
    companion object{
        val Empty = ConversationsState()
    }
}
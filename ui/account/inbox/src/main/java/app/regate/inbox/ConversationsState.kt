package app.regate.inbox

import app.regate.api.UiMessage
import app.regate.data.dto.empresa.conversation.Conversation
import app.regate.models.Establecimiento
import app.regate.models.MessageInbox
import app.regate.models.User

data class ConversationsState (
    val loading:Boolean = false,
    val message:UiMessage? = null,
    val conversations:List<Conversation> = emptyList(),
    val user:User? = null,
    val messageInbox: MessageInbox? = null,
    val establecimiento:Establecimiento? = null
){
    companion object{
        val Empty = ConversationsState()
    }
}
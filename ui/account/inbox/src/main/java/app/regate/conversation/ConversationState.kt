package app.regate.conversation

import app.regate.api.UiMessage
import app.regate.inbox.ConversationsState
import app.regate.models.Establecimiento
import app.regate.models.MessageInbox
import app.regate.models.account.User

data class ConversationState (
    val loading:Boolean = false,
    val message:UiMessage? = null,
    val user:User? = null,
    val messageInbox: MessageInbox? = null,
    val establecimiento: Establecimiento? = null
){
    companion object{
        val Empty = ConversationState()
    }
}
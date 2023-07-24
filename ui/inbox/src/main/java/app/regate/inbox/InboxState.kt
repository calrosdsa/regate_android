package app.regate.inbox

import app.regate.api.UiMessage
import app.regate.conversation.ConversationState

data class InboxState (
    val loading:Boolean = false,
    val message:UiMessage? = null,
){
    companion object{
        val Empty = ConversationState()
    }
}
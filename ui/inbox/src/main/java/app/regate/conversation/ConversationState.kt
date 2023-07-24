package app.regate.conversation

import app.regate.api.UiMessage

data class ConversationState (
    val loading:Boolean = false,
    val message:UiMessage? = null,
){
    companion object{
        val Empty = ConversationState()
    }
}
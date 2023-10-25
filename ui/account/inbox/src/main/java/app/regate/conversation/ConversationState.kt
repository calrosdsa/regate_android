package app.regate.conversation

import app.regate.api.UiMessage
import app.regate.compoundmodels.MessageProfile
import app.regate.data.auth.AppAuthState
import app.regate.models.Emoji
import app.regate.models.Establecimiento
import app.regate.models.account.User

data class ConversationState (
    val establecimiento: Establecimiento? = null,
    val loading:Boolean = false,
    val message:UiMessage? = null,
    val messages:List<MessageProfile> = emptyList(),
    val scrollToBottom:Boolean? = null,
    val user: User? = null,
    val authState: AppAuthState? = null,
    val emojiData:List<List<Emoji>> = emptyList()
){
    companion object{
        val Empty = ConversationState()
    }
}
package app.regate.chat.sala

import androidx.compose.runtime.Immutable
import app.regate.api.UiMessage
import app.regate.data.auth.AppAuthState
import app.regate.compoundmodels.MessageProfile
import app.regate.compoundmodels.UserProfileGrupoAndSala
import app.regate.models.Emoji
import app.regate.models.account.User
import app.regate.models.chat.Chat

@Immutable
data class ChatSalaState(
//    val establecimiento: Establecimiento? = null,
    val loading:Boolean = false,
    val message:UiMessage? = null,
    val messages:List<MessageProfile> = emptyList(),
    val scrollToBottom:Boolean? = null,
    val user: User? = null,
    val authState: AppAuthState? = null,
    val emojiData:List<List<Emoji>> = emptyList(),
    val chat:Chat? = null,
    val usersSala:List<UserProfileGrupoAndSala> = emptyList()
){
    companion object{
        val Empty = ChatSalaState()
    }
}
package app.regate.chat.grupo

import androidx.compose.runtime.Immutable
import app.regate.api.UiMessage
import app.regate.data.auth.AppAuthState
import app.regate.compoundmodels.MessageProfile
import app.regate.compoundmodels.UserProfileGrupoAndSalaDto
import app.regate.models.Emoji
import app.regate.models.account.User
import app.regate.models.chat.Chat

@Immutable
data class ChatGrupoState(
    val loading:Boolean = false,
    val message:UiMessage? = null,
    val messages:List<MessageProfile> = emptyList(),
    val scrollToBottom:Boolean? = null,
    val chat:Chat? = null,
    val user: User? = null,
    val authState:AppAuthState? = null,
    val usersGrupo: List<UserProfileGrupoAndSalaDto> = emptyList(),
    val emojiData:List<List<Emoji>> = emptyList()
){
    companion object{
        val Empty = ChatGrupoState()
    }
}

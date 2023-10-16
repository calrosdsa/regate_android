package app.regate.chat.grupo

import app.regate.api.UiMessage
import app.regate.data.auth.AppAuthState
import app.regate.compoundmodels.MessageProfile
import app.regate.compoundmodels.UserProfileGrupo
import app.regate.data.app.EmojisState
import app.regate.models.Emoji
import app.regate.models.Grupo
import app.regate.models.Message
import app.regate.models.User

data class ChatGrupoState(
    val loading:Boolean = false,
    val message:UiMessage? = null,
    val messageChat:Message? = null,
    val messages:List<MessageProfile> = emptyList(),
    val scrollToBottom:Boolean? = null,
    val grupo:Grupo? = null,
    val user:User? = null,
    val authState:AppAuthState? = null,
    val usersGrupo: List<UserProfileGrupo> = emptyList(),
    val emojiData:List<List<Emoji>> = emptyList()
){
    companion object{
        val Empty = ChatGrupoState()
    }
}

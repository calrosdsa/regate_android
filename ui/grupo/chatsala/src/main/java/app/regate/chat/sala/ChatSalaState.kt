package app.regate.chat.sala

import app.regate.api.UiMessage
import app.regate.data.auth.AppAuthState
import app.regate.compoundmodels.MessageProfile
import app.regate.compoundmodels.UserProfileGrupo
import app.regate.compoundmodels.UserProfileSala
import app.regate.models.Grupo
import app.regate.models.Message
import app.regate.models.MessageSala
import app.regate.models.User

data class ChatSalaState(
    val loading:Boolean = false,
    val message:UiMessage? = null,
    val messageChat:MessageSala? = null,
    val messages:List<Pair<Int,MessageProfile>> = emptyList(),
    val grupo:Grupo? = null,
    val user:User? = null,
    val authState:AppAuthState? = null,
    val usersSala: List<UserProfileSala> = emptyList()
){
    companion object{
        val Empty = ChatSalaState()
    }
}

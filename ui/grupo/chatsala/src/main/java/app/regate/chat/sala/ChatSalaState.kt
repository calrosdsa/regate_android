package app.regate.chat.sala

import app.regate.api.UiMessage
import app.regate.data.auth.AppAuthState
import app.regate.compoundmodels.MessageProfile
import app.regate.compoundmodels.UserProfileRoom
import app.regate.models.Grupo
import app.regate.models.MessageSala
import app.regate.models.account.User

data class ChatSalaState(
    val loading:Boolean = false,
    val message:UiMessage? = null,
    val messageChat:MessageSala? = null,
    val messages:List<Pair<Int,MessageProfile>> = emptyList(),
    val grupo:Grupo? = null,
    val user: User? = null,
    val authState:AppAuthState? = null,
    val usersSala: List<UserProfileRoom> = emptyList()
){
    companion object{
        val Empty = ChatSalaState()
    }
}

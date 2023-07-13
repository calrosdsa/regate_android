package app.regate.chat.grupo

import app.regate.api.UiMessage
import app.regate.data.auth.AppAuthState
import app.regate.compoundmodels.MessageProfile
import app.regate.compoundmodels.UserProfileGrupo
import app.regate.models.Grupo
import app.regate.models.Message
import app.regate.models.User

data class ChatSalaState(
    val loading:Boolean = false,
    val message:UiMessage? = null,
    val messageChat:Message? = null,
    val messages:List<Pair<Int,MessageProfile>> = emptyList(),
    val grupo:Grupo? = null,
    val user:User? = null,
    val authState:AppAuthState? = null,
    val usersGrupo: List<UserProfileGrupo> = emptyList()
){
    companion object{
        val Empty = ChatSalaState()
    }
}


data class MessageData(
    val content:String,
    val reply_to:Long? = null
)
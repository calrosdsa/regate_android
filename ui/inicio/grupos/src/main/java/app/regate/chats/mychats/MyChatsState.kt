package app.regate.chats.mychats

import androidx.compose.runtime.Immutable
import app.regate.api.UiMessage
import app.regate.models.grupo.Grupo
import app.regate.models.account.User
import app.regate.models.chat.Chat

@Immutable
data class MyChatsState(
    val loading:Boolean = false,
    val message:UiMessage? = null,
    val grupos:List<Grupo> = emptyList(),
    val user: User?= null,
    val selectedChats:List<Chat> = emptyList()

){
    companion object{
        val Empty = MyChatsState()
    }
}

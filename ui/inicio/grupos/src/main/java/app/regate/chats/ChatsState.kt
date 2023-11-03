package app.regate.chats

import androidx.compose.runtime.Immutable
import app.regate.api.UiMessage
import app.regate.compoundmodels.GrupoWithMessage
import app.regate.data.auth.AppAuthState
import app.regate.models.chat.Chat

@Immutable
data class ChatsState(
    val loading:Boolean = false,
    val message:UiMessage? = null,
    val grupos:List<GrupoWithMessage> = emptyList(),
    val authState:AppAuthState = AppAuthState.LOGGED_OUT,
    val selectedChat:Chat? = null,
){
    companion object{
        val Empty = ChatsState()
    }
}

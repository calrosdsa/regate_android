package app.regate.main

import androidx.compose.runtime.Immutable

@Immutable
data class InicioState(
//    val loading:Boolean = false,
//    val message:UiMessage? = null,
    val notificationCount:Int = 0,
    val messagesCount:Int = 0
){
    companion object{
        val Empty = InicioState()
    }
}

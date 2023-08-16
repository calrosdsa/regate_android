package app.regate.system.notification

import app.regate.api.UiMessage
import app.regate.models.Notification


data class NotificationState(
    val loading:Boolean = false,
    val message:UiMessage? = null,
    val notifications:List<Notification> = emptyList(),
){
    companion object{
         val Empty = NotificationState()
    }
}

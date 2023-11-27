package app.regate.data.daos

import app.regate.models.Notification
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

interface NotificationDao:EntityDao<Notification> {
    fun getNotificaciones():Flow<List<Notification>>
    fun observeUnReadNotificationsCount():Flow<Int>
    suspend fun updateUnreadNotifications()
    suspend fun deleteLastNotifications(date:Instant)
    suspend fun deleteNotification(id:Long)
    suspend fun deleteAll()
}
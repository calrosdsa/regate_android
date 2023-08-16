package app.regate.data.daos

import app.regate.models.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationDao:EntityDao<Notification> {
    fun getNotificaciones():Flow<List<Notification>>
    fun observeUnReadNotificationsCount():Flow<Int>
    suspend fun updateUnreadNotifications()
}
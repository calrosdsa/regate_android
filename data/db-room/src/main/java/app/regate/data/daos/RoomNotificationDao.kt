package app.regate.data.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import app.regate.models.Notification
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RoomNotificationDao:NotificationDao,RoomEntityDao<Notification> {
    @Transaction
    @Query("select * from notification order by created_at desc limit 500")
    abstract override fun getNotificaciones(): Flow<List<Notification>>

    @Transaction
    @Query("select count(*) from notification where read = 0")
    abstract override fun observeUnReadNotificationsCount(): Flow<Int>

    @Transaction
    @Query("update notification set read = 1")
    abstract override suspend fun updateUnreadNotifications()
    @Query("delete from notification")
    abstract override suspend fun deleteAll()
}
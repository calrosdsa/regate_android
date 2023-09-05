package app.regate.data.system

import app.regate.data.daos.NotificationDao
import app.regate.data.daos.UserDao
import app.regate.data.dto.system.NotificationCount
import app.regate.data.dto.system.ReportData
import app.regate.inject.ApplicationScope
import app.regate.models.Notification
import app.regate.models.TypeEntity
import me.tatarka.inject.annotations.Inject
@ApplicationScope
@Inject
class SystemRepository(
    private val systemDataSourceImpl: SystemDataSourceImpl,
    private val userDao: UserDao,
    private val notificationDao: NotificationDao,
){
    suspend fun getNotifications(){
        val res = systemDataSourceImpl.getNotifications().map {
            Notification(
                id = it.id,
                title = it.title,
                content = it.content,
                created_at = it.created_at,
                typeEntity = it.type_entity?.let { it1 -> TypeEntity.fromInt(it1) },
                entityId = it.entity_id,
                read = true
            )
        }
        notificationDao.upsertAll(res)
    }
    suspend fun getNotificationsCount():NotificationCount{
        return systemDataSourceImpl.getNotificationCount()
    }

    suspend fun updateUnreadNotifications(){
        notificationDao.updateUnreadNotifications()
    }
    suspend fun sendReport(d:ReportData) {
        val profileId = try {
            userDao.getUser(0).profile_id
        }catch (e:Exception){
            0
        }
        systemDataSourceImpl.sendReport(d.copy(
            profile_id = profileId
        ))
    }

}
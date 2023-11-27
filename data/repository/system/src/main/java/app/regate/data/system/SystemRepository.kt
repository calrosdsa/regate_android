package app.regate.data.system

import app.regate.data.daos.NotificationDao
import app.regate.data.daos.UserDao
import app.regate.data.dto.system.NotificationCount
import app.regate.data.dto.system.NotificationDto
import app.regate.data.dto.system.ReportData
import app.regate.data.mappers.toNotification
import app.regate.inject.ApplicationScope
import app.regate.models.Notification
import app.regate.models.TypeEntity
import app.regate.util.AppCoroutineDispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Inject
@ApplicationScope
@Inject
class SystemRepository(
    private val systemDataSourceImpl: SystemDataSourceImpl,
    private val userDao: UserDao,
    private val notificationDao: NotificationDao,
    private val dispatchers: AppCoroutineDispatchers
){
    suspend fun getNotifications(){
        val res = systemDataSourceImpl.getNotifications().map {
            it.toNotification()
        }
        notificationDao.insertAllonConflictIgnore(res)
    }
    suspend fun insertNotification(d:NotificationDto){
        withContext(dispatchers.computation){
            try{
                d.let {
                   notificationDao.upsert(it.toNotification())
                }
            }catch (e:Exception){
                throw  e
            }
        }
    }
    suspend fun getNotificationsCount():NotificationCount{
        return systemDataSourceImpl.getNotificationCount()
    }
    suspend fun deleteLastNotifications(date:Instant){
        withContext(dispatchers.io){
            try{
                notificationDao.deleteLastNotifications(date)
            }catch (e:Exception){
                throw e
            }
        }
    }
    suspend fun deleteNotification(id:Long){
        withContext(dispatchers.io){
            notificationDao.deleteNotification(id)
        }
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
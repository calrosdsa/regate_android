package app.regate.domain.observers.system

import app.regate.data.daos.NotificationDao
import app.regate.domain.SubjectInteractor
import app.regate.models.Notification
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveNotifications (
     private val notificationDao: NotificationDao
 ):SubjectInteractor<Unit,List<Notification>>(){
          override fun createObservable(params: Unit): Flow<List<Notification>> {
              return notificationDao.getNotificaciones()
          }
}
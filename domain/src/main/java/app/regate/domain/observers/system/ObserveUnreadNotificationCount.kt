package app.regate.domain.observers.system

import app.regate.data.daos.NotificationDao
import app.regate.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveUnreadNotificationCount(
    private val notificationDao: NotificationDao
) :SubjectInteractor<Unit,Int>(){
    override fun createObservable(params: Unit): Flow<Int> {
        return notificationDao.observeUnReadNotificationsCount()
    }
}
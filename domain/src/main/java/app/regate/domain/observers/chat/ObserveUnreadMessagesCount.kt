package app.regate.domain.observers.chat

import app.regate.data.daos.MessageProfileDao
import app.regate.data.daos.NotificationDao
import app.regate.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveUnreadMessagesCount(
    private val messageProfileDao: MessageProfileDao
) :SubjectInteractor<Unit,Int>(){
    override fun createObservable(params: Unit): Flow<Int> {
        return messageProfileDao.observeUnreadMessagesCount()
    }
}
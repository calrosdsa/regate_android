package app.regate.domain.observers.account

import app.regate.data.daos.UserDao
import app.regate.domain.SubjectInteractor
import app.regate.models.account.User
import app.regate.models.account.UserBalance
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveUserBalance(
    private val userDao: UserDao
):SubjectInteractor<Unit, UserBalance>() {
    override fun createObservable(params: Unit): Flow<UserBalance> {
        return userDao.observeUserBalance()
    }
}
package app.regate.domain.observers.account

import app.regate.data.daos.UserDao
import app.regate.domain.SubjectInteractor
import app.regate.models.account.User
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveUser(
    private val userDao: UserDao
):SubjectInteractor<Unit, User>() {
    override fun createObservable(params: Unit): Flow<User> {
        return userDao.observeUser()
    }
}
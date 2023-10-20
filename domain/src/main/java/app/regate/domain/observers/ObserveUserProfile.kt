package app.regate.domain.observers

import app.regate.compoundmodels.UserProfile
import app.regate.data.daos.UserDao
import app.regate.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveUserProfile(
    private val userDao: UserDao
):SubjectInteractor<Unit,UserProfile>() {
    override fun createObservable(params: Unit): Flow<UserProfile> {
        return userDao.observeUserAndProfile()
    }
}
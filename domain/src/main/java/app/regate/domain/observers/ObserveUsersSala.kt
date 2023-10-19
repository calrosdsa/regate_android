package app.regate.domain.observers

import app.regate.compoundmodels.UserProfileRoom
import app.regate.data.daos.UserRoomDao
import app.regate.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveUsersSala(
    private val userRoomDao: UserRoomDao
) :SubjectInteractor<ObserveUsersSala.Params,List<UserProfileRoom>>(){

    override fun createObservable(params: Params): Flow<List<UserProfileRoom>> {
        return userRoomDao.observeUsersRoom(params.id)
    }

    data class Params(
        val id:Long
    )
}
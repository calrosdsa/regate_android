package app.regate.domain.observers.grupo

import app.regate.data.daos.MyGroupsDao
import app.regate.domain.SubjectInteractor
import app.regate.models.grupo.MyGroups
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveMyGroups(
    private val myGroupsDao: MyGroupsDao
):SubjectInteractor<Unit,List<MyGroups>>() {
    override fun createObservable(params: Unit): Flow<List<MyGroups>> {
        return myGroupsDao.observeMyGroups()
    }
}
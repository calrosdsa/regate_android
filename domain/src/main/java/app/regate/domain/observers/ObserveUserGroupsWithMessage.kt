package app.regate.domain.observers

import app.regate.compoundmodels.GrupoWithMessage
import app.regate.data.daos.MyGroupsDao
import app.regate.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveUserGroupsWithMessage(
    private val myGroupsDao: MyGroupsDao
):SubjectInteractor<Unit,List<GrupoWithMessage>>() {
    override fun createObservable(params: Unit): Flow<List<GrupoWithMessage>> {
        return myGroupsDao.observeUserGroupsWithMessage()
    }
}
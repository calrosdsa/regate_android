package app.regate.domain.observers.grupo

import app.regate.compoundmodels.GrupoWithMessage
import app.regate.data.daos.MyGroupsDao
import app.regate.domain.SubjectInteractor
import app.regate.models.Grupo
import app.regate.models.MyGroups
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveMyGroupById(
    private val myGroupsDao: MyGroupsDao
):SubjectInteractor<ObserveMyGroupById.Params,MyGroups?>() {
    override fun createObservable(params: Params): Flow<MyGroups?> {
        return myGroupsDao.observeMyGroupById(params.grupoId)
    }

    data class Params(
        val grupoId:Long=0
    )
}
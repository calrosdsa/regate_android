package app.regate.domain.observers

import app.regate.data.daos.GrupoDao
import app.regate.data.daos.MyGroupsDao
import app.regate.data.daos.UserGrupoDao
import app.regate.domain.SubjectInteractor
import app.regate.models.Grupo
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveUserGroups(
    private val myGroupsDao: MyGroupsDao
):SubjectInteractor<Unit,List<Grupo>>() {
    override fun createObservable(params: Unit): Flow<List<Grupo>> {
        return myGroupsDao.observeUserGroups()
    }
}
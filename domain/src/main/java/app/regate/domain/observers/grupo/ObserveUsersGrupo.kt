package app.regate.domain.observers.grupo

import app.regate.compoundmodels.UserProfileGrupoAndSala
import app.regate.data.daos.UserGrupoDao
import app.regate.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveUsersGrupo (
    private val usersGrupoDao: UserGrupoDao
    ):SubjectInteractor<ObserveUsersGrupo.Params,List<UserProfileGrupoAndSala>>(){

    override fun createObservable(params: Params): Flow<List<UserProfileGrupoAndSala>> {
        return usersGrupoDao.observeUsersGrupo(params.id)
    }

    data class Params(
        val id:Long
    )
}
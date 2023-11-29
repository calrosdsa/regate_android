package app.regate.domain.observers.grupo

import app.regate.compoundmodels.UserProfileGrupoAndSalaDto
import app.regate.data.daos.UserGrupoDao
import app.regate.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveUsersGrupo (
    private val usersGrupoDao: UserGrupoDao
    ):SubjectInteractor<ObserveUsersGrupo.Params,List<UserProfileGrupoAndSalaDto>>(){

    override fun createObservable(params: Params): Flow<List<UserProfileGrupoAndSalaDto>> {
        return usersGrupoDao.observeUsersGrupo(params.id)
    }

    data class Params(
        val id:Long
    )
}
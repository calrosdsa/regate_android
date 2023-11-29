package app.regate.domain.observers

import app.regate.compoundmodels.UserProfileGrupoAndSalaDto
import app.regate.data.daos.UserGrupoDao
import app.regate.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveUsersSala(
    private val userGrupoDao: UserGrupoDao
) :SubjectInteractor<ObserveUsersSala.Params,List<UserProfileGrupoAndSalaDto>>(){

    override fun createObservable(params: Params): Flow<List<UserProfileGrupoAndSalaDto>> {
        return userGrupoDao.observeUsersRoom(params.id)
    }

    data class Params(
        val id:Long
    )
}
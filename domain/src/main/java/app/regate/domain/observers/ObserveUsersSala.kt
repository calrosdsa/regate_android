package app.regate.domain.observers

import app.regate.compoundmodels.UserProfileGrupoAndSala
import app.regate.data.daos.UserGrupoDao
import app.regate.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveUsersSala(
    private val userGrupoDao: UserGrupoDao
) :SubjectInteractor<ObserveUsersSala.Params,List<UserProfileGrupoAndSala>>(){

    override fun createObservable(params: Params): Flow<List<UserProfileGrupoAndSala>> {
        return userGrupoDao.observeUsersRoom(params.id)
    }

    data class Params(
        val id:Long
    )
}
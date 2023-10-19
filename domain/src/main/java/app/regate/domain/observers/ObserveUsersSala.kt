package app.regate.domain.observers

import app.regate.compoundmodels.UserProfileSala
import app.regate.data.daos.UserSalaDao
import app.regate.data.instalacion.CupoRepository
import app.regate.domain.SubjectInteractor
import app.regate.models.Cupo
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveUsersSala(
    private val userSalaDao: UserSalaDao
) :SubjectInteractor<ObserveUsersSala.Params,List<UserProfileSala>>(){

    override fun createObservable(params: Params): Flow<List<UserProfileSala>> {
        return userSalaDao.observeUsersSala(params.id)
    }

    data class Params(
        val id:Long
    )
}
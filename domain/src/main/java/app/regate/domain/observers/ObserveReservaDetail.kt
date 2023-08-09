package app.regate.domain.observers

import app.regate.compoundmodels.ReservaDetail
import app.regate.data.daos.ReservaDao
import app.regate.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveReservaDetail(
    private val reservaDao: ReservaDao
):SubjectInteractor<ObserveReservaDetail.Params,ReservaDetail>() {

    override fun createObservable(params: Params): Flow<ReservaDetail> {
        return reservaDao.observeReservaDetail(params.id)
    }

    data class Params(
        val id:Long
    )
}
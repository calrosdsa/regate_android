package app.regate.domain.observers

import app.regate.data.daos.ReservaDao
import app.regate.domain.SubjectInteractor
import app.regate.models.Reserva
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveReservas(
    private val reservaDao: ReservaDao
):SubjectInteractor<Unit,List<Reserva>>() {
    override fun createObservable(params: Unit): Flow<List<Reserva>> {
        return reservaDao.observeReservas()
    }
}
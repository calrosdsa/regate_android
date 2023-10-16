package app.regate.domain.observers.establecimiento

import app.regate.data.daos.EstablecimientoDao
import app.regate.domain.SubjectInteractor
import app.regate.models.AttentionSchedule
import app.regate.models.Setting
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveAttentionScheduleWeek(
    private val establecimientoDao: EstablecimientoDao
):SubjectInteractor<ObserveAttentionScheduleWeek.Params,List<AttentionSchedule>>() {

    override fun createObservable(params: Params): Flow<List<AttentionSchedule>> {
        return establecimientoDao.observeAttentionScheduleWeek(params.id)
    }

    data class Params(
        val id:Long,
    )
}
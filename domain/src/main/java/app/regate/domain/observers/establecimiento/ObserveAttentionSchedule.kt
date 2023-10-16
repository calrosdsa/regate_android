package app.regate.domain.observers.establecimiento

import app.regate.data.daos.EstablecimientoDao
import app.regate.domain.SubjectInteractor
import app.regate.models.AttentionSchedule
import app.regate.models.Setting
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveAttentionSchedule(
    private val establecimientoDao: EstablecimientoDao
):SubjectInteractor<ObserveAttentionSchedule.Params,AttentionSchedule>() {

    override fun createObservable(params: Params): Flow<AttentionSchedule> {
        return establecimientoDao.observeAttentionScheduleTime(params.id,params.dayWeek)
    }

    data class Params(
        val id:Long,
        val dayWeek:Int
    )
}
package app.regate.domain.observers.establecimiento

import app.regate.data.daos.EstablecimientoDao
import app.regate.domain.SubjectInteractor
import app.regate.models.Setting
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveSettingEstablecimiento(
    private val establecimientoDao: EstablecimientoDao
):SubjectInteractor<ObserveSettingEstablecimiento.Params,Setting>() {

    override fun createObservable(params: Params): Flow<Setting> {
        return establecimientoDao.observeEstablecimientoSetting(params.id)
    }

    data class Params(
        val id:Long
    )
}
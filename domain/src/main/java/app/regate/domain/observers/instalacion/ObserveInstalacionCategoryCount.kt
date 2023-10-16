package app.regate.domain.observers.instalacion

import app.regate.compoundmodels.InstalacionCategoryCount
import app.regate.data.daos.InstalacionDao
import app.regate.domain.SubjectInteractor
import app.regate.models.LabelType
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveInstalacionCategoryCount(
    private val instalacionDao: InstalacionDao
):SubjectInteractor<ObserveInstalacionCategoryCount.Params,List<InstalacionCategoryCount>>() {

    override fun createObservable(params: Params): Flow<List<InstalacionCategoryCount>> {
        return instalacionDao.observeGroupInstalacionByCategory(params.establecimientoId,params.type)
    }

    data class Params(
        val establecimientoId:Long,
        val type:LabelType
    )
}
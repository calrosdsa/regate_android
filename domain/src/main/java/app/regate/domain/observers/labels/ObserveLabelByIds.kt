package app.regate.domain.observers.labels

import app.regate.data.daos.LabelDao
import app.regate.domain.SubjectInteractor
import app.regate.models.LabelType
import app.regate.models.Labels
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveLabelByIds (
    private val labelDao: LabelDao
    ):SubjectInteractor<ObserveLabelByIds.Params,List<Labels>>() {

    override fun createObservable(params: Params): Flow<List<Labels>> {
        return labelDao.observeLabelByIdsAndType(params.type,params.ids)
    }

    data class Params (
        val type:LabelType,
        val ids:List<Long>
        )
}
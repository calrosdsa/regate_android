package app.regate.domain.observers

import app.regate.data.daos.LabelDao
import app.regate.domain.SubjectInteractor
import app.regate.models.LabelType
import app.regate.models.Labels
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveLabelType(
    private val labelDao: LabelDao
):SubjectInteractor<ObserveLabelType.Params,List<Labels>>() {
    override fun createObservable(params: Params): Flow<List<Labels>> {
        return labelDao.observeLabelByType(params.type)
    }

    data class Params(
        val type:LabelType
    )
}
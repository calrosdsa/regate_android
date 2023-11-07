package app.regate.domain.observers.user

import app.regate.data.daos.ProfileDao
import app.regate.domain.SubjectInteractor
import app.regate.models.LabelType
import app.regate.models.Labels
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveProfileCategory(
    private val profileDao: ProfileDao
):SubjectInteractor<ObserveProfileCategory.Params,List<Labels>>() {

    override fun createObservable(params: Params): Flow<List<Labels>> {
        return profileDao.observeProfileCategory(params.id,LabelType.CATEGORIES)
    }

    data class Params(
        val id:Long,
    )

}
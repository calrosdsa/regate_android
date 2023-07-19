package app.regate.domain.observers

import app.regate.data.daos.ProfileDao
import app.regate.domain.SubjectInteractor
import app.regate.models.Profile
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveProfile(
    private val profileDao: ProfileDao
):SubjectInteractor<ObserveProfile.Params,Profile>() {
    override fun createObservable(params: Params): Flow<Profile> {
        return profileDao.observeProfile(params.id)
    }

    data class Params(
        val id:Long
    )
}
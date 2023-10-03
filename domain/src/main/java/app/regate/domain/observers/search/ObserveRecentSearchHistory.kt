package app.regate.domain.observers.search

import app.regate.data.daos.SearchHistoryDao
import app.regate.data.instalacion.CupoRepository
import app.regate.domain.SubjectInteractor
import app.regate.models.Cupo
import app.regate.models.SearchHistory
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveRecentSearchHistory(
    private val searchHistory: SearchHistoryDao
) :SubjectInteractor<Unit,List<SearchHistory>>(){

    override fun createObservable(params: Unit): Flow<List<SearchHistory>> {
        return searchHistory.observeRecentHistory(0,10)
    }

}
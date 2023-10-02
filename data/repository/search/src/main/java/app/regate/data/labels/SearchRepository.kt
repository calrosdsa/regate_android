package app.regate.data.labels

import androidx.paging.PagingSource
import app.regate.data.daos.LabelDao
import app.regate.data.daos.SearchHistoryDao
import app.regate.data.mappers.AmenityToLabel
import app.regate.data.mappers.CategoryToLabel
import app.regate.data.mappers.RuleToLabel
import app.regate.data.mappers.SportsToLabel
import app.regate.inject.ApplicationScope
import app.regate.models.SearchHistory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import me.tatarka.inject.annotations.Inject
@ApplicationScope
@Inject
class SearchRepository(
    private val searchHistoryDao: SearchHistoryDao
){
    suspend fun addSearchQueryToHistory(query:String){
        searchHistoryDao.upsert(SearchHistory(
            query = query
        ))
    }

    fun observePaginationSearchHistory(page:Int,size:Int = 20):Flow<List<SearchHistory>>{
        return searchHistoryDao.observeRecentHistory(page,size)
    }

    suspend fun getHistorySearch(page:Int, size:Int = 20):List<SearchHistory>{
        return searchHistoryDao.getHistorySearch(page,size)
    }
}
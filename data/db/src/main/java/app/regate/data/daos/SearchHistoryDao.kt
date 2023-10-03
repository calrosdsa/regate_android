package app.regate.data.daos

import androidx.paging.PagingSource
import app.regate.models.SearchHistory
import kotlinx.coroutines.flow.Flow

interface SearchHistoryDao:EntityDao<SearchHistory> {
    fun observeRecentHistory(page:Int,size:Int):Flow<List<SearchHistory>>

    fun observePaginationRecentHistory(page:Int,size:Int):PagingSource<Int,SearchHistory>

    suspend fun getHistorySearch(page: Int,size:Int):List<SearchHistory>
    fun observeLastSearchHistory():Flow<SearchHistory>
}
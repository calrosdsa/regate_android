package app.regate.data.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import app.regate.models.SearchHistory
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RoomSearchHistoryDao : SearchHistoryDao,RoomEntityDao<SearchHistory>{

    @Transaction
    @Query("select * from search_history order by created_at desc limit :size offset (:size * :page)")
    abstract override fun observeRecentHistory(page: Int, size: Int): Flow<List<SearchHistory>>
    @Transaction
    @Query("select * from search_history order by created_at desc  limit :size offset (:size * :page) ")
    abstract override fun observePaginationRecentHistory(page: Int, size: Int): PagingSource<Int,SearchHistory>

    @Transaction
    @Query("select * from search_history order by created_at desc  limit :size offset (:size * :page) ")
    abstract override suspend fun getHistorySearch(page: Int,size:Int): List<SearchHistory>
}
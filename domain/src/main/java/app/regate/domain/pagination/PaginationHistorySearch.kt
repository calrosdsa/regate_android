package app.regate.domain.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.regate.data.dto.empresa.salas.PaginationSalaResponse
import app.regate.data.dto.empresa.salas.SalaDto
import app.regate.models.SearchHistory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.toList

class PaginationHistorySearch(
//    private val loadingState:ObservableLoadingCounter,
    private val getHistory:suspend (page:Int)->List<SearchHistory>,
) : PagingSource<Int, SearchHistory>() {
    override fun getRefreshKey(state: PagingState<Int, SearchHistory>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchHistory> {
        return try{
            val page = params.key?:1
            val res = getHistory(page -1)
            LoadResult.Page(
                data = res,
                prevKey = null,
                nextKey = if(res.size < 20 ) null else page + 1
            )
        }catch(e:Exception){
            LoadResult.Error(e)
        }
    }


}
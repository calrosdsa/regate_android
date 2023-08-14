package app.regate.domain.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.regate.data.dto.account.billing.ConsumeDto
import app.regate.data.dto.account.billing.ConsumePaginationResponse

class ConsumesPagination(
    private val fetch:suspend (page:Int)-> ConsumePaginationResponse,
) : PagingSource<Int, ConsumeDto>() {
    override fun getRefreshKey(state: PagingState<Int, ConsumeDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ConsumeDto> {
        return try{
            val page = params.key?:1
            val res = fetch(page)
                LoadResult.Page(
                    data = res.results,
                    prevKey = null,
                    nextKey = if (res.nextPage == 0) null else res.nextPage
                )
        }catch(e:Exception){
            LoadResult.Error(e)
        }
    }


}
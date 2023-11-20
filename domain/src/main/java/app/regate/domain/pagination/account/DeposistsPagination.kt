package app.regate.domain.pagination.account

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.regate.data.dto.account.billing.DepositDto
import app.regate.data.dto.account.billing.DepositPaginationResponse

class DeposistsPagination(
    private val fetch:suspend (page:Int)-> DepositPaginationResponse,
) : PagingSource<Int, DepositDto>() {
    override fun getRefreshKey(state: PagingState<Int, DepositDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
    @Suppress("SuspiciousIndentation")
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DepositDto> {
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
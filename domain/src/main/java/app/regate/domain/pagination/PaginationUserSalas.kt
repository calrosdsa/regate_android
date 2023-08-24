package app.regate.domain.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.regate.data.dto.empresa.salas.PaginationSalaResponse
import app.regate.data.dto.empresa.salas.SalaDto

class PaginationUserSalas(
//    private val loadingState:ObservableLoadingCounter,
    private val getSalas:suspend (page:Int)->PaginationSalaResponse,
) : PagingSource<Int, SalaDto>() {
    override fun getRefreshKey(state: PagingState<Int, SalaDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SalaDto> {
        return try{
            val page = params.key?:1
            val res = getSalas(page)
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
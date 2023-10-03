package app.regate.domain.pagination.search

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.regate.data.dto.empresa.salas.PaginationSalaResponse
import app.regate.data.dto.empresa.salas.SalaDto

class PaginationSearchSalas(
    private val init:Boolean,
    private val getData:suspend (page:Int)->PaginationSalaResponse,
) : PagingSource<Int, SalaDto>() {
    override fun getRefreshKey(state: PagingState<Int, SalaDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SalaDto> {
        return try{
            if(init){

                val page = params.key?:1
                val res = getData(page)
                LoadResult.Page(
                    data = res.results,
                    prevKey = null,
                    nextKey = if (res.nextPage == 0) null else res.nextPage
                )
            }else {
                LoadResult.Invalid()
            }
        }catch(e:Exception){
            LoadResult.Error(e)
        }
    }


}
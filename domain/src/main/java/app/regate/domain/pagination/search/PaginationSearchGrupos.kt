package app.regate.domain.pagination.search

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.regate.data.dto.empresa.grupo.GrupoDto
import app.regate.data.dto.empresa.grupo.PaginationGroupsResponse
import app.regate.util.ObservableLoadingCounter

class PaginationSearchGrupos(
    private val init:Boolean,
    private val getData:suspend (page:Int)->PaginationGroupsResponse,
) : PagingSource<Int, GrupoDto>() {
    override fun getRefreshKey(state: PagingState<Int, GrupoDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GrupoDto> {
        return try{
            if(init){

            val page = params.key?:1
                val res = getData(page)
                LoadResult.Page(
                    data = res.results,
                    prevKey = null,
                    nextKey = if (res.page == 0) null else res.page
                )
            }else {
                LoadResult.Invalid()
            }
        }catch(e:Exception){
            LoadResult.Error(e)
        }
    }


}
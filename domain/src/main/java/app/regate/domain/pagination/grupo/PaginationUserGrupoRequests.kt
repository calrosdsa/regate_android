package app.regate.domain.pagination.grupo

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.regate.data.dto.empresa.grupo.PaginationUserGrupoRequest
import app.regate.data.dto.empresa.grupo.UserGrupoRequestDto

class PaginationUserGrupoRequests(
    private val getData:suspend (page:Int)->PaginationUserGrupoRequest
) : PagingSource<Int, UserGrupoRequestDto>() {
    override fun getRefreshKey(state: PagingState<Int, UserGrupoRequestDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserGrupoRequestDto> {
        return try{
            val page = params.key?:1
            val res = getData(page)
            LoadResult.Page(
                data = res.results,
                prevKey = null,
                nextKey = if (res.page == 0) null else res.page
            )
        }catch(e:Exception){
            LoadResult.Error(e)
        }
    }


}
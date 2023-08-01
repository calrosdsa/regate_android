package app.regate.domain.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.regate.data.dto.empresa.grupo.FilterGrupoData
import app.regate.data.dto.empresa.instalacion.InstalacionDto
import app.regate.data.dto.empresa.instalacion.PaginationInstalacionReponse
import app.regate.data.grupo.GrupoRepository
import app.regate.data.instalacion.InstalacionRepository

class PaginationInstalacionFilter(
    private val getInstalaciones:suspend (page:Int)->PaginationInstalacionReponse
) : PagingSource<Int, InstalacionDto>() {
    override fun getRefreshKey(state: PagingState<Int, InstalacionDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, InstalacionDto> {
        return try{
            val page = params.key?:1
            val res = getInstalaciones(page)
            LoadResult.Page(
                data = res.results,
                prevKey = null,
                nextKey = if (res.page == 0) null else res.page.plus(1)
            )
        }catch(e:Exception){
            LoadResult.Error(e)
        }
    }


}
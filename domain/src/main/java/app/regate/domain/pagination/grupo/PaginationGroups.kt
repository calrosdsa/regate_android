package app.regate.domain.pagination.grupo

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.regate.data.dto.empresa.grupo.FilterGrupoData
import app.regate.data.dto.empresa.grupo.GrupoDto
import app.regate.data.grupo.GrupoRepository

class PaginationGroups(
    private val grupoRepository: GrupoRepository
) : PagingSource<Int, GrupoDto>() {
    override fun getRefreshKey(state: PagingState<Int, GrupoDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GrupoDto> {
        return try{
            val page = params.key?:1
            val res = grupoRepository.filterGrupos(FilterGrupoData(category_id = 1),page)
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
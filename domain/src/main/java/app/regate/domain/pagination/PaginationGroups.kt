package app.regate.domain.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.regate.data.dto.empresa.grupo.FilterGrupoData
import app.regate.data.dto.empresa.grupo.GrupoDto
import app.regate.data.grupo.GrupoRepository

class PaginationGroups(
    private val grupoRepository: GrupoRepository
) : PagingSource<Int, GrupoDto>() {
    override fun getRefreshKey(state: PagingState<Int, GrupoDto>): Int? {
        TODO("Not yet implemented")
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GrupoDto> {
        return try{
            val page = params.key?:1
            val res = grupoRepository.filterGrupos(FilterGrupoData(category_id = 1),page)
            LoadResult.Page(
                data = res,
                prevKey = if (page == 1) null else page - 1,
                nextKey = page.plus(1)
            )
        }catch(e:Exception){
            LoadResult.Error(e)
        }
    }


}
package app.regate.domain.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.regate.data.dto.empresa.grupo.FilterGrupoData
import app.regate.data.dto.empresa.instalacion.InstalacionDto
import app.regate.data.dto.empresa.instalacion.PaginationInstalacionReponse
import app.regate.data.grupo.GrupoRepository
import app.regate.data.instalacion.InstalacionRepository
import app.regate.util.ObservableLoadingCounter
import kotlinx.coroutines.delay

class PaginationInstalacionFilter(
    private val isInit:Boolean,
    private val loadingState:ObservableLoadingCounter,
    private val getInstalaciones:suspend (page:Int)->PaginationInstalacionReponse,
) : PagingSource<Int, InstalacionDto>() {
    override fun getRefreshKey(state: PagingState<Int, InstalacionDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, InstalacionDto> {
        return try{
            loadingState.addLoader()
            val page = params.key?:1
            if(isInit){
                delay(500)
            val res = getInstalaciones(page)
            loadingState.removeLoader()
            LoadResult.Page(
                data = res.results,
                prevKey = null,
                nextKey = if (res.page == 0) null else res.page.plus(1)
            )
            }else{
                loadingState.removeLoader()
                LoadResult.Invalid()
            }
        }catch(e:Exception){
            loadingState.removeLoader()
            LoadResult.Error(e)
        }
    }


}
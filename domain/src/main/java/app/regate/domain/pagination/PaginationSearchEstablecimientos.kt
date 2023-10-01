package app.regate.domain.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.regate.data.dto.empresa.establecimiento.EstablecimientoDto
import app.regate.data.dto.empresa.establecimiento.PaginationEstablecimientoResponse
import app.regate.data.dto.empresa.grupo.FilterGrupoData
import app.regate.data.dto.empresa.instalacion.InstalacionDto
import app.regate.data.dto.empresa.instalacion.PaginationInstalacionReponse
import app.regate.data.grupo.GrupoRepository
import app.regate.data.instalacion.InstalacionRepository
import app.regate.util.ObservableLoadingCounter
import kotlinx.coroutines.delay

class PaginationSearchEstablecimientos(
    private val loadingState:ObservableLoadingCounter,
    private val getEstablecimientos:suspend (page:Int)->PaginationEstablecimientoResponse,
) : PagingSource<Int, EstablecimientoDto>() {
    override fun getRefreshKey(state: PagingState<Int, EstablecimientoDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, EstablecimientoDto> {
        return try{
            loadingState.addLoader()
            val page = params.key?:1
                val res = getEstablecimientos(page)
                loadingState.removeLoader()
                LoadResult.Page(
                    data = res.results,
                    prevKey = null,
                    nextKey = if (res.page == 0) null else res.page
                )
        }catch(e:Exception){
            loadingState.removeLoader()
            LoadResult.Error(e)
        }
    }


}
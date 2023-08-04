package app.regate.domain.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.regate.data.dto.empresa.establecimiento.EstablecimientoDto
import app.regate.data.dto.empresa.establecimiento.PaginationEstablecimientoResponse

class PaginationEstablecimientos(
    private val isInit:Boolean,
//    private val loadingState:ObservableLoadingCounter,
    private val getResults:suspend (page:Int)->PaginationEstablecimientoResponse,
) : PagingSource<Int, EstablecimientoDto>() {
    override fun getRefreshKey(state: PagingState<Int, EstablecimientoDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, EstablecimientoDto> {
        return try{
            val page = params.key?:1
            if(isInit){
            val res = getResults(page)
            LoadResult.Page(
                data = res.results,
                prevKey = null,
                nextKey = if (res.page == 0) null else res.page.plus(1)
            )
            }else{
                LoadResult.Invalid()
            }
        }catch(e:Exception){
            LoadResult.Error(e)
        }
    }


}
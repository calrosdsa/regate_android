package app.regate.domain.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.regate.data.dto.empresa.establecimiento.EstablecimientoReviewDto
import app.regate.data.dto.empresa.establecimiento.EstablecimientoReviews

class PaginationReviews(
    private val isInit:Boolean,
//    private val loadingState:ObservableLoadingCounter,
    private val getSalas:suspend (page:Int)->EstablecimientoReviews,
) : PagingSource<Int, EstablecimientoReviewDto>() {
    override fun getRefreshKey(state: PagingState<Int, EstablecimientoReviewDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, EstablecimientoReviewDto> {
        return try{
            val page = params.key?:1
            if(isInit){
            val res = getSalas(page)
            LoadResult.Page(
                data = res.results,
                prevKey = null,
                nextKey = if (res.page == 0) null else res.page
            )
            }else{
                LoadResult.Invalid()
            }
        }catch(e:Exception){
            LoadResult.Error(e)
        }
    }


}
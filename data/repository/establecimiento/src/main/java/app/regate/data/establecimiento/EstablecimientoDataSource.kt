package app.regate.data.establecimiento

import app.regate.data.dto.SearchFilterRequest
import app.regate.data.dto.empresa.establecimiento.CupoEstablecimiento
import app.regate.data.dto.empresa.establecimiento.CuposEstablecimientoRequest
import app.regate.data.dto.empresa.establecimiento.EstablecimientoDetailDto
import app.regate.data.dto.empresa.establecimiento.EstablecimientoDto
import app.regate.data.dto.empresa.establecimiento.EstablecimientoReview
import app.regate.data.dto.empresa.establecimiento.EstablecimientoReviews
import app.regate.data.dto.empresa.establecimiento.InitialData
import app.regate.data.dto.empresa.establecimiento.InitialDataFilter
import app.regate.data.dto.empresa.establecimiento.PaginationEstablecimientoResponse
import app.regate.data.dto.empresa.establecimiento.PhotoDto

interface EstablecimientoDataSource {
    suspend fun getEstablecimientos(d:InitialDataFilter):InitialData
    suspend fun getEstablecimentoPhotos(id:Long):List<PhotoDto>
    suspend fun getRecommendedEstablecimientos(d:InitialDataFilter,page:Int):PaginationEstablecimientoResponse
    suspend fun getNearEstablecimientos(lng:String,lat:String):List<EstablecimientoDto>

    suspend fun getEstablecimientoDetail(id:Long): EstablecimientoDetailDto
    suspend fun getEstablecimiento(id:Long):EstablecimientoDto
    suspend fun getEstablecimientoCupos(d:CuposEstablecimientoRequest):List<CupoEstablecimiento>
    suspend fun getEstablecimientoFavoritos():List<EstablecimientoDto>
    suspend fun likeEstablecimiento(id:Long)
    suspend fun removeLikeEstablecimiento(id:Long)
    suspend fun getEstablecimientoReview(id: Long, page: Int, size: Int): EstablecimientoReviews
    suspend fun createEstablecimientoReview(d: EstablecimientoReview): EstablecimientoReview
    suspend fun getReviewUser(establecimientoId:Long):EstablecimientoReview
    suspend fun searcEstablecimientos(d:SearchFilterRequest, page:Int, size: Int):PaginationEstablecimientoResponse
}


package app.regate.data.establecimiento

import app.regate.api.ApiResult
import app.regate.data.dto.empresa.establecimiento.CupoEstablecimiento
import app.regate.data.dto.empresa.establecimiento.CuposEstablecimientoRequest
import app.regate.data.mappers.EstablecimientoDtoToEstablecimiento
import app.regate.api.handleApi
import app.regate.data.auth.store.AuthStore
import app.regate.data.dto.ResponseMessage
import app.regate.data.dto.empresa.establecimiento.EstablecimientoDetailDto
import app.regate.data.dto.empresa.establecimiento.EstablecimientoDto
import app.regate.data.dto.empresa.establecimiento.EstablecimientoReview
import app.regate.data.dto.empresa.establecimiento.EstablecimientoReviews
import app.regate.data.dto.empresa.establecimiento.InitialData
import app.regate.data.dto.empresa.establecimiento.InitialDataFilter
import app.regate.data.dto.empresa.establecimiento.PaginationEstablecimientoResponse
import app.regate.models.Establecimiento
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import me.tatarka.inject.annotations.Inject

@Inject
class EstablecimientoDataSourceImpl(
    private val client:HttpClient,
    private val authStore: AuthStore
): EstablecimientoDataSource {
        override suspend fun getEstablecimientoFavoritos(): List<EstablecimientoDto> {
        val token = authStore.get()?.accessToken
        return client.get("/v1/establecimiento/liked/"){
            header("Authorization","Bearer $token")
        }.body()
    }

    override suspend fun likeEstablecimiento(id: Long) {
        val token = authStore.get()?.accessToken
        client.put("/v1/establecimiento/like/${id}/"){
            header("Authorization","Bearer $token")
        }.body<ResponseMessage>()
    }

    override suspend fun removeLikeEstablecimiento(id: Long) {
        val token = authStore.get()?.accessToken
        client.put("/v1/establecimiento/dislike/${id}/"){
            header("Authorization","Bearer $token")
        }.body<ResponseMessage>()
    }

    override suspend fun getEstablecimientoReview(id: Long, page: Int,size:Int): EstablecimientoReviews {
        return client.get("/v1/review/establecimiento/${id}/?page=${page}&size=${size}").body()
    }

    override suspend fun createEstablecimientoReview(d: EstablecimientoReview): EstablecimientoReview {
        val token = authStore.get()?.accessToken
        return client.post("/v1/review/establecimiento/") {
            header("Authorization","Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(d)
        }.body()
    }

    override suspend fun getReviewUser(establecimientoId: Long): EstablecimientoReview {
        val token = authStore.get()?.accessToken
        return client.get("/v1/review/establecimiento/profile/${establecimientoId}/") {
            header("Authorization","Bearer $token")
        }.body()
    }

    override suspend fun getEstablecimientoCupos(d: CuposEstablecimientoRequest):List<CupoEstablecimiento> {
        return client.post("/v1/establecimiento/cupos/"){
            contentType(ContentType.Application.Json)
            setBody(d)
        }.body()
    }
    override suspend fun getEstablecimientos(d:InitialDataFilter): InitialData {
        return client.post("/v1/establecimientos/"){
            contentType(ContentType.Application.Json)
            setBody(d)
        }.body()
    }

    override suspend fun getRecommendedEstablecimientos(d:InitialDataFilter,page:Int): PaginationEstablecimientoResponse {
        return client.post("/v1/establecimientos/recommended/?page=${page}"){
            contentType(ContentType.Application.Json)
            setBody(d)
        }.body()
    }

    override suspend fun getNearEstablecimientos(
        lng: String,
        lat: String
    ): List<EstablecimientoDto> {
        return client.get("/v1/establecimientos/near/?lng=$lng&lat=$lat").body()
    }

    override suspend fun getEstablecimientoDetail(id: Long): EstablecimientoDetailDto {
        return client.get("/v1/establecimiento-detail/${id}/").body()
    }

    override suspend fun getEstablecimiento(id: Long): EstablecimientoDto {
        return client.get("/v1/establecimiento/${id}/").body()
    }
}
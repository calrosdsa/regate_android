package app.regate.data.establecimiento

import app.regate.api.ApiResult
import app.regate.data.dto.empresa.establecimiento.CupoEstablecimiento
import app.regate.data.dto.empresa.establecimiento.CuposEstablecimientoRequest
import app.regate.data.mappers.EstablecimientoDtoToEstablecimiento
import app.regate.api.handleApi
import app.regate.data.dto.empresa.establecimiento.EstablecimientoDetailDto
import app.regate.data.dto.empresa.establecimiento.EstablecimientoDto
import app.regate.models.Establecimiento
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import me.tatarka.inject.annotations.Inject

@Inject
class EstablecimientoDataSourceImpl(
    private val client:HttpClient,
): EstablecimientoDataSource {
    //    override suspend fun me(): Me {
//        return client.get("/v1/account/").body()
//    }
//
//    override suspend fun login(d: LoginRequest): LoginResponse {
//        val res =  client.post("/v1/account/login/"){
//            contentType(ContentType.Application.Json)
//            setBody(LoginRequest(email = d.email, password = d.password))
//        }.body<LoginResponse>()
//        return res
//    }
    override suspend fun getEstablecimientoCupos(d: CuposEstablecimientoRequest):List<CupoEstablecimiento> {
        return client.post("/v1/establecimiento/cupos/"){
            contentType(ContentType.Application.Json)
            setBody(d)
        }.body()
    }
    override suspend fun getEstablecimientos(): List<EstablecimientoDto> {
        return client.get("/v1/establecimientos/").body()
    }

    override suspend fun getEstablecimiento(id: Long): EstablecimientoDetailDto {
        return client.get("/v1/establecimiento/${id}/").body()
    }
}
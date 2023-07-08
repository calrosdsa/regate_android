package app.regate.data.instalacion

import app.regate.api.ApiResult
import app.regate.data.dto.empresa.establecimiento.CupoInstaDto
import app.regate.data.dto.empresa.establecimiento.CuposRequest
import app.regate.data.dto.empresa.instalacion.InstalacionDto
import app.regate.data.dto.empresa.instalacion.InstalacionRequest
import app.regate.data.dto.empresa.instalacion.InstalacionesAvailables
import app.regate.data.mappers.toInstalacion
import app.regate.api.handleApi
import app.regate.data.dto.empresa.instalacion.FilterInstalacionData
import app.regate.models.Instalacion
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import me.tatarka.inject.annotations.Inject

@Inject
class InstalacionDataSourceImpl(
    private val client:HttpClient
): InstalacionDataSource {
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
    override suspend fun filterInstalaciones(d: FilterInstalacionData,page:Int?): List<InstalacionDto> {
        return client.post("/v1/instalacion/filter/?page=${page?:0}"){
            contentType(ContentType.Application.Json)
            setBody(d)
        }.body()
    }
    override suspend fun getCupos(d: CuposRequest): List<CupoInstaDto> {
        return client.post("/v1/reserva/instalacion/cupos/"){
            contentType(ContentType.Application.Json)
            setBody(d)
        }.body()
    }
    override suspend fun getInstalaciones(id:Long): List<Instalacion> {
        return client.get("/v1/instalacion/instalaciones/$id/").body<List<InstalacionDto>>().map { it.toInstalacion() }
    }

    override suspend fun getInstalacion(id: Long): Instalacion {
        return client.get("/v1/instalacion/$id/").body<InstalacionDto>().toInstalacion()
    }

    override suspend fun getInstalacionesAvailables(d: InstalacionRequest): InstalacionesAvailables {
        return client.post("/v1/instalacion/availables/"){
            contentType(ContentType.Application.Json)
            setBody(d)
        }.body()
    }
}
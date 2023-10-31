package app.regate.data.sala

import app.regate.data.auth.store.AuthStore
import app.regate.data.dto.ResponseMessage
import app.regate.data.dto.SearchFilterRequest
import app.regate.data.dto.empresa.salas.JoinSalaRequest
import app.regate.data.dto.empresa.salas.SalaDetail
import app.regate.data.dto.empresa.salas.SalaDto
import app.regate.data.dto.empresa.salas.CompleteSalaRequest
import app.regate.data.dto.empresa.salas.CreateSalaResponse
import app.regate.data.dto.empresa.salas.MessageSalaDto
import app.regate.data.dto.empresa.salas.MessageSalaPagination
import app.regate.data.dto.empresa.salas.PaginationSalaResponse
import app.regate.data.dto.empresa.salas.SalaCompleteDetail
import app.regate.data.dto.empresa.salas.SalaFilterData
import app.regate.data.dto.empresa.salas.SalaRequestDto
import app.regate.data.dto.empresa.salas.UserSalaDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import me.tatarka.inject.annotations.Inject

@Inject
class SalaDataSourceImpl(
    private val client:HttpClient,
    private val authStore: AuthStore
): SalaDataSource {

    override suspend fun createSala(d: SalaRequestDto):CreateSalaResponse {
        val token = authStore.get()?.accessToken
        return client.post("/v1/sala/"){
            header("Authorization", "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(d)
        }.body()
    }

    override suspend fun exitSala(id: Int) {
        val token = authStore.get()?.accessToken
        client.get("/v1/sala/exit/${id}/"){
            header("Authorization", "Bearer $token")
        }
    }

    override suspend fun getUsersSala(salaId: Long): List<UserSalaDto> {
        return client.get("/v1/sala/users/${salaId}/").body()
    }

    override suspend fun searchSalas(
        d: SearchFilterRequest,
        page: Int,
        size: Int
    ): PaginationSalaResponse {
        return client.post("/v1/salas/search/?page=${page}&size=${size}"){
            contentType(ContentType.Application.Json)
            setBody(d)
        }.body()
    }

    override suspend fun getCompleteSalaHistory(salaId: Long): SalaCompleteDetail {
        val token = authStore.get()?.accessToken
        return client.get("/v1/sala/sala-complete-history/${salaId}/"){
            header("Authorization", "Bearer $token")
        }.body()
    }

    override suspend fun completeSala(d: CompleteSalaRequest) {
        val token = authStore.get()?.accessToken
        client.post("/v1/sala/sala-complete/"){
            header("Authorization", "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(d)
        }
    }

    override suspend fun syncMessages(d: List<MessageSalaDto>): List<MessageSalaDto> {
        return client.post("/v1/sala/message/sync-message/"){
            contentType(ContentType.Application.Json)
            setBody(d)
        }.body()
    }

    override suspend fun getEstablecimientoSalas(id: Long): List<SalaDto> {
            return client.get("/v1/salas/establecimiento/${id}/").body()

//            ResponseData.Success(data = res)
//        }catch (e:ResponseException){
//            ResponseData.Error(message = e.errorMessage<ErrorResponse>().message)
//        }
    }

    override suspend fun filterSalas(d: SalaFilterData,page:Int): PaginationSalaResponse {
        return client.post("/v1/salas/filter/?page=${page}"){
            contentType(ContentType.Application.Json)
            setBody(d)
        }.body()
    }
    override suspend fun getSalasUser(page:Int): PaginationSalaResponse {
        val token = authStore.get()?.accessToken
        return client.get("/v1/salas/user/?page=${page}"){
            header("Authorization","Bearer $token")
        }.body()
    }

    override suspend fun getGrupoSalas(id:Long,page:Int): PaginationSalaResponse {
        return client.get("/v1/salas/grupo/${id}/?page=${page}").body()
    }


    override suspend fun getMessagesSala(id: Long,page: Int): MessageSalaPagination {
        return client.get("/v1/sala/messages/${id}/?page=${page}").body()
    }

    override suspend fun getSala(id: Long): SalaDetail {
        return client.get("/v1/sala/${id}/").body()
    }

    override suspend fun joinSala(d: JoinSalaRequest):ResponseMessage{
        val token = authStore.get()?.accessToken
        return client.post("/v1/sala/add-user/"){
            header("Authorization","Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(d)
        }.body()
    }
}


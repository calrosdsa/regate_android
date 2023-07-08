package app.regate.data.grupo

import app.regate.data.auth.store.AuthStore
import app.regate.data.dto.ResponseMessage
import app.regate.data.dto.account.user.ProfileDto
import app.regate.data.dto.empresa.grupo.AddUserGrupoRequest
import app.regate.data.dto.empresa.grupo.FilterGrupoData
import app.regate.data.dto.empresa.grupo.GrupoDto
import app.regate.data.dto.empresa.grupo.GrupoMessageDto
import app.regate.data.dto.empresa.grupo.GrupoResponse
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
class GrupoDataSourceImpl(
    private val client:HttpClient,
    private val authStore: AuthStore
): GrupoDataSource {
//    override suspend fun createSala(d: SalaRequestDto):ResponseMessage {
//        val token = authStore.get()?.accessToken
//        return client.post("/v1/sala/"){
//            header("Authorization", "Bearer $token")
//            contentType(ContentType.Application.Json)
//            setBody(d)
//        }.body()
//    }
    override suspend fun filterGrupos(d: FilterGrupoData): List<GrupoDto> {
            return client.post("/v1/grupo/filter/"){
                contentType(ContentType.Application.Json)
                setBody(d)
            }.body()
    }

    override suspend fun getUsersGrupo(id: Long): List<ProfileDto> {
        return client.get("/v1/grupo/users/${id}/").body()
    }

    override suspend fun getMessagesGrupo(id: Long,page:Int): List<GrupoMessageDto> {
        return client.get("/v1/grupo/messages/${id}/?page=${page}").body()
    }

    override suspend fun getGrupo(id: Long):GrupoResponse {
        return client.get("/v1/grupo/${id}/").body()
    }

    override suspend fun joinGrupo(d: AddUserGrupoRequest):ResponseMessage{
        val token = authStore.get()?.accessToken
        return client.post("/v1/grupo/add-user/"){
            header("Authorization","Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(d)
        }.body()
    }
}


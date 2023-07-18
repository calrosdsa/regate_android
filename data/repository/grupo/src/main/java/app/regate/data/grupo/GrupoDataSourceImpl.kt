package app.regate.data.grupo

import app.regate.data.auth.store.AuthStore
import app.regate.data.dto.ResponseMessage
import app.regate.data.dto.account.user.ProfileDto
import app.regate.data.dto.empresa.grupo.AddUserGrupoRequest
import app.regate.data.dto.empresa.grupo.FilterGrupoData
import app.regate.data.dto.empresa.grupo.GroupRequest
import app.regate.data.dto.empresa.grupo.GrupoDto
import app.regate.data.dto.empresa.grupo.GrupoMessageDto
import app.regate.data.dto.empresa.grupo.GrupoResponse
import app.regate.data.dto.empresa.grupo.UserGrupoDto
import app.regate.models.Message
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import me.tatarka.inject.annotations.Inject

@Inject
class GrupoDataSourceImpl(
    private val client:HttpClient,
    private val authStore: AuthStore
): GrupoDataSource {
    override suspend fun myGroups(): List<GrupoDto> {
        val token = authStore.get()?.accessToken
        return client.get("/v1/grupo/user-groups/"){
            header("Authorization","Bearer $token")
        }.body()
    }
    override  suspend fun syncMessages(d:List<GrupoMessageDto>):List<GrupoMessageDto>{
        return client.post("/v1/grupo/message/sync-message/"){
            contentType(ContentType.Application.Json)
            setBody(d)
        }.body()
    }
    override suspend fun filterGrupos(d: FilterGrupoData,page: Int): List<GrupoDto> {
            return client.post("/v1/grupo/filter/?page=${page}"){
                contentType(ContentType.Application.Json)
                setBody(d)
            }.body()
    }

    override suspend fun getUsersGrupo(id: Long): List<UserGrupoDto> {
        return client.get("/v1/grupo/users/${id}/").body()
    }

    override suspend fun createGroup(d: GroupRequest):GrupoDto {
        val token = authStore.get()?.accessToken
        val res = client.submitFormWithBinaryData(
            url = "/v1/grupo/create-grupo/",
            formData = formData {
                append("id",d.id)
                append("descripcion", d.description)
                append("name", d.name)
                append("visibility",d.visibility)
                append("photo_url",d.photo_url?:"")
                d.fileData?.byteArray?.let {
                append("photo",it, Headers.build {
                    append(HttpHeaders.ContentType,d.fileData?.type?:"")
                    append(HttpHeaders.ContentDisposition,"filename=${d.fileData?.name?:""}")
                })
                }
            },
        ){
            header("Authorization","Bearer $token")
        }
        return res.body()
    }

    override suspend fun removeUserFromGroup(id: Long) {
        val token = authStore.get()?.accessToken
        client.put("/v1/grupo/users/remove/${id}/"){
            header("Authorization","Bearer $token")
        }
    }

    override suspend fun changeStatusUser(id: Long, status: Boolean) {
        val token = authStore.get()?.accessToken
        client.put("/v1/grupo/users/update-user-status/$id/$status/"){
            header("Authorization","Bearer $token")
        }
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


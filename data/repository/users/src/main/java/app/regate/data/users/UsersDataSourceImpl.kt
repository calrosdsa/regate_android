package app.regate.data.users

import app.regate.data.auth.store.AuthStore
import app.regate.data.dto.FileData
import app.regate.data.dto.SearchFilterRequest
import app.regate.data.dto.account.user.PaginationProfilesResponse
import app.regate.data.dto.account.user.ProfileCategoryRequest
import app.regate.data.dto.account.user.ProfileDetailDto
import app.regate.data.dto.account.user.ProfileDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import me.tatarka.inject.annotations.Inject

@Inject
class UsersDataSourceImpl(
    private val client:HttpClient,
    private val authStore: AuthStore
): UsersDataSource {
    override suspend fun getProfile(id: Long): ProfileDetailDto {
        return client.get("v1/users/profile/${id}/").body()
    }

    override suspend fun editProfile(d: ProfileDto,file:FileData?): ProfileDto {
        val token = authStore.get()?.accessToken
        val res = client.submitFormWithBinaryData(
            url = "/v1/users/profile/edit/",
            formData = formData {
                append("id",d.profile_id)
                append("uuid",d.uuid)
                append("nombre", d.nombre)
                append("apellido", d.apellido?:"")
                append("photo_url",d.profile_photo?:"")
                   file?.byteArray?.let {
                       append("photo", it, Headers.build {
                           append(HttpHeaders.ContentType,file.type)
                           append(HttpHeaders.ContentDisposition,"filename=${file.name}")
                       })
                   }
            },
        ){
            header("Authorization","Bearer $token")
        }
        return res.body()
    }

    override suspend fun searchUsers(
        d: SearchFilterRequest,
        page: Int,
        size: Int
    ): PaginationProfilesResponse {
        return client.post("/v1/users/profile/search/?page=${page}&size=${size}"){
            contentType(ContentType.Application.Json)
            setBody(d)
        }.body()
    }

    override suspend fun updateCategoriesProfile(d: List<ProfileCategoryRequest>){
        val token = authStore.get()?.accessToken
        return client.post("/v1/profile/update-categories/"){
            header("Authorization","Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(d)
        }.body()
    }
}
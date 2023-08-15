package app.regate.data.system

import app.regate.data.auth.store.AuthStore
import app.regate.data.dto.empresa.coin.QrRequest
import app.regate.data.dto.empresa.coin.QrResponse
import app.regate.data.dto.empresa.coin.RecargaCoinDto
import app.regate.data.dto.empresa.coin.TokenQrReponse
import app.regate.data.dto.empresa.coin.UserBalance
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import me.tatarka.inject.annotations.Inject

@Inject
class SystemDataSourceImpl(
    private val client:HttpClient,
    private val authStore: AuthStore
): SystemDataSource {
    override suspend fun getUserBalance(): UserBalance {
        val token = authStore.get()?.accessToken
        return client.get("/v1/coin/user-balance/"){
            header("Authorization","Bearer $token")
        }.body()
    }

    override suspend fun getRecargaCoins(): List<RecargaCoinDto> {
        return client.get("/v1/coin/list/").body()
    }

    override suspend fun getToken(): TokenQrReponse {
        return client.get("v1/coin/token/").body()
    }

    override suspend fun getQr(d: QrRequest,token:String): QrResponse {
        return client.post{
            url("http://test.bnb.com.bo/QRSimple.API/api/v1/main/getQRWithImageAsync")
            header("Authorization","Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(d)
        }.body()
    }

}


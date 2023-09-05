package app.regate.data.system

import app.regate.data.auth.store.AuthStore
import app.regate.data.dto.empresa.coin.QrRequest
import app.regate.data.dto.empresa.coin.QrResponse
import app.regate.data.dto.empresa.coin.RecargaCoinDto
import app.regate.data.dto.empresa.coin.TokenQrReponse
import app.regate.data.dto.empresa.coin.UserBalance
import app.regate.data.dto.system.NotificationCount
import app.regate.data.dto.system.NotificationDto
import app.regate.data.dto.system.ReportData
import app.regate.models.Notification
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
    override suspend fun sendReport(d: ReportData) {
        client.post("/v1/system/report-abuse/") {
            contentType(ContentType.Application.Json)
            setBody(d)
        }
    }

    override suspend fun getNotifications(): List<NotificationDto> {
        val token = authStore.get()?.accessToken
        return client.get("/v1/system/notifications/"){
            header("Authorization","Bearer $token")
        }.body()
    }

    override suspend fun getNotificationCount():NotificationCount {
        val token = authStore.get()?.accessToken
        return client.get("/v1/system/notifications-count/"){
            header("Authorization","Bearer $token")
        }.body()
    }
}


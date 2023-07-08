package app.regate.data.reserva

import app.regate.data.auth.store.AuthStore
import app.regate.data.dto.ResponseMessage
import app.regate.data.dto.account.reserva.ReservaDto
import app.regate.data.dto.account.reserva.ReservaRequest
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
class ReservaDataSourceImpl(
    private val client:HttpClient,
    private val authStore: AuthStore
): ReservaDataSource {
    override suspend fun getReserva(id: Long): ReservaDto {
        val token = authStore.get()?.accessToken
        return client.get("/v1/reserva/${id}/"){
            header("Authorization","Bearer $token")
        }.body()
    }

    override suspend fun getReservas(): List<ReservaDto> {
        val token = authStore.get()?.accessToken
        return client.get("/v1/reservas/"){
            header("Authorization","Bearer $token")
        }.body()
    }

    override suspend fun confirmarReservas(data:ReservaRequest): ResponseMessage {
        val token = authStore.get()?.accessToken
        return client.post("/v1/reserva/"){
            header("Authorization","Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(data)
        }.body()
    }

}


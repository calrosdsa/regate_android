package app.regate.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.HttpResponse

suspend inline fun <reified T> HttpResponse.handleApi(
): ApiResult<T> {
    return try {
        ApiSuccess(data = body())
    } catch (e: ResponseException) {
        ApiError(code = e.response.status.value, message = e.errorMessage())
    }
}

typealias HttpClientMessage = HttpClient
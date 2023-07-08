package app.regate.api

import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.HttpResponse

class MissingPageException(response: HttpResponse, cachedResponseText: String) :
    ResponseException(response, cachedResponseText) {
    override val message: String = "Missing page: ${response.call.request.url}. " +
            "Status: ${response.status}."
}
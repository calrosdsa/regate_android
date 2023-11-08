package app.regate.inject

import android.app.Application
import app.regate.api.MissingPageException
import app.regate.constant.Host
import app.regate.settings.AppPreferences
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Provides
import okhttp3.Cache
import okhttp3.ConnectionPool
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import java.io.File
import java.util.concurrent.TimeUnit

interface NetworkComponent {
    @ApplicationScope
    @Provides
    fun provideOkHttpClient(
        context:Application
    ):OkHttpClient =OkHttpClient.Builder()
        .cache(Cache(File(context.cacheDir, "api_cache"), 50L * 1024 * 1024))
        .connectionPool(ConnectionPool(10, 2, TimeUnit.MINUTES))
        .dispatcher(
            Dispatcher().apply {
                // Allow for increased number of concurrent image fetches on same host
                maxRequestsPerHost = 10
            },
        )
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .pingInterval(20,TimeUnit.SECONDS)
        .build()

    @ApplicationScope
    @Provides
    fun provideKtorClient(
        client:OkHttpClient,
        preferences:AppPreferences
    ) : HttpClient = HttpClient(OkHttp) {
        defaultRequest {
//            url("http://172.20.20.76:9090")
            url(Host.url)
        }
        engine {
            preconfigured = client
        }
        install(ContentNegotiation){
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(WebSockets) {
            pingInterval = 20_000
        }
        expectSuccess = true
        HttpResponseValidator {
            handleResponseExceptionWithRequest { exception, _ ->
                val clientException = exception as? ClientRequestException
                    ?: return@handleResponseExceptionWithRequest
                val exceptionResponse = clientException.response
                if (exceptionResponse.status == HttpStatusCode.NotFound) {
                    val exceptionResponseText = exceptionResponse.bodyAsText()
                    throw MissingPageException(
                        exceptionResponse,
                        exceptionResponseText
                    )
                }
                if (exceptionResponse.status == HttpStatusCode.Unauthorized) {
                    val exceptionResponseText = exceptionResponse.bodyAsText()
                    throw MissingPageException(
                        exceptionResponse,
                        exceptionResponseText
                    )
                }
                if (exceptionResponse.status == HttpStatusCode.BadRequest) {
                    val exceptionResponseText = exceptionResponse.bodyAsText()
                    throw MissingPageException(
                        exceptionResponse,
                        exceptionResponseText
                    )
                }
                if (exceptionResponse.status == HttpStatusCode.UnprocessableEntity) {
                    val exceptionResponseText = exceptionResponse.bodyAsText()
                    throw MissingPageException(
                        exceptionResponse,
                        exceptionResponseText
                    )
                }
            }
        }
//        install(Auth){
//            bearer {
//                loadTokens {
//                    BearerTokens(preferences.token,preferences.token)
//                }
//            }
    }
}



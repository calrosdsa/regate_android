package app.regate.api

import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.HttpResponse

sealed class ApiResult<T>
class ApiSuccess<T>(val data: T? = null) : ApiResult<T>()
class ApiError<T>(val code: Int, val message: String) : ApiResult<T>()


//class ApiException<T : Any>(val e: Throwable) : ApiResult<T>

//sealed class ResponseData<T>{
//    class Success<T>(val data: T? = null):ResponseData<T>()
//    class Error<T>(val message:String):ResponseData<T>()
//}

//data class ErrorResponse(
//    val message:String
//)

suspend inline fun <reified T>ResponseException.errorMessage() = response.body<T>()

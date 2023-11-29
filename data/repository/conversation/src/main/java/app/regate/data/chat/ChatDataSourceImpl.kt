package app.regate.data.chat

import app.regate.compoundmodels.UserProfileGrupoAndSalaDto
import app.regate.constant.HostMessage
import app.regate.data.auth.store.AuthStore
import app.regate.data.dto.chat.ChatDto
import app.regate.data.dto.chat.DeleteMessageRequest
import app.regate.data.dto.chat.DeletedMessagesIds
import app.regate.data.dto.chat.MessagePublishRequest
import app.regate.data.dto.chat.MessagePublishResponse
import app.regate.data.dto.chat.PaginateChatResponse
import app.regate.data.dto.chat.RequestChatUnreadMessages
import app.regate.data.dto.chat.RequestUserGroupAndRoom
import app.regate.data.dto.empresa.conversation.Conversation
import app.regate.data.dto.empresa.conversation.ConversationId
import app.regate.data.dto.empresa.conversation.ConversationMessage
import app.regate.data.dto.empresa.conversation.PaginationConversationMessages
import app.regate.data.dto.empresa.grupo.GrupoMessageDto
import app.regate.data.dto.empresa.grupo.PaginationGroupMessages
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject

@Inject
class ChatDataSourceImpl(
    clientMessage:HttpClient,
    private val authStore: AuthStore
): ChatDataSource {
    companion object {
        const val baseUrl = HostMessage.url
//          const val baseUrl = "http://172.20.20.76:9091"
    }
    val client = clientMessage.config {
        install(ContentNegotiation){
            json(Json{
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }
    override suspend fun getchatUnreadMessages(d:RequestChatUnreadMessages): List<GrupoMessageDto> {
        return client.post{
            url("$baseUrl/v1/chat/unread-messages/")
            contentType(ContentType.Application.Json)
            setBody(d)
        }.body()
    }
    override suspend fun getMessages(id: Long, page: Int): PaginationConversationMessages{
        return client.get{
            url("$baseUrl/v1/conversation/messages/${id}/?page=${page}")
        }.body()
    }

    override suspend fun syncMessages(d:List<ConversationMessage>): List<ConversationMessage> {
        return client.post{
            url("$baseUrl/v1/conversation/sync-messages/")
            contentType(ContentType.Application.Json)
            setBody(d)
        }.body()
    }

    override suspend fun getConversationId(establecimientoId: Long): ConversationId {
        val token = authStore.get()?.accessToken
        return client.get {
            url("$baseUrl/v1/conversation/get-id/1/")
            header("Authorization","Bearer $token")
        }.body()
    }

    override suspend fun getChat(id: Long, typeChat: Int): ChatDto {
        return client.get {
            url("$baseUrl/v1/chat/$id/$typeChat/")
        }.body()
    }

    override suspend fun getConversations(): List<Conversation> {
        val token = authStore.get()?.accessToken
        return  client.get{
            url("$baseUrl/v1/conversations/")
            header("Authorization","Bearer $token")
        }.body()
    }

    //Chat
    override suspend fun getChats(page:Int):PaginateChatResponse{
        val token = authStore.get()?.accessToken
        return client.config {
        }.get{
            url("$baseUrl/v1/chats/?page=${page}")
            header("Authorization","Bearer $token")
        }.body()
    }

    override suspend fun getUnreadMessages(page: Int): PaginationGroupMessages {
        val token = authStore.get()?.accessToken
        return client.config {
        }.get{
            url("$baseUrl/v1/chat/grupo/unread-messages/?page=${page}")
            header("Authorization","Bearer $token")
        }.body()
    }

    override suspend fun publishMessage(data: MessagePublishRequest):MessagePublishResponse {
        val token = authStore.get()?.accessToken
        return client.post{
            url("$baseUrl/v1/chat/publish/message/")
            header("Authorization","Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(data)
        }.body()
    }

    override suspend fun notifyNewUser(chatId: Long, d: UserProfileGrupoAndSalaDto) {
        return client.post{
            url("$baseUrl/v1/chat/notify/new-user/$chatId/")
            contentType(ContentType.Application.Json)
            setBody(d)
        }.body()
    }

    override suspend fun deleteMessage(data: DeleteMessageRequest) {
        val token = authStore.get()?.accessToken
        return client.post{
            url("$baseUrl/v1/chat/delete/message/")
            header("Authorization","Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(data)
        }.body()
    }

    override suspend fun getDeletedMessages(id: Long): DeletedMessagesIds {
        return client.get{
            url("$baseUrl/v1/chat/deleted/messages/$id/")
        }.body()
    }
    override suspend fun getUsers(d:RequestUserGroupAndRoom): List<UserProfileGrupoAndSalaDto>? {
        return client.post{
            url("$baseUrl/v1/chat/users/")
            contentType(ContentType.Application.Json)
            setBody(d)
        }.body()
    }
}


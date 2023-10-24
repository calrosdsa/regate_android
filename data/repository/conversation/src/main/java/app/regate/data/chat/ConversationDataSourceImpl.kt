package app.regate.data.chat

import app.regate.constant.HostMessage
import app.regate.data.auth.store.AuthStore
import app.regate.data.dto.chat.PaginateChatResponse
import app.regate.data.dto.empresa.conversation.Conversation
import app.regate.data.dto.empresa.conversation.ConversationId
import app.regate.data.dto.empresa.conversation.ConversationMessage
import app.regate.data.dto.empresa.conversation.PaginationConversationMessages
import app.regate.data.dto.empresa.grupo.PaginationGroupMessages
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
class ConversationDataSourceImpl(
    private val client:HttpClient,
    private val authStore: AuthStore
): ConversationDataSource {
    companion object {
        const val baseUrl = HostMessage.url
//          const val baseUrl = "http://172.20.20.76:9091"
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
}


package app.regate.data.coin

import app.regate.data.auth.store.AuthStore
import app.regate.data.dto.empresa.conversation.Conversation
import app.regate.data.dto.empresa.conversation.ConversationId
import app.regate.data.dto.empresa.conversation.ConversationMessage
import app.regate.data.dto.empresa.conversation.PaginationConversationMessages
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
    override suspend fun getMessages(id: Long, page: Int): PaginationConversationMessages{
        return client.get{
            url("http://172.20.20.76:9091/v1/conversation/messages/${id}/?page=${page}")
        }.body()
    }

    override suspend fun syncMessages(d:List<ConversationMessage>): List<ConversationMessage> {
        return client.post{
            url("http://172.20.20.76:9091/v1/conversation/sync-messages/")
            contentType(ContentType.Application.Json)
            setBody(d)
        }.body()
    }

    override suspend fun getConversationId(establecimientoId: Long): ConversationId {
        val token = authStore.get()?.accessToken
        return client.get {
            url("http://172.20.20.76:9091/v1/conversation/get-id/1/")
            header("Authorization","Bearer $token")
        }.body()
    }

    override suspend fun getConversations(): List<Conversation> {
        val token = authStore.get()?.accessToken
        return  client.get{
            url("http://172.20.20.76:9091/v1/conversations/")
            header("Authorization","Bearer $token")
        }.body()
    }
}


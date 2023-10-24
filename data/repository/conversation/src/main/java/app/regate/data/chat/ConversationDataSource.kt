package app.regate.data.chat

import app.regate.data.dto.chat.PaginateChatResponse
import app.regate.data.dto.empresa.coin.RecargaCoinDto
import app.regate.data.dto.empresa.conversation.Conversation
import app.regate.data.dto.empresa.conversation.ConversationId
import app.regate.data.dto.empresa.conversation.ConversationMessage
import app.regate.data.dto.empresa.conversation.PaginationConversationMessages
import app.regate.data.dto.empresa.grupo.PaginationGroupMessages

interface ConversationDataSource {
    suspend fun getMessages(id:Long,page:Int):PaginationConversationMessages
    suspend fun getConversations():List<Conversation>
    suspend fun syncMessages(d:List<ConversationMessage>):List<ConversationMessage>
    suspend fun getConversationId(establecimientoId:Long):ConversationId

    //chat
    suspend fun getChats(page: Int):PaginateChatResponse
    suspend fun getUnreadMessages(page:Int):PaginationGroupMessages
}


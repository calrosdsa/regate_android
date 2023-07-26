package app.regate.data.coin

import app.regate.data.dto.empresa.coin.RecargaCoinDto
import app.regate.data.dto.empresa.conversation.Conversation
import app.regate.data.dto.empresa.conversation.ConversationMessage

interface ConversationDataSource {
    suspend fun getMessages(id:Long,page:Int):List<ConversationMessage>
    suspend fun getConversations():List<Conversation>
    suspend fun syncMessages(d:List<ConversationMessage>):List<ConversationMessage>
}


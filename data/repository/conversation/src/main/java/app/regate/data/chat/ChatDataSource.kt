package app.regate.data.chat

import app.regate.compoundmodels.UserProfileGrupoAndSalaDto
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

interface ChatDataSource {
    suspend fun getMessages(id:Long,page:Int):PaginationConversationMessages
    suspend fun getConversations():List<Conversation>
    suspend fun getchatUnreadMessages(d:RequestChatUnreadMessages):List<GrupoMessageDto>
    suspend fun syncMessages(d:List<ConversationMessage>):List<ConversationMessage>
    suspend fun getConversationId(establecimientoId:Long):ConversationId

    //chat
    suspend fun getChat(id:Long,typeChat:Int):ChatDto
    suspend fun getChats(page: Int):PaginateChatResponse
    suspend fun getUnreadMessages(page:Int):PaginationGroupMessages
    suspend fun publishMessage(data:MessagePublishRequest):MessagePublishResponse
    suspend fun notifyNewUser(chatId:Long,d:UserProfileGrupoAndSalaDto)
    suspend fun deleteMessage(data:DeleteMessageRequest)
    suspend fun getDeletedMessages(id:Long):DeletedMessagesIds
    suspend fun getUsers(d:RequestUserGroupAndRoom):List<UserProfileGrupoAndSalaDto>?
}


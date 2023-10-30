package app.regate.data.chat

import app.regate.compoundmodels.MessageProfile
import app.regate.data.daos.ChatDao
import app.regate.data.daos.GrupoDao
import app.regate.data.daos.MessageInboxDao
import app.regate.data.daos.MessageProfileDao
import app.regate.data.daos.MyGroupsDao
import app.regate.data.daos.UserDao
import app.regate.data.dto.chat.DeleteMessageRequest
import app.regate.data.dto.chat.MessagePublishRequest
import app.regate.data.dto.chat.RequestChatUnreadMessages
import app.regate.data.dto.empresa.conversation.Conversation
import app.regate.data.dto.empresa.conversation.ConversationId
import app.regate.data.dto.empresa.grupo.GrupoMessageDto
import app.regate.data.dto.empresa.grupo.GrupoRequestEstado
import app.regate.data.mappers.MessageConversationToMessage
import app.regate.data.mappers.MessageDtoToMessage
import app.regate.data.mappers.MessageToMessageDto
import app.regate.inject.ApplicationScope
import app.regate.models.Grupo
import app.regate.models.Message
import app.regate.models.MessageInbox
import app.regate.models.MyGroups
import app.regate.models.chat.Chat
import app.regate.util.AppCoroutineDispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import me.tatarka.inject.annotations.Inject
@ApplicationScope
@Inject
class ChatRepository(
    private val chatDataSourceImpl: ChatDataSourceImpl,
    private val dispatchers: AppCoroutineDispatchers,
    private val messageConversationMapper:MessageConversationToMessage,
    private val messageInboxDao: MessageInboxDao,
    private val userDao:UserDao,
    private val chatDao: ChatDao,
    private val messageProfileDao: MessageProfileDao,
    private val messageMapper:MessageDtoToMessage,
    private val messageMapperDto: MessageToMessageDto,
    private val grupoDao: GrupoDao,
    private val myGrupoDao: MyGroupsDao
) {
    fun observeMessages(id: Long): Flow<List<MessageProfile>> {
        return messageProfileDao.getMessages(id)
    }

    suspend fun getChatUnreadMessages(chatId: Long, typeChat: Int) {
        try {
            val lastMessage = messageProfileDao.getLastMessageSended(chatId)
            val chat = chatDao.getChat(chatId)
            var d = RequestChatUnreadMessages(
                chat_id = chatId,
                type_chat = typeChat,
                last_update_chat = Clock.System.now()
            )
            if (lastMessage != null) {
                d = d.copy(last_update_chat = lastMessage.created_at)
            } else if (chat != null) {
                d = d.copy(last_update_chat = chat.updated_at)
            }
            chatDataSourceImpl.getchatUnreadMessages(d).also { result ->
                val messages = result.map { messageMapper.map(it) }
                messageProfileDao.insertAllonConflictIgnore(messages)
            }
        } catch (e: Exception) {
            //TODO()
        }
    }

    suspend fun getConversationId(establecimientoId: Long): ConversationId {
        return chatDataSourceImpl.getConversationId(establecimientoId)
    }

    @Suppress("SuspiciousIndentation")
    suspend fun syncMessages() {
        withContext(dispatchers.computation) {
            try {
//                val profileId = userDao.getUser(0).profile_id
                val messages = messageProfileDao.getUnSendedMessage()
                if (messages.isEmpty()) return@withContext
                messages.map { result ->
                    result.chat?.let { chat ->
                        val requestData = MessagePublishRequest(
                            message = messageMapperDto.map(result.message),
                            type_chat = chat.type_chat,
                            chat_id = chat.id
                        )
                        val res = chatDataSourceImpl.publishMessage(requestData)
                        messageProfileDao.updateSendedMessage(result.message.id, res.id)
                    }
                }
//                val results = chatDataSourceImpl.publishMessage(data).map {
//                    messageConversationMapper.map(it)
//                }
            } catch (e: Exception) {
                //TODO()
            }
        }
    }

    suspend fun updateOrSaveMessage(data: GrupoMessageDto, readed: Boolean) {
        try {
            messageProfileDao.updatedPrimaryKey(data.local_id, data.id)
            val message = messageMapper.map(data)
            messageProfileDao.insertOnConflictIgnore(message.copy(readed = readed))
        } catch (e: Exception) {
            //todo()
        }
    }

    suspend fun saveMessageIgnoreOnConflict(data: GrupoMessageDto, readed: Boolean) {
        try {
            val message = messageMapper.map(data)
            messageProfileDao.insertOnConflictIgnore(message.copy(readed = readed))
        } catch (e: Exception) {
            //todo()
        }
    }

    suspend fun updateUnreadMessages(chatId: Long) {
        withContext(dispatchers.computation) {
            try {
                messageProfileDao.updateUnreadMessages(chatId)
            } catch (e: Exception) {
                //TODO()
            }
        }
    }

    suspend fun saveMessageLocal(data: Message) {
        messageProfileDao.upsert(data)
    }

    suspend fun getConversations(): List<Conversation> {
        return chatDataSourceImpl.getConversations()
    }

    suspend fun getMessages(id: Long, page: Int): Int {
        return try {
            val response = chatDataSourceImpl.getMessages(id, page)
            withContext(dispatchers.computation) {
                response.results.also { results ->
                    val messages = async { results.map { messageConversationMapper.map(it) } }
                    val replies = async {
                        results.filter { it.reply_to != null }.map {
//                     replyMessageMapper.map(it.reply_message)
                            MessageInbox(
                                id = it.reply.id,
                                conversation_id = it.reply.conversation_id,
                                content = it.reply.content,
                                created_at = it.reply.created_at ?: Clock.System.now(),
                                sender_id = it.reply.sender_id
                            )
                        }
                    }
                    val result = messages.await() + replies.await()
                    messageInboxDao.upsertAll(result)
                }
            }
            response.nextPage
        } catch (e: Exception) {
            //TODO()
            0
        }
    }

    suspend fun getUnreadMessages(page: Int) {
        withContext(dispatchers.computation) {
            try {
                val res = chatDataSourceImpl.getUnreadMessages(page)
                res.results.map {
                    messageProfileDao.insertOnConflictIgnore(
                        messageMapper.map(it).copy(readed = false)
                    )
                }
                if (res.page != 0) {
                    getUnreadMessages(res.page)
                }
            } catch (e: Exception) {
                throw e
            }
        }
    }

    //chats
    suspend fun getChats(page: Int): Int {
        return try {
            val response = chatDataSourceImpl.getChats(page)
            withContext(dispatchers.computation) {
                response.results.also { results ->
                    val chats = results.map { result ->
                        Chat(
                            id = result.id,
                            name = result.name,
                            photo = result.photo,
                            type_chat = result.type_chat,
                            parent_id = result.parent_id,
                            updated_at = Clock.System.now()
                        )
                    }
                    chatDao.upsertAll(chats)
                    val grupos = results.map { result ->
                        Grupo(
                            name = result.name,
                            photo = result.photo,
                            id = result.parent_id
                        )
                    }
                    val myGroups = results.map { result ->
                        MyGroups(
                            id = result.parent_id,
                            request_estado = GrupoRequestEstado.JOINED,
                        )
                    }
                    myGrupoDao.insertAllonConflictIgnore(myGroups)
                    chatDao.upsertAll(chats)
                    grupoDao.insertAllonConflictIgnore(grupos)
                }
            }
            response.page //Next Page
        } catch (e: Exception) {
            //TODO()
            0
        }
    }

    suspend fun publishMessage(data: MessagePublishRequest) {
        chatDataSourceImpl.publishMessage(data)
//        try{
//            val res = chatDataSourceImpl.publishMessage(data)
//            messageProfileDao.updatedPrimaryKey(data.message.id,res.id)
//        }catch (e:Exception){
//            throw e
//        }

    }
    suspend fun getDeletedMessages(id:Long){
        withContext(dispatchers.io){
            try {
                chatDataSourceImpl.getDeletedMessages(id).also {result->
                    result.ids.map { id->
                        updateMessageToDeleted(id)
                    }
                }
            }catch (e:Exception){
                throw e
            }
        }
    }

    suspend fun updateMessageToDeleted(id:Long){
        messageProfileDao.updateMessageToDeleted(id)
    }
    suspend fun deleteMessageLocal(id: Long) {
        withContext(dispatchers.io) {
            try {
                messageProfileDao.deleteMessageById(id)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun deleteMessage(d: DeleteMessageRequest) {
        withContext(dispatchers.io) {
            try {
                chatDataSourceImpl.deleteMessage(d).also {
                    messageProfileDao.updateMessageToDeleted(d.id)
                }
            } catch (e: Exception) {
                throw e
            }
        }
    }
}
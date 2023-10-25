package app.regate.data.chat

import app.regate.data.daos.ChatDao
import app.regate.data.daos.GrupoDao
import app.regate.data.daos.MessageInboxDao
import app.regate.data.daos.MessageProfileDao
import app.regate.data.daos.MyGroupsDao
import app.regate.data.daos.UserDao
import app.regate.data.dto.chat.MessagePublishRequest
import app.regate.data.dto.chat.RequestChatUnreadMessages
import app.regate.data.dto.empresa.conversation.Conversation
import app.regate.data.dto.empresa.conversation.ConversationId
import app.regate.data.dto.empresa.conversation.ConversationMessage
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
    suspend fun getUnreadMessages(d: RequestChatUnreadMessages){
        chatDataSourceImpl.getchatUnreadMessages(d).also {result->
            val messages = result.map { messageMapper.map(it) }
            messageProfileDao.insertAllonConflictIgnore(messages)
        }
    }
    suspend fun getChat(id:Long):Chat{
        return chatDao.getChat(id)
    }
    suspend fun getConversationId(establecimientoId:Long):ConversationId{
        return  chatDataSourceImpl.getConversationId(establecimientoId)
    }
    suspend fun syncMessages(conversationId: Long) {
        withContext(dispatchers.computation) {
            try {
                val profileId = userDao.getUser(0).profile_id
                val messages = messageInboxDao.getUnSendedMessage(profileId, conversationId)
                val data = messages.map {
                    ConversationMessage(
                        id = it.id,
                        conversation_id = it.conversation_id,
                        sender_id = it.sender_id,
                        content = it.content,
                        created_at = it.created_at,
                        reply_to = it.reply_to
                    )
                }
                if (messages.isEmpty()) return@withContext
                val results = chatDataSourceImpl.syncMessages(data).map {
                    messageConversationMapper.map(it)
                }
                messageInboxDao.upsertAll(results)
            } catch (e: Exception) {
                //TODO()
            }
        }
    }

    suspend fun saveMessage(data: GrupoMessageDto, readed:Boolean){
        try{
            val message = messageMapper.map(data)
            messageProfileDao.upsert(message.copy(readed = readed))
        }catch (e:Exception){
            //todo()
        }
    }
    suspend fun saveMessageIgnoreOnConflict(data: GrupoMessageDto, readed: Boolean){
        try{
            val message = messageMapper.map(data)
            messageProfileDao.insertOnConflictIgnore(message.copy(readed = readed))
        }catch (e:Exception){
            //todo()
        }
    }
    suspend fun updateUnreadMessages(chatId:Long){
        withContext(dispatchers.computation){
            try{
                messageProfileDao.updateMessages(chatId)
            }catch (e:Exception){
                //TODO()
            }
        }
    }

    suspend fun saveMessageLocal(data: Message){
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

    suspend fun getUnreadMessages(page:Int){
        withContext(dispatchers.computation){
            try{
                val res = chatDataSourceImpl.getUnreadMessages(page)
                res.results.map {
                   messageProfileDao.insertOnConflictIgnore(messageMapper.map(it).copy(readed = false))
                }
                if(res.page != 0){
                    getUnreadMessages(res.page)
                }
            }catch (e:Exception){
                throw  e
            }
        }
    }

    //chats
    suspend fun getChats(page: Int): Int {
        return try {
            val response = chatDataSourceImpl.getChats(page)
            withContext(dispatchers.computation) {

                response.results.also { results ->
                    val chats = results.map {result->
                        Chat(
                            id = result.id,
                            name = result.name,
                            photo = result.photo,
                            type_chat = result.type_chat,
                            parent_id = result.parent_id,
                        )
                    }
                    chatDao.upsertAll(chats)
                    val grupos = results.map { result->
                        Grupo(
                            name = result.name,
                            photo = result.photo,
                            id = result.parent_id
                        )
                    }
                    val myGroups = results.map {result->
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
            response.page
        } catch (e: Exception) {
            //TODO()
            0
        }
    }

    suspend fun publishMessage(data:MessagePublishRequest){
        chatDataSourceImpl.publishMessage(data)
    }
}
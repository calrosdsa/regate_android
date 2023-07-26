package app.regate.data.coin

import app.regate.data.daos.MessageInboxDao
import app.regate.data.daos.UserDao
import app.regate.data.dto.empresa.coin.RecargaCoinDto
import app.regate.data.dto.empresa.conversation.Conversation
import app.regate.data.dto.empresa.conversation.ConversationMessage
import app.regate.data.dto.empresa.conversation.Reply
import app.regate.data.mappers.MessageConversationToMessage
import app.regate.inject.ApplicationScope
import app.regate.models.MessageInbox
import app.regate.util.AppCoroutineDispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import me.tatarka.inject.annotations.Inject
@ApplicationScope
@Inject
class ConversationRepository(
    private val conversationDataSourceImpl: ConversationDataSourceImpl,
    private val dispatchers: AppCoroutineDispatchers,
    private val messageConversationMapper:MessageConversationToMessage,
    private val messageInboxDao: MessageInboxDao,
    private val userDao:UserDao
){
    suspend fun syncMessages(conversationId:Long){
        withContext(dispatchers.computation){
            try{
                val profileId = userDao.getUser(0).profile_id
                val messages = messageInboxDao.getUnSendedMessage(profileId,conversationId)
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
                val results = conversationDataSourceImpl.syncMessages(data).map {
                    messageConversationMapper.map(it)
                }
                messageInboxDao.upsertAll(results)
            }catch (e:Exception){
                //TODO()
            }
        }
    }
    suspend fun saveMessage(message:ConversationMessage){
        withContext(dispatchers.computation){
            val data = messageConversationMapper.map(message)
            messageInboxDao.upsert(data)
        }
    }
    suspend fun saveMessageLocal(message:MessageInbox){
        withContext(dispatchers.computation){
            messageInboxDao.upsert(message)
        }
    }
    suspend fun getConversations():List<Conversation>{
        return conversationDataSourceImpl.getConversations()
    }
    suspend fun getMessages(id:Long,page:Int) {
        withContext(dispatchers.computation) {
            try {
                conversationDataSourceImpl.getMessages(id, page).also { results ->
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
            } catch (e: Exception) {
                //TODO()
            }
//                 messageProfileDao.upsertAll(results)
        }
    }
}
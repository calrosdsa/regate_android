package app.regate.data.daos

import androidx.paging.PagingSource
import app.regate.compoundmodels.MessageProfile
import app.regate.compoundmodels.MessageWithChat
import app.regate.models.Message
import kotlinx.coroutines.flow.Flow

interface MessageProfileDao:EntityDao<Message> {
    fun observeMessages(id:Long): PagingSource<Int, MessageProfile>
    suspend fun getReplyMessage(id:Long):MessageProfile
    fun getMessages(id:Long):Flow<List<MessageProfile>>
    suspend fun updateUnreadMessages(id:Long)
    suspend fun updateSendedMessage(id:Long,newId: Long)
    suspend fun getUnSendedMessage():List<MessageWithChat>
    suspend fun getLastMessageSended(chatId:Long):Message?
    suspend fun updatedPrimaryKey(id:Long,newId:Long)
//    suspend fun deleteById(id:Long)
}
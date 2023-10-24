package app.regate.data.daos

import androidx.paging.PagingSource
import app.regate.compoundmodels.MessageProfile
import app.regate.models.Message
import kotlinx.coroutines.flow.Flow

interface MessageProfileDao:EntityDao<Message> {
    fun observeMessages(id:Long): PagingSource<Int, MessageProfile>
    suspend fun getReplyMessage(id:Long):MessageProfile
    fun getMessages(id:Long):Flow<List<MessageProfile>>
    suspend fun updateMessages(id:Long)
    suspend fun getUnSendedMessage(profileId:Long,grupoId:Long):List<Message>

}
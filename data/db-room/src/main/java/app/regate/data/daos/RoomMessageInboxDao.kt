package app.regate.data.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import app.regate.compoundmodels.MessageProfile
import app.regate.models.Message
import app.regate.models.MessageInbox

@Dao
abstract class RoomMessageInboxDao:RoomEntityDao<MessageInbox>,MessageInboxDao{
    @Transaction
    @Query("SELECT * FROM message_inbox where conversation_id = :id ORDER BY datetime(created_at) DESC")
    abstract override fun observeMessages(id: Long): PagingSource<Int, MessageInbox>

    @Query("select *  from message_inbox where sender_id = :sender and sended = 0 and conversation_id = :conversationId")
    abstract override suspend fun getUnSendedMessage(sender: Long,conversationId:Long): List<MessageInbox>
}
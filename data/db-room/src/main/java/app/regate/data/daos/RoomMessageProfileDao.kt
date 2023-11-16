package app.regate.data.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import app.regate.compoundmodels.MessageProfile
import app.regate.compoundmodels.MessageWithChat
import app.regate.models.Message
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RoomMessageProfileDao:RoomEntityDao<Message>,MessageProfileDao {
    @Transaction
    @Query("SELECT * FROM messages where chat_id = :id ORDER BY datetime(created_at) DESC")
    abstract override fun observeMessages(id: Long): PagingSource<Int, MessageProfile>
    @Transaction
    @Query("SELECT count(*) FROM messages where readed = 0")
    abstract override fun observeUnreadMessagesCount(): Flow<Int>
    @Transaction
    @Query("SELECT * FROM messages where chat_id = :id ORDER BY datetime(created_at) DESC LIMIT 5")
    abstract override fun getMessages(id: Long):Flow<List<MessageProfile>>

    @Query("update messages set readed = 1 where chat_id= :id and readed = 0")
    abstract override suspend fun updateUnreadMessages(id: Long)
    @Query("update messages set sended = 1,id = :newId where id = :id and sended = 0")
    abstract override suspend fun updateSendedMessage(id: Long,newId: Long)
    @Transaction
    @Query("SELECT * FROM messages where id = :id")
    abstract override suspend fun getReplyMessage(id: Long): MessageProfile

    @Transaction
    @Query("select *  from messages where sended = 0")
    abstract override suspend fun getUnSendedMessage(): List<MessageWithChat>

    @Query("select * from messages where sended = 1 and chat_id = :chatId order by created_at desc limit 1")
    abstract override suspend fun getLastMessageSended(chatId: Long): Message?

    @Query("update messages set id = :newId ,sended = 1,readed = 1 where id= :id")
    abstract override suspend fun updatedPrimaryKey(id: Long,newId:Long)

    @Query("delete from messages where id = :id")
    abstract override suspend fun deleteMessageById(id: Long)

    @Query("update messages set is_deleted = 1 where id = :id")
    abstract override suspend fun updateMessageToDeleted(id: Long)

}
package app.regate.data.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import app.regate.compoundmodels.MessageProfile
import app.regate.models.Message
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RoomMessageProfileDao:RoomEntityDao<Message>,MessageProfileDao {
    @Transaction
    @Query("SELECT * FROM messages where grupo_id = :id ORDER BY datetime(created_at) DESC")
    abstract override fun observeMessages(id: Long): PagingSource<Int, MessageProfile>

    @Transaction
    @Query("SELECT * FROM messages where grupo_id = :id ORDER BY datetime(created_at) DESC LIMIT 5")
    abstract override fun getMessages(id: Long):Flow<List<MessageProfile>>

    @Query("update messages set readed = 1 where grupo_id= :grupoId and readed = 0")
    abstract override suspend fun updateMessages(grupoId: Long)
    @Transaction
    @Query("SELECT * FROM messages where id = :id")
    abstract override suspend fun getReplyMessage(id: Long): MessageProfile

    @Query("select *  from messages where profile_id = :profileId and sended = 0 and grupo_id = :grupoId")
    abstract override suspend fun getUnSendedMessage(profileId: Long,grupoId:Long): List<Message>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract override suspend fun upsertOnConflictStrategyIgnore(entity: Message)


}
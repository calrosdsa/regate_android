package app.regate.data.daos

import androidx.paging.PagingSource
import androidx.room.Dao
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
    @Query("SELECT * FROM messages where id = :id")
    abstract override suspend fun getReplyMessage(id: Long): MessageProfile

}
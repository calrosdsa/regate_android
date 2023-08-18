package app.regate.data.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import app.regate.compoundmodels.MessageSalaWithProfile
import app.regate.models.Message
import app.regate.models.MessageSala

@Dao
abstract class RoomMessageSalaDao:MessageSalaDao,RoomEntityDao<MessageSala> {
    @Transaction
    @Query("SELECT * FROM message_sala where sala_id = :id ORDER BY datetime(created_at) DESC")
    abstract override fun observeMessages(id: Long): PagingSource<Int, MessageSalaWithProfile>

    @Query("SELECT * FROM message_sala where sala_id = :id ORDER BY datetime(created_at) DESC LIMIT 5")
    abstract override fun getMessages(id: Long):List<MessageSalaWithProfile>
    @Query("SELECT * FROM message_sala where id = :id")
    abstract override suspend fun getReplyMessage(id: Long): MessageSalaWithProfile

    @Query("select *  from message_sala where profile_id = :profileId and sended = 0 and sala_id = :salaId")
    abstract override suspend fun getUnSendedMessage(profileId: Long,salaId:Long): List<MessageSala>
}
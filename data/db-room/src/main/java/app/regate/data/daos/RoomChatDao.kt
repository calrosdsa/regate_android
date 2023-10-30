package app.regate.data.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import app.regate.compoundmodels.MessageProfile
import app.regate.models.chat.Chat
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RoomChatDao:ChatDao,RoomEntityDao<Chat> {
    @Transaction
    @Query(
        """
               SELECT c.id,c.name,c.photo,
    (select content from messages where chat_id = c.id order by created_at DESC limit 1) as last_message,
    (select created_at from messages where chat_id = c.id order by created_at DESC limit 1) as last_message_created,
    (select count(*) from messages where chat_id = c.id and readed = 0) as messages_count,
    (select is_deleted from messages where chat_id = c.id order by created_at DESC limit 1) as is_message_deleted,
    type_chat,parent_id,updated_at
    FROM chat as c order by last_message_created desc
    """
    )
    abstract override fun observeChatsPaging(): PagingSource<Int, Chat>



    @Transaction
    @Query("select * from chat where id = :id")
    abstract override fun observeChat(id: Long): Flow<Chat>
    @Query("select * from chat where id = :id")
    abstract override suspend fun getChat(id:Long):Chat?

}
//
//@Query("""
//            SELECT c.*,(select content from messages
//             where grupo_id =  c.id
//            order by created_at desc limit 1) as last_message,
//            (select created_at from messages
//             where grupo_id =  c.id
//            order by created_at desc limit 1) as last_message_created,
//            (select count(*) from messages
//             where grupo_id =  c.id) as messages_count
//             FROM chat c
//    """)
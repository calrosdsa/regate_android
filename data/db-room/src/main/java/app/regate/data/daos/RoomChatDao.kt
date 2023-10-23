package app.regate.data.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import app.regate.compoundmodels.MessageProfile
import app.regate.models.chat.Chat

@Dao
abstract class RoomChatDao:ChatDao,RoomEntityDao<Chat> {
    @Transaction
    @Query("""
            SELECT * FROM chat 
    """)
    abstract override fun observeChats(): PagingSource<Int, Chat>
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
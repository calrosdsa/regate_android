package app.regate.data.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import app.regate.models.Emoji
import app.regate.models.LabelType
import app.regate.models.Labels
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RoomEmojiDao:EmojiDao,RoomEntityDao<Emoji> {
    @Transaction
    @Query("select * from emoji where category = :category")
    abstract override suspend fun getEmojisByCategory(category: String): List<Emoji>
}
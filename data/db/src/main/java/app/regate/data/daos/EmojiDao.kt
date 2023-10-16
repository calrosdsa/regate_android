package app.regate.data.daos

import app.regate.models.Emoji
import app.regate.models.LabelType
import app.regate.models.Labels
import kotlinx.coroutines.flow.Flow

interface EmojiDao:EntityDao<Emoji> {
    suspend fun getEmojisByCategory(category:String):List<Emoji>
}
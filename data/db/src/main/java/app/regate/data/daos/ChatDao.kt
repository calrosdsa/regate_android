package app.regate.data.daos

import androidx.paging.PagingSource
import app.regate.models.chat.Chat
import kotlinx.coroutines.flow.Flow

interface ChatDao:EntityDao<Chat> {
    fun observeChatsPaging(): PagingSource<Int, Chat>
    fun observeChats(page:Int,offset:Int):Flow<List<Chat>>
}
package app.regate.data.daos

import androidx.paging.PagingSource
import app.regate.models.chat.Chat
import kotlinx.coroutines.flow.Flow

interface ChatDao:EntityDao<Chat> {
    fun observeChatsPaging(): PagingSource<Int, Chat>
    fun observeChat(id:Long):Flow<Chat>
    fun observeChatByType(parentId:Long,typeChat: Int):Flow<Chat>
    suspend fun getChatByType(pId:Long, typeChat:Int):Chat?
    suspend fun getChat(id:Long):Chat?
    suspend fun updateWhenUserLeave(chatId:Long)
    suspend fun deleteById(id: Long)
    suspend fun deleteAll()
}
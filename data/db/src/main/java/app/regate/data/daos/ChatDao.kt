package app.regate.data.daos

import androidx.paging.PagingSource
import app.regate.compoundmodels.MessageProfile
import app.regate.models.chat.Chat

interface ChatDao:EntityDao<Chat> {
    fun observeChats(): PagingSource<Int, Chat>
}
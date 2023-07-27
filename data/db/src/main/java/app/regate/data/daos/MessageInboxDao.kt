package app.regate.data.daos

import androidx.paging.PagingSource
import app.regate.compoundmodels.MessageConversation
import app.regate.models.MessageInbox

interface MessageInboxDao:EntityDao<MessageInbox> {
    fun observeMessages(id:Long): PagingSource<Int, MessageConversation>

    suspend fun getUnSendedMessage(sender:Long,conversationId:Long):List<MessageInbox>
}
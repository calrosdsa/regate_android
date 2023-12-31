package app.regate.data.daos

import androidx.paging.PagingSource
import app.regate.compoundmodels.MessageSalaWithProfile
import app.regate.models.chat.MessageSala

interface MessageSalaDao:EntityDao<MessageSala> {
    fun observeMessages(id:Long): PagingSource<Int, MessageSalaWithProfile>
    suspend fun getReplyMessage(id:Long): MessageSalaWithProfile

    fun getMessages(id:Long):List<MessageSalaWithProfile>
    suspend fun getUnSendedMessage(profileId:Long,salaId:Long):List<MessageSala>
}
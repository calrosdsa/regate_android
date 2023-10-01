package app.regate.domain.observers.pagination

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import app.regate.compoundmodels.MessageConversation
import app.regate.data.coin.ConversationRepository
import app.regate.data.daos.MessageInboxDao
import app.regate.domain.PagingInteractor
import app.regate.domain.pagination.mediator.PagingMessagesInboxMediator
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObservePagerMessagesInbox(
    private val repository: ConversationRepository,
    private val messageInboxDao: MessageInboxDao
):PagingInteractor<ObservePagerMessagesInbox.Params,MessageConversation>() {

    override fun createObservable(params: Params): Flow<PagingData<MessageConversation>> {
        return Pager(
            config = params.pagingConfig,
            remoteMediator = PagingMessagesInboxMediator(fetch = {
                repository.getMessages(params.conversationId,it)
            }),
            pagingSourceFactory = {
                messageInboxDao.observeMessages(params.conversationId)
            }
        ).flow
    }


    data class Params(
        override val pagingConfig: PagingConfig,
        val conversationId:Long,
        var page :Int = 1
    ) : Parameters<MessageConversation>
}
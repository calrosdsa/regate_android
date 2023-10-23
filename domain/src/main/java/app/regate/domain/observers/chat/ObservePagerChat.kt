package app.regate.domain.observers.chat

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import app.regate.compoundmodels.MessageProfile
import app.regate.data.coin.ConversationRepository
import app.regate.data.daos.ChatDao
import app.regate.data.daos.MessageProfileDao
import app.regate.data.grupo.GrupoRepository
import app.regate.domain.PagingInteractor
import app.regate.domain.pagination.chat.PagingChatsMediator
import app.regate.domain.pagination.mediator.PagingMessagesMediator
import app.regate.models.chat.Chat
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObservePagerChat(
    private val conversationRepository: ConversationRepository,
    private val chatDao: ChatDao,
):PagingInteractor<ObservePagerChat.Params,Chat>() {

    override fun createObservable(params: Params): Flow<PagingData<Chat>> {
        return Pager(
            config = params.pagingConfig,
            remoteMediator = PagingChatsMediator(fetch = {page->
                conversationRepository.getChats(page)
            }),
            pagingSourceFactory = {
                chatDao.observeChats()
            }
        ).flow
    }


    data class Params(
        override val pagingConfig: PagingConfig,
        var page :Int = 1
    ) : Parameters<Chat>
}
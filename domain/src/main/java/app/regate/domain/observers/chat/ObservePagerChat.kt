package app.regate.domain.observers.chat

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import app.regate.data.chat.ChatRepository
import app.regate.data.daos.ChatDao
import app.regate.domain.PagingInteractor
import app.regate.domain.pagination.chat.PagingChatsMediator
import app.regate.models.chat.Chat
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObservePagerChat(
    private val chatRepository: ChatRepository,
    private val chatDao: ChatDao,
):PagingInteractor<ObservePagerChat.Params,Chat>() {

    override fun createObservable(params: Params): Flow<PagingData<Chat>> {
        return Pager(
            config = params.pagingConfig,
            remoteMediator = PagingChatsMediator(fetch = {page->
                chatRepository.getChats(page)
            }),
            pagingSourceFactory = {
                chatDao.observeChatsPaging()
            }
        ).flow
    }


    data class Params(
        override val pagingConfig: PagingConfig,
        var page :Int = 1
    ) : Parameters<Chat>
}
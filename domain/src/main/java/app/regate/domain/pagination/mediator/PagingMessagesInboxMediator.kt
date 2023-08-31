package app.regate.domain.pagination.mediator

import androidx.paging.ExperimentalPagingApi
import app.cash.paging.LoadType
import app.cash.paging.PagingState
import app.cash.paging.RemoteMediator
import app.regate.compoundmodels.MessageConversation
import app.regate.compoundmodels.MessageProfile

internal class PagingMessagesInboxMediator(
    private val fetch: suspend (page: Int) -> Int,
): RemoteMediator<Int, MessageConversation>() {
    private var currentPage:Int = 1
//    override suspend fun initialize(): InitializeAction {
//        fetch(1)
//        return  super.initialize()
//    }
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MessageConversation>
    ): MediatorResult {
    val page = when (loadType) {
        LoadType.REFRESH -> {
            1
        }
        LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
        LoadType.APPEND -> {
            state.lastItemOrNull()
                ?: return MediatorResult.Success(endOfPaginationReached = true)
            currentPage
        }

        else -> { 0 }
    }
    return try {
        if(currentPage != 0){
            val nextPage = fetch(page)
            currentPage = nextPage
        }
        MediatorResult.Success(endOfPaginationReached = currentPage == 0)
    } catch (t: Throwable) {
        MediatorResult.Error(t)
    }
    }


}
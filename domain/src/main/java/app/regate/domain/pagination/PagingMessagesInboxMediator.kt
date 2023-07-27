package app.regate.domain.pagination

import androidx.paging.ExperimentalPagingApi
import app.cash.paging.LoadType
import app.cash.paging.PagingState
import app.cash.paging.RemoteMediator
import app.regate.compoundmodels.MessageConversation
import app.regate.compoundmodels.MessageProfile

internal class PagingMessagesInboxMediator(
    private var currentPage:Int,
    private val fetch: suspend (page: Int) -> Unit,
): RemoteMediator<Int, MessageConversation>() {
    override suspend fun initialize(): InitializeAction {
        fetch(1)
        return  super.initialize()
    }
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MessageConversation>
    ): MediatorResult {
       when (loadType) {
            LoadType.REFRESH -> {}
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                state.lastItemOrNull()
                    ?: return MediatorResult.Success(endOfPaginationReached = true)
                currentPage += 1
                fetch(currentPage)
            }
        }
        return try {
            MediatorResult.Success(endOfPaginationReached = false)
        } catch (t: Throwable) {
            MediatorResult.Error(t)
        }
    }


}
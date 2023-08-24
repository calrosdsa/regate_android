package app.regate.domain.observers.pagination

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import app.regate.compoundmodels.MessageSalaWithProfile
import app.regate.data.daos.MessageSalaDao
import app.regate.data.sala.SalaRepository
import app.regate.domain.PagingInteractor
import app.regate.domain.pagination.mediator.PagingMessagesSalaMediator
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObservePagerMessagesSala(
    private val repository: SalaRepository,
    private val messageSalaDao: MessageSalaDao
):PagingInteractor<ObservePagerMessagesSala.Params,MessageSalaWithProfile>() {

    override fun createObservable(params: Params): Flow<PagingData<MessageSalaWithProfile>> {
        return Pager(
            config = params.pagingConfig,
            remoteMediator = PagingMessagesSalaMediator(fetch = {
                repository.getMessagesSala(params.salaId,it)
            }),
            pagingSourceFactory = {
                messageSalaDao.observeMessages(params.salaId)
            }
        ).flow
    }


    data class Params(
        override val pagingConfig: PagingConfig,
        val salaId:Long,
        var page :Int = 1
    ) : Parameters<MessageSalaWithProfile>
}
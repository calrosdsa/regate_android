package app.regate.domain.observers.pagination

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import app.regate.compoundmodels.MessageProfile
import app.regate.data.daos.MessageProfileDao
import app.regate.data.grupo.GrupoRepository
import app.regate.domain.PagingInteractor
import app.regate.domain.pagination.mediator.PagingMessagesMediator
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObservePagerMessages(
    private val repository: GrupoRepository,
    private val messageProfileDao: MessageProfileDao
):PagingInteractor<ObservePagerMessages.Params,MessageProfile>() {

    override fun createObservable(params: Params): Flow<PagingData<MessageProfile>> {
        return Pager(
            config = params.pagingConfig,
            remoteMediator = PagingMessagesMediator(fetch = {
                repository.getMessagesGrupo(params.grupoId,it)
            }),
            pagingSourceFactory = {
                messageProfileDao.observeMessages(params.grupoId)
            }
        ).flow
    }


    data class Params(
        override val pagingConfig: PagingConfig,
        val grupoId:Long,
        var page :Int = 1
    ) : Parameters<MessageProfile>
}
package app.regate.domain.observers.grupo

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import app.regate.compoundmodels.grupo.UserInvitationGrupo
import app.regate.data.daos.GrupoDao
import app.regate.data.grupo.GrupoRepository
import app.regate.domain.PagingInteractor
import app.regate.domain.pagination.grupo.PagingInvitationMediator
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObservePagerInvitations(
//    private val repository: GrupoRepository,
    private val grupoRepository: GrupoRepository,
    private val grupoDao: GrupoDao
):PagingInteractor<ObservePagerInvitations.Params,UserInvitationGrupo>() {

    override fun createObservable(params: Params): Flow<PagingData<UserInvitationGrupo>> {
        return Pager(
            config = params.pagingConfig,
            remoteMediator = PagingInvitationMediator(fetch = {page->
                grupoRepository.updateInvitationSource(params.grupoId,page)
//                repository.getMessagesGrupo(params.grupoId,it)
            }),
            pagingSourceFactory = {
                grupoDao.observeInvitations(params.grupoId)
            }
        ).flow
    }


    data class Params(
        override val pagingConfig: PagingConfig,
        val grupoId:Long,
    ) : Parameters<UserInvitationGrupo>
}
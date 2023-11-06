package app.regate.domain.observers.grupo

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import app.regate.compoundmodels.grupo.UserInvitation
import app.regate.compoundmodels.grupo.UserInvitationGrupo
import app.regate.data.daos.GrupoDao
import app.regate.data.grupo.GrupoRepository
import app.regate.domain.PagingInteractor
import app.regate.domain.pagination.grupo.PagingInvitationMediator
import app.regate.domain.pagination.grupo.PagingUserInvitationMediator
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObservePagerUserInvitations(
//    private val repository: GrupoRepository,
    private val grupoRepository: GrupoRepository,
    private val grupoDao: GrupoDao
):PagingInteractor<ObservePagerUserInvitations.Params,UserInvitation>() {

    override fun createObservable(params: Params): Flow<PagingData<UserInvitation>> {
        return Pager(
            config = params.pagingConfig,
            remoteMediator = PagingUserInvitationMediator(fetch = {page->
                grupoRepository.updateUserInvitationsSource(params.profileId,page)
//                repository.getMessagesGrupo(params.grupoId,it)
            }),
            pagingSourceFactory = {
                grupoDao.observeUserInvitations(params.profileId)
            }
        ).flow
    }


    data class Params(
        override val pagingConfig: PagingConfig,
        val profileId:Long,
    ) : Parameters<UserInvitation>
}
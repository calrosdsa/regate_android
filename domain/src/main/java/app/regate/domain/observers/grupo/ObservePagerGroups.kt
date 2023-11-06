package app.regate.domain.observers.grupo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import app.regate.compoundmodels.grupo.UserInvitation
import app.regate.data.daos.GrupoDao
import app.regate.data.grupo.GrupoRepository
import app.regate.domain.PagingInteractor
import app.regate.domain.pagination.grupo.PaginationGroupsMediator
import app.regate.domain.pagination.grupo.PagingUserInvitationMediator
import app.regate.models.grupo.Grupo
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObservePagerGroups (
    private val grupoRepository: GrupoRepository,
    private val grupoDao: GrupoDao
): PagingInteractor<ObservePagerGroups.Params, Grupo>() {

    override fun createObservable(params: Params): Flow<PagingData<Grupo>> {
        return Pager(
            config = params.pagingConfig,
            remoteMediator = PaginationGroupsMediator(fetch = {page->
                grupoRepository.updateGruposSource(page)
//                repository.getMessagesGrupo(params.grupoId,it)
            }),
            pagingSourceFactory = {
                grupoDao.observePaginationGroups()
            }
        ).flow
    }

    data class Params(
        override val pagingConfig: PagingConfig,
    ) : Parameters<Grupo>
}
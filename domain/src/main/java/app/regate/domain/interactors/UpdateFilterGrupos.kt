package app.regate.domain.interactors

import app.regate.data.dto.empresa.grupo.FilterGrupoData
import app.regate.data.grupo.GrupoRepository
import app.regate.domain.Interactor
import app.regate.util.AppCoroutineDispatchers
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class UpdateFilterGrupos(
    private val grupoRepository: GrupoRepository,
    private val dispatchers: AppCoroutineDispatchers
):Interactor<UpdateFilterGrupos.Params>() {
    override suspend fun doWork(params: Params) {
        withContext(dispatchers.computation){
            grupoRepository.filterGrupos(params.d,1)
        }
    }
    data class  Params(
        val d:FilterGrupoData
    )

}
package app.regate.domain.interactors

import app.regate.data.establecimiento.EstablecimientoRepository
import app.regate.data.establecimiento.EstablecimientoStore
import app.regate.data.instalacion.InstalacionRepository
import app.regate.data.util.fetch
import app.regate.domain.Interactor
import app.regate.util.AppCoroutineDispatchers
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject




@Inject
class UpdateEstablecimiento(
    private val establecimientoRepository: EstablecimientoRepository,
    private val instalacionRepository: InstalacionRepository,
    private val dispatchers: AppCoroutineDispatchers,
    private val establecimientoStore: EstablecimientoStore
) : Interactor<UpdateEstablecimiento.Params>(){
    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io){
            establecimientoStore.fetch(
                key = params.id,
                forceFresh = params.forceLoad
            )
            establecimientoRepository.updateEstablecimientoDetail(params.id)
            instalacionRepository.getInstalaciones(params.id)
        }
    }

    data class Params(
        val id: Long,
        val forceLoad:Boolean = false
    )
}
package app.regate.domain.interactors

import app.regate.data.establecimiento.EstablecimientoRepository
import app.regate.data.instalacion.InstalacionRepository
import app.regate.domain.Interactor
import app.regate.util.AppCoroutineDispatchers
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class UpdateEstablecimientoDetail(
    private val establecimientoRepository: EstablecimientoRepository,
    private val instalacionRepository: InstalacionRepository,
    private val dispatchers: AppCoroutineDispatchers
) : Interactor<UpdateEstablecimientoDetail.Params>(){

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.computation){
            establecimientoRepository.updateEstablecimientoDetail(params.id).also {
            instalacionRepository.getInstalaciones(params.id)
            }
        }
    }

    data class Params(val id: Long)
}

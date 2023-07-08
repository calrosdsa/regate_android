package app.regate.domain.interactors

import app.regate.data.establecimiento.EstablecimientoRepository
import app.regate.domain.Interactor
import app.regate.inject.ApplicationScope
import app.regate.util.AppCoroutineDispatchers
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class UpdateEstablecimiento(
    private val establecimientoRepository: EstablecimientoRepository,
    private val dispatchers: AppCoroutineDispatchers
) : Interactor<UpdateEstablecimiento.Params>(){

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.computation){
            establecimientoRepository.updateEstablecimiento(params.id)
        }
    }

    data class Params(val id: Long)
}
package app.regate.domain.interactors

import app.regate.data.instalacion.InstalacionRepository
import app.regate.domain.Interactor
import app.regate.util.AppCoroutineDispatchers
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class UpdateInstalaciones (
        private val instalacionRepository: InstalacionRepository,
        private val dispatchers: AppCoroutineDispatchers,
): Interactor<UpdateInstalaciones.Params>(){

        override suspend fun doWork(params: Params) {

                withContext(dispatchers.computation){
                        instalacionRepository.getInstalaciones(params.id)
                }
        }

        data class Params(val id: Long)
}
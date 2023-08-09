package app.regate.domain.interactors

import app.regate.data.instalacion.InstalacionRepository
import app.regate.data.instalacion.InstalacionStore
import app.regate.data.util.fetch
import app.regate.domain.Interactor
import app.regate.util.AppCoroutineDispatchers
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class UpdateInstalacion (
        private val dispatchers: AppCoroutineDispatchers,
        private val instalacionStore: InstalacionStore
): Interactor<UpdateInstalacion.Params>(){

        override suspend fun doWork(params: Params) {
                withContext(dispatchers.io){
                        instalacionStore.fetch(
                                key = params.id,
                                forceFresh = params.forceLoad
                        )
                }
        }

        data class Params(val id: Long, val forceLoad: Boolean=false)
}
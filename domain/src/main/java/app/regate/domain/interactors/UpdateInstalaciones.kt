package app.regate.domain.interactors

import app.regate.data.instalacion.InstalacionRepository
import app.regate.data.instalacion.InstalacionesStore
import app.regate.data.util.fetch
import app.regate.domain.Interactor
import app.regate.util.AppCoroutineDispatchers
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class UpdateInstalaciones (
        private val dispatchers: AppCoroutineDispatchers,
        private val instalacionesStore: InstalacionesStore,
): Interactor<UpdateInstalaciones.Params>(){

        override suspend fun doWork(params: Params) {
                withContext(dispatchers.io){
                        instalacionesStore.fetch(
                                key = params.id,
                                forceFresh = params.forceLoad
                        )
                }
        }

        data class Params(
                val id: Long,
                val forceLoad:Boolean = false
        )
}
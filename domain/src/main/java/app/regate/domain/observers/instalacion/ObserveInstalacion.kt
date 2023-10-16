package app.regate.domain.observers.instalacion

import app.regate.data.instalacion.InstalacionRepository
import app.regate.domain.SubjectInteractor
import app.regate.models.Instalacion
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveInstalacion (
    private val instalacionRepository: InstalacionRepository,
): SubjectInteractor<ObserveInstalacion.Params, Instalacion>(){

    override fun createObservable(params: Params): Flow<Instalacion> {
        return instalacionRepository.observeInstalacion(params.id)
    }

    data class Params(val id: Long)
}
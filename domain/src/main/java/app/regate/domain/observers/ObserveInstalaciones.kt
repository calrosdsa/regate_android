package app.regate.domain.observers

import app.regate.data.instalacion.InstalacionRepository
import app.regate.domain.SubjectInteractor
import app.regate.models.Instalacion
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveInstalaciones (
    private val instalacionRepository: InstalacionRepository,
): SubjectInteractor<ObserveInstalaciones.Params, List<Instalacion>>(){

    override fun createObservable(params: Params): Flow<List<Instalacion>> {
        return instalacionRepository.observeInstalaciones(params.id)
    }

    data class Params(val id: Long)
}
package app.regate.domain.observers

import app.regate.data.instalacion.InstalacionRepository
import app.regate.domain.SubjectInteractor
import app.regate.models.Instalacion
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveInstalacionesAvailables(
    private val instalacionRepository: InstalacionRepository
):SubjectInteractor<ObserveInstalacionesAvailables.Params,List<Instalacion>>() {

    override fun createObservable(params: Params): Flow<List<Instalacion>> {
        return instalacionRepository.observeInstalacionesAvailables(params.ids)
    }

    data class Params(
        val ids:List<Long>
    )
}
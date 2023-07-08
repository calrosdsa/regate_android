package app.regate.domain.observers

import app.regate.data.instalacion.CupoRepository
import app.regate.domain.SubjectInteractor
import app.regate.models.Cupo
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveCupos(
    private val cupoRepository: CupoRepository
) :SubjectInteractor<ObserveCupos.Params,List<Cupo>>(){

    override fun createObservable(params: Params): Flow<List<Cupo>> {
        return cupoRepository.observeCupos(params.id)
    }

    data class Params(
        val id:Long
    )
}
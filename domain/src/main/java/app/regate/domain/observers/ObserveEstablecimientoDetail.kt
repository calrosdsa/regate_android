package app.regate.domain.observers

import app.regate.data.establecimiento.EstablecimientoRepository
import app.regate.domain.SubjectInteractor
import app.regate.models.Establecimiento
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveEstablecimientoDetail (
    private val establecimientoRepository: EstablecimientoRepository
    ):SubjectInteractor<ObserveEstablecimientoDetail.Params,Establecimiento>(){

    override fun createObservable(params: Params): Flow<Establecimiento> {
        return establecimientoRepository.observeEstablecimiento(params.id)
    }

    data class Params(val id: Long)
}
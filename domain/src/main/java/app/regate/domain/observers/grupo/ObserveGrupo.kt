package app.regate.domain.observers.grupo

import app.regate.data.daos.GrupoDao
import app.regate.domain.SubjectInteractor
import app.regate.models.Grupo
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveGrupo(
    private val grupoDao:GrupoDao
):SubjectInteractor<ObserveGrupo.Param,Grupo>() {
    override fun createObservable(params: Param): Flow<Grupo> {
        return grupoDao.observeGrupo(params.id)
    }
    data class Param(
        val id:Long
    )
}
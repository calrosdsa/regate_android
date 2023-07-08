package app.regate.data.daos

import app.regate.models.Grupo
import kotlinx.coroutines.flow.Flow

interface GrupoDao:EntityDao<Grupo> {
    fun observeGrupo(id:Long):Flow<Grupo>
    fun observeGrupos():Flow<List<Grupo>>
    fun deleteAll()

}
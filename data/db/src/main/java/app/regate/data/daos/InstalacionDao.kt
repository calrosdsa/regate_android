package app.regate.data.daos

import app.regate.compoundmodels.InstalacionCategoryCount
import app.regate.models.Instalacion
import app.regate.models.LabelType
import kotlinx.coroutines.flow.Flow

interface InstalacionDao:EntityDao<Instalacion> {

    fun observeInstalaciones(id:Long): Flow<List<Instalacion>>

    fun observeInstalacion(id:Long):Flow<Instalacion>

    fun observeInstalacionesAvailables(ids:List<Long>):Flow<List<Instalacion>>
    fun observeGroupInstalacionByCategory(id:Long,type:LabelType):Flow<List<InstalacionCategoryCount>>
    suspend fun delete(id: Long)
    suspend fun deleteAll()
}
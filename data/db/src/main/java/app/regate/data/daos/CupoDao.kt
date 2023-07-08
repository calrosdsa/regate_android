package app.regate.data.daos

import app.regate.compoundmodels.InstalacionCupos
import app.regate.models.Cupo
import kotlinx.coroutines.flow.Flow

interface CupoDao:EntityDao<Cupo> {
    fun observeCupos(id:Long):Flow<List<Cupo>>

    fun observeLastCupo():Flow<Cupo?>
    suspend fun getInstalacionCupos(id:Long):InstalacionCupos
    suspend fun deleteCupos()
}
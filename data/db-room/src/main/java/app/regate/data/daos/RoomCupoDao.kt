package app.regate.data.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import app.regate.compoundmodels.InstalacionCupos
import app.regate.models.Cupo
import kotlinx.coroutines.flow.Flow
@Dao
abstract class RoomCupoDao : CupoDao,RoomEntityDao<Cupo> {
    @Transaction
    @Query("SELECT * FROM cupos WHERE instalacion_id = :id")
    abstract override fun observeCupos(id: Long): Flow<List<Cupo>>

    @Transaction
    @Query("SELECT * FROM cupos limit 1")
    abstract override fun observeLastCupo(): Flow<Cupo?>

    @Query("DELETE FROM cupos")
    abstract override suspend fun deleteCupos()

    @Transaction
    @Query("SELECT * FROM instalaciones where id = :id")
    abstract override suspend fun getInstalacionCupos(id:Long): InstalacionCupos
}